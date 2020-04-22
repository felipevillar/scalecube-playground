package com.example.services.client;

import com.example.protobuf.GreetingProtos.Greeting;
import com.example.services.service.api.GreetingsService;
import com.example.services.util.CommandLineUtil;
import com.example.services.util.MicroservicesFactory;
import com.google.protobuf.TextFormat;
import io.scalecube.net.Address;
import io.scalecube.services.Microservices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleClientReceiveStream {

    public static final Logger LOGGER = LoggerFactory.getLogger(ExampleClientReceiveStream.class);

    public static void main(String[] args) {
        Address discoveryAddress = CommandLineUtil.getDiscoveryAddress(args);

        LOGGER.info("Starting Client");
        Microservices client = new MicroservicesFactory().createMember(discoveryAddress);

        LOGGER.info("Calling the service");
        GreetingsService service = client.call().api(GreetingsService.class);
        service.subscribeToGreetings("Jack")
            .doOnNext(m -> {
                Greeting greeting = m.data();
                LOGGER.info((TextFormat.shortDebugString(greeting)));
                LOGGER.debug("Bytes: " + greeting.toByteString());
            })
            .blockLast();

        client.shutdown().block();
    }

}
