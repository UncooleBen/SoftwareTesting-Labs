package com.uncooleben.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;

import com.uncooleben.service.file.FAO;

class ImageControllerTest {

	private FAO fao = mock(FAO.class);
	private ImageController imageController = new ImageController();

	@Test
	void test_get_requested_image() throws IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		imageController.fao = fao;
		byte[] result = new byte[] { 'r', 'e', 'q', 'u', 'e', 's', 't' };
		when(fao.convertToByteArray("request")).thenReturn(result);
		StringBuffer urlbuffer = new StringBuffer("http://localhost:8080/jqueryLearn/resources/request");
		when(request.getRequestURL()).thenReturn(urlbuffer);

		assertEquals(imageController.getRequestedImage(request), result);
	}

}
