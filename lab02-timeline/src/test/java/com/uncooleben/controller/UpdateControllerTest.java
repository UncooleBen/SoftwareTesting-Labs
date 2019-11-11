package com.uncooleben.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.uncooleben.dao.MessageDAO;
import com.uncooleben.dao.MessageDBDAO;

class UpdateControllerTest {

	@Mock
	private MessageDAO messagedao;

	@InjectMocks
	private UpdateController updatecontroller = new UpdateController();

	@Test
	void test_update() {
		MessageDBDAO messageDBDAO = mock(MessageDBDAO.class);
		MessageDAO messageDAO = mock(MessageDAO.class);
		when(messageDAO.queryUpdates(1)).thenReturn(2);

		UpdateController updatecontroller = new UpdateController();

		String result = 2 + " Update(s)";
		assertEquals(updatecontroller.update("1"), result);
	}

}
