package com.example.services.client;

import com.example.protobuf.GreetingProtos.Greeting;
import com.example.services.service.api.GreetingsService;
import com.example.services.util.CommandLineUtil;
import com.example.services.util.MicroservicesFactory;
import io.scalecube.net.Address;
import io.scalecube.services.Microservices;
import io.scalecube.services.api.ServiceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Client that uses the Service interface.
 */
public class ExampleClientSimpleSend {

    public static final Logger LOGGER = LoggerFactory.getLogger(ExampleClientSimpleSend.class);

    public static void main(String[] args) {
        Address discoveryAddress = CommandLineUtil.getDiscoveryAddress(args);

        LOGGER.info("Starting Client");
        Microservices client = new MicroservicesFactory().createMember(discoveryAddress);

        LOGGER.info("Calling the service");
        GreetingsService service = client.call().api(GreetingsService.class);
        ServiceMessage greeting = service.sayHello("Bob").block();

        LOGGER.info(requireNonNull(greeting).<Greeting>data().getMessage());

        client.shutdown().block();
    }

}
