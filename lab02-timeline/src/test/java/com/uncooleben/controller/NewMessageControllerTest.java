package com.uncooleben.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.uncooleben.dao.MessageDAO;
import com.uncooleben.dao.MessageDBDAO;
import com.uncooleben.model.Message;

class NewMessageControllerTest {

	@Test
	void test_onSubmit() 
	{
		MessageDBDAO messageDBDAO=mock(MessageDBDAO.class);
		MessageDAO messageDAO=mock(MessageDAO.class);
		PowerMockito.whenNew(MessageDBDAO.class).withArguments().thenReturn(messageDBDAO);
		Message newMessage;
		
		NewMessageController newmessagecontroller=new NewMessageController();
		ModelAndView result=newmessagecontroller.onSubmit("彭钧涛", "彭哥牛逼", null);
		
		
		verify(messageDAO).storeMessage(any(Message));
		
		assertEquals(result.getViewName(),"redirect:/");
	}

}
