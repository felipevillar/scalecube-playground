package com.example.services.server;

import com.example.services.service.GreetingsServiceImpl;
import com.example.services.util.CommandLineUtil;
import com.example.services.util.LifecycleUtil;
import com.example.services.util.MicroservicesFactory;
import io.scalecube.net.Address;
import io.scalecube.services.Microservices;

/**
 * Contains both a seed node and the member node with the Service
 */
public class ServerClusterMain {

    public static void main(String[] args) throws InterruptedException {
        Address discoveryAddress = CommandLineUtil.getDiscoveryAddress(args);

        MicroservicesFactory microservicesFactory = new MicroservicesFactory();
        Microservices seed = microservicesFactory.createSeedNode(discoveryAddress);

        // Member node which joins the cluster hosting the Greeting Service
        Microservices ms = microservicesFactory.createMember(
            seed.discovery().address(),
            builder -> builder.services(new GreetingsServiceImpl()));

        LifecycleUtil.shutdownOnInterrupt(ms, seed);
        Thread.currentThread().join();
    }
}
