package cl.svasquezm.android_http2_protobuf

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    val TAG = "AH2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        request()
    }

    fun request(){

        GlobalScope.run {
            launch {
                val protocolList = mutableListOf<Protocol>()
                //protocolList.add(Protocol.HTTP_1_1)
                protocolList.add(Protocol.H2_PRIOR_KNOWLEDGE)

                val client = OkHttpClient.Builder().apply {
                    protocols(protocolList)
                }.build()

                val request = Request.Builder()
                    .url("http://192.168.1.82")
                    .build()

                val response = client.newCall(request).execute()
                Log.i(TAG, "Server response: ")
                response.headers.forEach {
                    Log.i(TAG, " ${it.first}: ${it.second} ")
                }

                Log.i(TAG, "Protocol: ${response.protocol.name}")


                val user = Models.User.parseFrom(response.body!!.bytes())
                Log.i(TAG, "User.name = ${user.name}")
                Log.i(TAG, "User.email = ${user.email}")
                Log.i(TAG, "User.type = ${user.type}")
                user.petsList.forEachIndexed { i, pet ->
                    Log.i(TAG, "User.pet$i = (${pet.type}, name: ${pet.name})")
                }
            }
        }
    }
}
