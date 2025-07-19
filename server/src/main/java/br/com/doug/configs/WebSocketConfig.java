package br.com.doug.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String[] WS_ENDPOINTS = {
            "/ant-route-updates",
    };

    private final String[] ALLOWED_ORIGINS = {
            "http://localhost:8080",
            "ws://localhost:8080",
            "http://localhost:4200"
    };

    private final String[] DESTINATION_PREFIXES = {
            "/topic",
    };

    private final String[] PREFIXES = {
            "/app",
    };

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(DESTINATION_PREFIXES);
        registry.setApplicationDestinationPrefixes(PREFIXES);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint(WS_ENDPOINTS)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

}
