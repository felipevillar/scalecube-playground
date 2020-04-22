package com.example.services.util;

import com.example.services.Constants;
import io.scalecube.cluster.transport.api.TransportConfig;
import io.scalecube.net.Address;
import io.scalecube.services.Microservices;
import io.scalecube.services.discovery.ScalecubeServiceDiscovery;
import io.scalecube.services.exceptions.DefaultErrorMapper;
import io.scalecube.services.transport.api.HeadersCodec;
import io.scalecube.services.transport.rsocket.RSocketServiceTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.UnaryOperator;

public class MicroservicesFactory {

    public static final Logger LOGGER = LoggerFactory.getLogger(LifecycleUtil.class);

    private final RSocketServiceTransport transport;

    public MicroservicesFactory() {
        transport = new RSocketServiceTransport()
            .headersCodec(HeadersCodec.getInstance(Constants.CONTENT_TYPE));
    }

    public Microservices createSeedNode(Address discoveryAddress) {
        UnaryOperator<TransportConfig> transportConfigFunction = transportConfig -> {
            if (discoveryAddress != null) {
                return transportConfig
                    .host(discoveryAddress.host())
                    .port(discoveryAddress.port());
            } else {
                return transportConfig;
            }
        };

        Microservices ms =
            Microservices.builder()
                .discovery(
                    endpoint ->
                        new ScalecubeServiceDiscovery(endpoint)
                            .options(clusterConfig -> clusterConfig
                                .transport(transportConfigFunction))
                )
                .transport(() -> transport)
                .startAwait();

        LOGGER.info("Seed discovery address: {}", ms.discovery().address());
        LOGGER.info("Seed service address: {} ", ms.serviceAddress());
        return ms;
    }

    public Microservices createMember(Address seedDiscoveryAddress) {
        return createMember(seedDiscoveryAddress, UnaryOperator.identity());
    }

    public Microservices createMember(Address seedDiscoveryAddress,
                                      UnaryOperator<Microservices.Builder> builderConfig) {
        Microservices.Builder builder = Microservices.builder()
            .discovery(
                endpoint ->
                    new ScalecubeServiceDiscovery(endpoint)
                        .membership(cfg -> cfg.seedMembers(seedDiscoveryAddress)))
            .transport(() -> transport)
            .defaultErrorMapper(throwable -> {
                LOGGER.error(throwable.getMessage(), throwable);
                return DefaultErrorMapper.INSTANCE.toMessage(throwable);
            });

        Microservices ms = builderConfig.apply(builder)
            .startAwait();

        LOGGER.info("Cluster Member discovery address: {}", ms.discovery().address());
        LOGGER.info("Cluster Member service address: {}", ms.serviceAddress());
        return ms;
    }

}
