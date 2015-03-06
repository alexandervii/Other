package com.alex.tur.bean;

import java.util.Date;

public class ChatMessage {

	public String name;
	public String msg;
	public Type type;
	public Date date;
	
	public ChatMessage() {
		super();
	}

	public ChatMessage(String msg, Type type, Date date) {
		super();
		this.msg = msg;
		this.type = type;
		this.date = date;
	}



	public enum Type {
		INCOMING,OUTCOMING;
	}
}
