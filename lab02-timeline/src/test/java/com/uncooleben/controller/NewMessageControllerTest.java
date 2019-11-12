package com.uncooleben.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.uncooleben.model.Message;
import com.uncooleben.service.dao.MessageDAO;
import com.uncooleben.service.file.FAO;

class NewMessageControllerTest {

	private MessageDAO messageDAO = mock(MessageDAO.class);
	private NewMessageController newmessageController = new NewMessageController();
	private MultipartFile multipartFile = mock(MultipartFile.class);
	private FAO fao = mock(FAO.class);

	@Test
	void test_onSubmit_true() {
		newmessageController.messageDAO = messageDAO;
		newmessageController.fao = fao;
		ModelAndView result = newmessageController.onSubmit("彭钧涛", "彭哥牛逼", multipartFile);
		verify(messageDAO).storeMessage(any(Message.class), eq(true));
		verify(fao).storeImage(any(Message.class), eq(multipartFile));
		assertEquals(result.getViewName(), "redirect:/");
	}

	@Test
	void test_onSubmit_false() {
		newmessageController.messageDAO = messageDAO;
		newmessageController.fao = fao;
		when(multipartFile.isEmpty()).thenReturn(true);
		ModelAndView result = newmessageController.onSubmit("彭钧涛", "彭哥牛逼", multipartFile);
		verify(messageDAO).storeMessage(any(Message.class), eq(false));
		assertEquals(result.getViewName(), "redirect:/");
	}

}
