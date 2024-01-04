package com.messager.chatbox.Model;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
	private MessageType messageType;

	private String content;

	private String sender;

	private Date dateSend;
}
