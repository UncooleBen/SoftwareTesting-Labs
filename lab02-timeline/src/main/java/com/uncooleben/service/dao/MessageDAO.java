package com.uncooleben.service.dao;

import java.util.List;
import java.util.UUID;

import com.uncooleben.model.Message;

public interface MessageDAO {

	boolean storeMessage(Message message, boolean withImage);

	List<Message> queryMessageByUUID(UUID uuid);

	List<Message> queryMessage(int size, long millisec);

	int queryUpdates(long millisec);

	boolean clearTable();

}
