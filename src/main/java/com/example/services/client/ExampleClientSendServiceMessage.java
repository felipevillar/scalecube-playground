package com.example.services.client;

import com.example.protobuf.GreetingProtos.Greeting;
import com.example.services.util.CommandLineUtil;
import com.example.services.util.MicroservicesFactory;
import io.scalecube.net.Address;
import io.scalecube.services.Microservices;
import io.scalecube.services.ServiceCall;
import io.scalecube.services.api.ServiceMessage;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import static com.example.services.util.ServiceMessageUtil.toServiceMessage;
import static java.util.Objects.requireNonNull;

/**
 * Client that sends a ServiceMessage - no dependency on Service interface
 */
public class ExampleClientSendServiceMessage {

    public static final Logger LOGGER = LoggerFactory.getLogger(ExampleClientSendServiceMessage.class);

    static final String SERVICE_QUALIFIER = "/com.example.service.GreetingsService/sayHello";

    public static void main(String[] args) {
        Address discoveryAddress = CommandLineUtil.getDiscoveryAddress(args);

        Microservices client = new MicroservicesFactory().createMember(discoveryAddress);

        ServiceCall service = client.call();

        // Create a ServiceMessage request with service qualifier and data
        ServiceMessage request = toServiceMessage(SERVICE_QUALIFIER, "Jack");

        // Execute the Greeting Service to emit a single Greeting response
        Publisher<ServiceMessage> publisher = service.requestOne(request, Greeting.class);

        // Convert the Publisher using the Mono API which ensures it will emit 0 or 1 item.
        ServiceMessage serviceMessage = requireNonNull(Mono.from(publisher).block());
        LOGGER.info(serviceMessage.<Greeting>data().getMessage());

        client.shutdown().block();
    }
}
