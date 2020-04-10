package com.podowd.websockets.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class DemoWebSocket implements WebSocketHandler {

    private int counter = 0;

    private Flux<String> eventStream = Flux.generate(sink ->
    {
        sink.next("Event - " + counter++);
    });

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {

        return webSocketSession.send(
                Flux.interval(Duration.ofSeconds(4))
                    .zipWith(eventStream, (time, event) -> event)
                    .map(webSocketSession::textMessage))
                .and(webSocketSession.receive()
                    .map(WebSocketMessage::getPayloadAsText)
                    .log());
    }
}