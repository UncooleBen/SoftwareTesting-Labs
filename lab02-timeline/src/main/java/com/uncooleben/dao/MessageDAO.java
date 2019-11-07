package com.uncooleben.dao;

import java.util.List;
import java.util.UUID;

import com.uncooleben.model.Message;
import org.springframework.web.multipart.MultipartFile;

public interface MessageDAO {

	boolean storeMessage(Message message);

	boolean storeMessage(Message message, MultipartFile image);

	List<Message> queryMessageByUUID(UUID uuid);

	List<Message> queryMessage(int size, long millisec);

	int queryUpdates(long millisec);

	boolean clearTable();

}
