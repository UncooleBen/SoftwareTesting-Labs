package com.uncooleben.dao;

import java.util.List;
import java.util.UUID;

import com.uncooleben.model.Message;

public interface MessageDAO {

	public boolean storeMessage(Message message);

	public List<Message> queryMessageByUUID(UUID uuid);

	public List<Message> queryMessage(int size, long millisec);

	public int queryUpdates(long millisec);

	public boolean clearTable();

}
