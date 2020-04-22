package com.example.services.client;

import com.example.protobuf.GreetingProtos.Greeting;
import com.example.services.service.api.GreetingsService;
import com.example.services.util.CommandLineUtil;
import com.example.services.util.MicroservicesFactory;
import io.scalecube.net.Address;
import io.scalecube.services.Microservices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.example.services.util.ServiceMessageUtil.toServiceMessage;

public class ExampleClientSendDataUsingApi {
    public static final Logger LOGGER = LoggerFactory.getLogger(ExampleClientReceiveStream.class);

    public static void main(String[] args) {
        Address discoveryAddress = CommandLineUtil.getDiscoveryAddress(args);

        LOGGER.info("Starting Client");

        Microservices client = new MicroservicesFactory().createMember(discoveryAddress);

        LOGGER.info("Calling the service");
        GreetingsService service = client.call().api(GreetingsService.class);
        Greeting greeting = Greeting.newBuilder().setMessage("The client says hello.").build();
        service.receiveGreeting(toServiceMessage(greeting)).block();

        client.shutdown().block();
    }

}
