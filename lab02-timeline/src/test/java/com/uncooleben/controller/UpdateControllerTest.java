package com.uncooleben.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.uncooleben.service.dao.MessageDAO;

class UpdateControllerTest {

	private MessageDAO messageDAO = mock(MessageDAO.class);
	private UpdateController updateController = new UpdateController();

	@Test
	void test_update() {
		updateController.messageDAO = messageDAO;
		when(messageDAO.queryUpdates(1)).thenReturn(2);

		String result = 2 + " Update(s)";
		assertEquals(updateController.update("1"), result);
	}

}
