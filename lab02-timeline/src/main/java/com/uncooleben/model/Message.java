package com.uncooleben.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Message {

	private String _username;
	private String _content;
	private Date _time;
	private UUID _uuid;
	private String _ago;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Message(String username, String content, Date time) {
		this._content = content;
		this._username = username;
		this._time = time;
		this._uuid = UUID.randomUUID();
		this._ago = "Error";
	}

	public Message(UUID uuid, String username, String content, Date time) {
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

	public void set_ago(long millisec) {
		long duration = millisec - this._time.getTime();
		duration /= 1000;
		if (duration < 1) {
			this._ago = "Just Now";
		} else if (duration < 60) {
			this._ago = duration + " Second(s) Ago";
		} else if (duration < 3600) {
			this._ago = duration / 60 + " Minute(s) Ago";
		} else if (duration < 3600 * 24) {
			this._ago = duration / 3600 + " Hours(s) Ago";
		} else {
			this._ago = format.format(this._time);
		}
	}

	public String get_ago() {
		return this._ago;
	}

	@Override
	public String toString() {
		return "Message { username : " + this._username + " content : " + this._content + " date : " + this._time
				+ " }";
	}
}
