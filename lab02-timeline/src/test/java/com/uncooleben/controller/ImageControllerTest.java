package com.uncooleben.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;

class ImageControllerTest {

	@Test
	void test_testphoto() throws IOException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getRequestURL()).thenReturn(new StringBuffer("dsasda"));

		ImageController imagecontroller = new ImageController();
		byte[] result = imagecontroller.testphoto(request);
		FileInputStream fis = mock(FileInputStream.class);

	}

}
