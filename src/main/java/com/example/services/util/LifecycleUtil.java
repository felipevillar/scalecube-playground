package com.example.services.util;

import io.scalecube.services.Microservices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;

public final class LifecycleUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(LifecycleUtil.class);

    public static void shutdownOnInterrupt(Microservices... microservices) {
        Signal.handle(new Signal("INT"), signal -> {
            for (Microservices ms : microservices) {
                ms.shutdown().block();
            }
            LOGGER.info("Shutdown complete");
            System.exit(0);
        });
    }

}
