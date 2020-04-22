package com.example.gateway;

import com.example.services.util.CommandLineUtil;
import com.example.services.util.LifecycleUtil;
import com.example.services.util.MicroservicesFactory;
import io.scalecube.net.Address;
import io.scalecube.services.Microservices;
import io.scalecube.services.gateway.http.HttpGateway;
import io.scalecube.services.gateway.rsocket.RSocketGateway;
import io.scalecube.services.gateway.ws.WebsocketGateway;

public class ApiGateway {
    public static void main(String[] args) throws InterruptedException {
        Address discoveryAddress = CommandLineUtil.getDiscoveryAddress(args);

        Microservices ms = new MicroservicesFactory().createMember(discoveryAddress,
            builder -> builder
                .gateway(options -> new WebsocketGateway(options.id("ws").port(8080)))
                .gateway(options -> new HttpGateway(options.id("http").port(7070)))
                .gateway(options -> new RSocketGateway(options.id("rsws").port(9090)))
        );

        LifecycleUtil.shutdownOnInterrupt(ms);
        Thread.currentThread().join();
    }
}
