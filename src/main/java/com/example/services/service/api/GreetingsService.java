package com.example.services.service.api;

import com.example.protobuf.GreetingProtos.Greeting;
import io.scalecube.services.annotations.RequestType;
import io.scalecube.services.annotations.ResponseType;
import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;
import io.scalecube.services.api.ServiceMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("com.example.service.GreetingsService")
public interface GreetingsService {

  @ServiceMethod("sayHello")
  @ResponseType(Greeting.class)
  Mono<ServiceMessage> sayHello(String name);

  @ServiceMethod("receiveGreeting")
  @RequestType(Greeting.class)
  Mono<Void> receiveGreeting(ServiceMessage message);

  @ServiceMethod("subscribeToGreetings")
  @ResponseType(Greeting.class)
  Flux<ServiceMessage> subscribeToGreetings(String name);

}
