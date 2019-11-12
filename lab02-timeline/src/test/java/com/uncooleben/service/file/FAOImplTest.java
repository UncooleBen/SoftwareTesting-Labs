package com.uncooleben.service.file;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.multipart.MultipartFile;

import com.uncooleben.model.Message;

class FAOImplTest {

	private File mockedDir = mock(File.class);
	private File mockedFile = mock(File.class);
	private TestableFAOImpl fao = new TestableFAOImpl();
	private long milliTime = System.currentTimeMillis();

	class TestableFAOImpl extends FAOImpl {

		@Override
		protected File createFile(String path) {
			return mockedDir;
		}

		@Override
		protected File createFile(File dir, String filename) {
			return mockedFile;
		}
	}

	@Test
	public void test_io_exception_store_image() {
		Date date = new Date(milliTime);
		Message message = new Message("james", "im bond", date);
		IOException test_ioe = new IOException();
		MultipartFile image = mock(MultipartFile.class);
		// Mocking behaviours
		when(mockedDir.exists()).thenReturn(true);
		when(mockedFile.exists()).thenReturn(false);
		try {
			when(mockedFile.createNewFile()).thenThrow(test_ioe);
		} catch (IOException ioe) {
			// Won't throw any exception in this try/catch block
			ioe.printStackTrace(System.err);
		}
		// Run the method
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		this.fao.storeImage(message, image);
		assertTrue(errContent.toString().contains("java.io.IOException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	static Stream<Arguments> booleanAndBooleanProvider() {
		return Stream.of(Arguments.of(true, true), Arguments.of(true, false), Arguments.of(false, true),
				Arguments.of(false, false));
	}

	@ParameterizedTest
	@MethodSource("booleanAndBooleanProvider")
	public void test_store_image(boolean value1, boolean value2) {
		Date date = new Date(milliTime);
		Message message = new Message("james", "im bond", date);
		MultipartFile image = mock(MultipartFile.class);
		// Mocking behaviours
		when(mockedDir.exists()).thenReturn(value1);
		when(mockedFile.exists()).thenReturn(value2);
		boolean result = this.fao.storeImage(message, image);
		assertTrue(result);
	}

}
