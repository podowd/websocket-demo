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

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {

        return webSocketSession.send(
                Flux.interval(Duration.ofSeconds(4))
                    .map(sequence -> webSocketSession.textMessage("Event - " + sequence.longValue())))
                .and(webSocketSession.receive()
                    .map(WebSocketMessage::getPayloadAsText)
                    .log());
    }
}