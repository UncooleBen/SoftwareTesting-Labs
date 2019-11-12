package com.uncooleben.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import com.uncooleben.service.dao.MessageDAO;

class NewMessageControllerTest {

	private MessageDAO messageDAO = mock(MessageDAO.class);
	private NewMessageController newmessageController = new NewMessageController();

	@Test
	void test_onSubmit() {
		newmessageController.messageDAO = messageDAO;

		ModelAndView result = newmessageController.onSubmit("彭钧涛", "彭哥牛逼", null);
		verify(messageDAO).storeMessage(any(Message.class));

		assertEquals(result.getViewName(), "redirect:/");
	}

}
