package com.example.services.service;

import com.example.protobuf.GreetingProtos.Greeting;
import com.example.services.service.api.GreetingsService;
import com.google.protobuf.TextFormat;
import io.scalecube.services.api.ServiceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static com.example.services.util.ServiceMessageUtil.toServiceMessage;

public class GreetingsServiceImpl implements GreetingsService {

  public static final Logger LOGGER = LoggerFactory.getLogger(GreetingsServiceImpl.class);

  @Override
  public Mono<ServiceMessage> sayHello(String name) {
    LOGGER.info("Got a request for a greeting: {}", name);
    Greeting greeting = createGreeting(name);
    return Mono.just(toServiceMessage(greeting));
  }

  @Override
  public Mono<Void> receiveGreeting(ServiceMessage message) {
    Greeting greeting = message.data();
    LOGGER.info("Received a greeting from client: {}", TextFormat.shortDebugString(greeting));
    return Mono.empty();
  }

  @Override
  public Flux<ServiceMessage> subscribeToGreetings(String name) {
    LOGGER.info("Received a request for multiple greetings from {}", name);
    return Flux.interval(Duration.ofMillis(500))
        .take(20)
        .map(l -> toServiceMessage(createGreeting(name)));
  }

  private Greeting createGreeting(String name) {
    return Greeting.newBuilder()
        .setMessage("Hello " + name + "!")
        .build();
  }

}
