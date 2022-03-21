# scalecube-playground

This is a "hello world"-style project in order to learn about the ScaleCube Microservices project (http://scalecube.io/).

This project starts a seed node and a member cluster node inside the same JVM (see `com.example.services.server.ServerClusterMain`).

An example API Gateway is here: `com.example.gateway.ApiGateway`

We have written a very rudimentary ProtobufCodec (ScaleCube comes with Protostuff and Jackson codecs).  See `com.example.codec.protobuf.ProtobufCodec`

The proto files are in `src/main/protobuf`

### Build Instructions
`mvn clean install`

### How to run the server cluster

Note that we provide the discovery address with a well-defined port by using the `-a` program argument.

```
java -cp "target/scalecube-playground-1.0-SNAPSHOT.jar;target/lib/*" com.example.services.server.ServerClusterMain -a <your-ip-address>:52000
```

### How to run the example clients

```
java -cp "target/scalecube-playground-1.0-SNAPSHOT.jar;target/lib/*" com.example.services.client.ExampleClientReceiveStream -a <your-ip-address>:52000

# Can also do similar to above for the other examples in com.example.services.client
```

That's it!
