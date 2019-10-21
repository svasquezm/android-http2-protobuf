package cl.svasquezm.server

import cl.svasquezm.proto.Models
import io.javalin.Javalin
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory
import org.eclipse.jetty.server.HttpConfiguration
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import java.io.ByteArrayInputStream

class ProtobufHTTP2Server {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val app = Javalin.create {
                it.server { createServer() }
            }.start()

            val model = Models.User.newBuilder().apply {
                name = "Sebastian"
                email = "seba.vasquez.m@gmail.com"
                type = Models.User.Type.MOBILE
                addPets(Models.Pet.newBuilder().apply {
                    type = "dog"
                    name = "luka"
                })
                addPets(Models.Pet.newBuilder().apply {
                    type = "cat"
                    name = "ema"
                })
                addPets(Models.Pet.newBuilder().apply {
                    type = "rabbit"
                    name = "gaspar"
                })
            }.build()

            app.get("/") {
                it.contentType("application/protobuf")
                it.result(ByteArrayInputStream(model.toByteArray()))
            }
        }

        fun createServer(): Server {
            val httpsConfig = HttpConfiguration()
            val http1 = HttpConnectionFactory(httpsConfig)
            val http2 = HTTP2CServerConnectionFactory(httpsConfig)

            return Server().apply {

                //HTTP/2 Connector
                addConnector(ServerConnector(server, http1, http2).apply {
                    port = 80
                })
            }
        }
    }
}
