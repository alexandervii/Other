package com.alex.tur.bean;

import java.util.Date;

public class ChatMessage {

	public String name;
	public String msg;
	public Type type;
	public Date date;
	
	public enum Type {
		INCOMING,OUTCOMING;
	}
}
