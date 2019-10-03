package com.uncooleben.model;

import java.util.Date;
import java.util.UUID;

public class Message {
	
	private String _username;
	private String _content;
	private Date _time;
	private UUID _uuid;
	
	public Message (String username, String content, Date time) {
		this._content = content;
		this._username = username;
		this._time = time;
		this._uuid = UUID.randomUUID();
	}
	
	public Message (UUID uuid, String username, String content, Date time) {
		this._content = content;
		this._username = username;
		this._time = time;
		this._uuid = uuid;
	}

	public String get_username() {
		return _username;
	}

	public String get_content() {
		return _content;
	}

	public Date get_time() {
		return _time;
	}
	
	public UUID get_uuid() {
		return this._uuid;
	}
	

}
