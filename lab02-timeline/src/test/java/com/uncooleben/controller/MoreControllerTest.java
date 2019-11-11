package com.uncooleben.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.uncooleben.dao.MessageDAO;
import com.uncooleben.dao.MessageDBDAO;
import com.uncooleben.model.Message;

class MoreControllerTest {

	@Test
	void test_more() {
		MessageDBDAO messageDBDAO=mock(MessageDBDAO.class);
		MessageDAO messageDAO=mock(MessageDAO.class);
		PowerMockito.whenNew(MessageDBDAO.class).withArguments().thenReturn(messageDBDAO);
		
		
		List<Message> messages=new ArrayList<Message>();
		messages.add(new Message("彭钧涛", "彭哥牛逼", new Date(1000)));
		when(messageDAO.queryMessage(4,2)).thenReturn(messages);
		
		MoreController morecontroller=new MoreController();
		
		String result="";
		assertEquals(morecontroller.more("1","2"),result);
	}

}
