package com.uncooleben.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import com.uncooleben.model.Message;

public class MessageDAOTest {
	
	private MessageDAO messageDAO;
	
	
	@Before
	public void init() {
		this.messageDAO = new MessageDAO();
	}

	@Test
	public void store_and_query_one_message() {
		Date date = new Date(System.currentTimeMillis());
		Message message = new Message("james", "im bond", date);
		this.messageDAO.storeMessage(message);
		Message stored_message = this.messageDAO.queryMessage(message.get_uuid()).get(0);
		assertEquals(message.get_uuid(), stored_message.get_uuid());
		assertEquals(message.get_username(), stored_message.get_username());
		assertEquals(message.get_content(), stored_message.get_content());
		assertEquals(message.get_time().toString(), stored_message.get_time().toString());
		
	}
	
	@Test
	public void store_and_query_five_message() {
		List<Message> messages = new ArrayList<Message>();
		for (int i=0; i<5; ++i) {
			Date date = new Date(System.currentTimeMillis()+i*1000);
			Message message = new Message("james"+i, "im bond"+i, date);
			messages.add(0, message);
			this.messageDAO.storeMessage(message);
		}
		List<Message> stored_messages = this.messageDAO.queryMessage(5, System.currentTimeMillis());
		for (int i=0; i<5; ++i) {
			Message message = messages.get(i);
			Message stored_message = stored_messages.get(i);
			System.out.println(stored_message.get_username());
			assertEquals(message.get_uuid(), stored_message.get_uuid());
			assertEquals(message.get_username(), stored_message.get_username());
			assertEquals(message.get_content(), stored_message.get_content());
			assertEquals(message.get_time().toString(), stored_message.get_time().toString());
		}
		
	}
	
	@After
	public void finalize() {
		this.messageDAO.finalize();
	}

}
