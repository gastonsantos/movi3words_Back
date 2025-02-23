package com.api.movi3words.movie3words.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
/*
@Configuration
@EnableWebSocketMessageBroker
public class webSocketConfig implements WebSocketMessageBrokerConfigurer{
	

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		registry.enableStompBrokerRelay("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints( StompEndpointRegistry registry) {
		registry.addEndpoint("/chat-socket")
		.setAllowedOrigins("http://localhost:3000")
		.withSockJS();
	}
	
	
}
*/