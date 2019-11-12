package com.uncooleben.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;

class ImageControllerTest {

	@Test
	void test_testphoto() throws IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		StringBuffer urlbuffer = new StringBuffer("http://localhost:8080/jqueryLearn/resources/request.jsp");
		when(request.getRequestURL()).thenReturn(urlbuffer);

		ImageController imageController = new ImageController();
		imageController.testphoto(request);
	}

}
