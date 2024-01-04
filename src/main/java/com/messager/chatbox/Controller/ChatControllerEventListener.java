package com.messager.chatbox.Controller;

import com.messager.chatbox.Model.ChatMessage;
import com.messager.chatbox.Model.MessageType;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class ChatControllerEventListener {
	@Autowired
	private SimpMessageSendingOperations messageSendingOperations;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent connectedEvent){
		log.info("Received a new web socket connection");
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent){
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());

		String username = (String) headerAccessor.getSessionAttributes().get("username");
		if (username != null){
			log.info("User disconnect : " + username);

			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setMessageType(MessageType.LEAVE);
			chatMessage.setSender(username);

			messageSendingOperations.convertAndSend("/topic/public", chatMessage);
		}
	}
}
