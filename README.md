# Android HTTP2 + Protocol Buffer test
This project contains two modules:
1. A `server` module which starts a Javalin server using http2 clear text protocol (h2c) in port `80`
2. An Android application module `app` which contains an application who requests server running in `server` module

## Project structure
### Server Model
The proto file contains next structure
```
syntax = "proto3";
package cl.svasquezm.proto;

message Pet {
   string type = 1;
   string name = 2;
}

message User {
   string name = 1;
   string email = 2;

   enum Type {
      WEB = 0;
      MOBILE = 1;
      ADMIN = 2;
   }

   Type type = 3;

   repeated Pet pets = 4;
}
```

When you compile it using:
```
protoc --java_out=proto/src/main/java/ proto/models.proto
```

You generate server Java models located in proto/src/main/java/

### Android Model
Android models are generated via `protobuf` gradle task. `/.gradlew build` will do this job.


# Start the project
To make this project work, you must follow next steps.

### Start server

```
./gradlew server:run
```

You can test is using
```
curl --http2-prior-knowledge -XGET 127.0.0.1 -I

```

You should get a response like:
```
HTTP/2 200
server: Javalin
date: Mon, 21 Oct 2019 12:57:39 GMT
content-type: application/protobuf
```

### Run client
Run project with android studio. Watch the log and it should print:
```
I/AH2     ( 3029): Server response:
I/AH2     ( 3029):  server: Javalin
I/AH2     ( 3029):  date: Mon, 21 Oct 2019 13:16:04 GMT
I/AH2     ( 3029):  content-type: application/protobuf
I/AH2     ( 3029): Protocol: H2_PRIOR_KNOWLEDGE 4
I/AH2     ( 3029): User.name = Sebastian
I/AH2     ( 3029): User.email = seba.vasquez.m@gmail.com
I/AH2     ( 3029): User.type = MOBILE
I/AH2     ( 3029): User.pet0 = (dog, name: luka)
I/AH2     ( 3029): User.pet1 = (cat, name: ema)
I/AH2     ( 3029): User.pet2 = (rabbit, name: gaspar)
```