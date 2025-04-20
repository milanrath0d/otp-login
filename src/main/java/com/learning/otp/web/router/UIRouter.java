package com.learning.otp.web.router;

import com.learning.otp.web.handler.UIHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Router configuration for UI pages
 * @author Milan Rathod
 */
@Configuration
@AllArgsConstructor
public class UIRouter {

    private final UIHandler handler;

    @Bean
    public RouterFunction<ServerResponse> uiRoutes() {
        return RouterFunctions.route()
                .GET("/", handler::index)
                .GET("/verify", handler::verify)
                .build();
    }
} 