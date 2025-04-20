package com.learning.otp.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Handler for UI pages
 * @author Milan Rathod
 */
@Component
@Slf4j
public class UIHandler {

    /**
     * Render the index/home page
     */
    public Mono<ServerResponse> index(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .render("index");
    }

    /**
     * Render the OTP verification page
     */
    public Mono<ServerResponse> verify(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .render("verify");
    }
} 