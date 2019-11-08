package com.uncooleben.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import com.uncooleben.model.Message;

public class MessageMySQLDAOTest {

	private Connection connection = mock(Connection.class);
	private PreparedStatement pstmt = mock(PreparedStatement.class);
	private MessageMySQLDAO messageDAO;
	private long milliTime;
	private SimpleDateFormat format;

	class TestableMessageMySQLDAO extends MessageMySQLDAO {

		@Override
		protected void loadDriver() {

		}

		@Override
		protected Connection getConnection() throws SQLException {
			return connection;
		}
	}

	@BeforeEach
	public void init() {
		this.messageDAO = new TestableMessageMySQLDAO();
		this.milliTime = System.currentTimeMillis();
		this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Test
	public void test_sql_exception_store_message() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		SQLException test_sqle = new SQLException();
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sqle);
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}

		this.messageDAO.storeMessage(null);
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test_store_one_message() {
		Date date = new Date(milliTime);
		Message message = new Message("james", "im bond", date);
		// Creating argument captors for verification
		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		// Mocking behaviours
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
		}
		// Run the method
		boolean succeeded = this.messageDAO.storeMessage(message);
		// Assertions
		assertTrue(succeeded);
		try {
			verify(pstmt, times(4)).setString(anyInt(), stringArgumentCaptor.capture());
			assertEquals("james", stringArgumentCaptor.getAllValues().get(1));
			assertEquals("im bond", stringArgumentCaptor.getAllValues().get(2));
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
			fail("Encountered SQLException when verifying arguments of pstmt, see below stack trace for detail.");
		}

	}

	@Test
	public void test_sql_exception_store_message_with_image() {
		Date date = new Date(milliTime);
		Message message = new Message("james", "im bond", date);
		SQLException test_sqle = new SQLException();
		// Spy the createFile method the shield the DAO from file system
		this.messageDAO = Mockito.spy(new TestableMessageMySQLDAO());
		// Create mocks
		File dir = mock(File.class);
		File f = mock(File.class);
		MultipartFile image = mock(MultipartFile.class);
		// Mocking behaviours
		when(dir.exists()).thenReturn(true);
		when(f.exists()).thenReturn(true);
		doReturn(dir).when(this.messageDAO).createFile(anyString());
		doReturn(f).when(this.messageDAO).createFile(any(File.class), anyString());
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sqle);
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}
		// Run the method
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		this.messageDAO.storeMessage(message, image);
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test_io_exception_store_message_with_image() {
		Date date = new Date(milliTime);
		Message message = new Message("james", "im bond", date);
		IOException test_ioe = new IOException();
		// Spy the createFile method the shield the DAO from file system
		this.messageDAO = Mockito.spy(new TestableMessageMySQLDAO());
		// Create mocks
		File dir = mock(File.class);
		File f = mock(File.class);
		MultipartFile image = mock(MultipartFile.class);
		// Mocking behaviours
		when(dir.exists()).thenReturn(true);
		when(f.exists()).thenReturn(false);
		doReturn(dir).when(this.messageDAO).createFile(anyString());
		doReturn(f).when(this.messageDAO).createFile(any(File.class), anyString());
		try {
			when(f.createNewFile()).thenThrow(test_ioe);
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
		this.messageDAO.storeMessage(message, image);
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
	public void test_store_message_with_image(boolean value1, boolean value2) {
		Date date = new Date(milliTime);
		Message message = new Message("james", "im bond", date);
		// Spy the createFile method the shield the DAO from file system
		this.messageDAO = Mockito.spy(new TestableMessageMySQLDAO());
		// Create mocks
		File dir = mock(File.class);
		File f = mock(File.class);
		MultipartFile image = mock(MultipartFile.class);
		// Mocking behaviours
		when(dir.exists()).thenReturn(value1);
		when(f.exists()).thenReturn(value2);
		doReturn(dir).when(this.messageDAO).createFile(anyString());
		doReturn(f).when(this.messageDAO).createFile(any(File.class), anyString());
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
		}
		// Run the method
		boolean succeeded = this.messageDAO.storeMessage(message, image);
		// Assertions
		assertTrue(succeeded);
		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		try {
			verify(pstmt, times(5)).setString(anyInt(), stringArgumentCaptor.capture());
			assertEquals("james", stringArgumentCaptor.getAllValues().get(1));
			assertEquals("im bond", stringArgumentCaptor.getAllValues().get(2));
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
			fail("Encountered SQLException when verifying arguments of pstmt, see below stack trace for detail.");
		}

	}

	@Test
	public void test_create_file() {
		assertTrue(this.messageDAO.createFile("") instanceof File);
		assertTrue(this.messageDAO.createFile(new File(""), "") instanceof File);
	}

	@Test
	public void test_sql_exception_query_by_uuid() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		SQLException test_sqle = new SQLException();
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sqle);
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}

		this.messageDAO.queryMessageByUUID(null);
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test_parse_exception_query_by_uuid() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		UUID uuid = UUID.randomUUID();
		String uuid_str = uuid.toString();
		String username = "james bond";
		String content = "hello";
		String time = "MALFORMED DATE STRING";
		ResultSet rs = mock(ResultSet.class);
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
			when(pstmt.executeQuery()).thenReturn(rs);
			when(rs.next()).thenReturn(true, false); // First call returns true, second call returns false
			when(rs.getString("uuid")).thenReturn(uuid_str);
			when(rs.getString("username")).thenReturn(username);
			when(rs.getString("content")).thenReturn(content);
			when(rs.getString("time")).thenReturn(time);
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}
		this.messageDAO.queryMessageByUUID(uuid);
		assertTrue(errContent.toString().contains("java.text.ParseException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test_query_message_by_UUID() {
		UUID uuid = UUID.randomUUID();
		String uuid_str = uuid.toString();
		String username = "james bond";
		String content = "hello";
		String time = this.format.format(new Date(this.milliTime));
		ResultSet rs = mock(ResultSet.class);
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
			when(pstmt.executeQuery()).thenReturn(rs);
			when(rs.next()).thenReturn(true, false); // First call returns true, second call returns false
			when(rs.getString("uuid")).thenReturn(uuid_str);
			when(rs.getString("username")).thenReturn(username);
			when(rs.getString("content")).thenReturn(content);
			when(rs.getString("time")).thenReturn(time);
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
		}
		List<Message> result_list = this.messageDAO.queryMessageByUUID(uuid);
		assertEquals(1, result_list.size());
		Message result_message = result_list.get(0);
		assertEquals(uuid, result_message.get_uuid());
		assertEquals(username, result_message.get_username());
		assertEquals(content, result_message.get_content());
		assertEquals(time, format.format(result_message.get_time()));
		// Creating argument captors for verification
		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		try {
			verify(pstmt, times(1)).setString(anyInt(), stringArgumentCaptor.capture());
			assertEquals(uuid_str, stringArgumentCaptor.getAllValues().get(0));
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
			fail("Encountered SQLException when verifying arguments of pstmt, see below stack trace for detail.");
		}

	}

	@Test
	public void test_sql_exception_query_message() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		SQLException test_sqle = new SQLException();
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sqle);
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}

		this.messageDAO.queryMessage(0, 0L);
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test_parse_exception_query_message() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		UUID uuid = UUID.randomUUID();
		String uuid_str = uuid.toString();
		String username = "james bond";
		String content = "hello";
		String time = "MALFORMED DATE STRING";
		ResultSet rs = mock(ResultSet.class);
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
			when(pstmt.executeQuery()).thenReturn(rs);
			when(rs.next()).thenReturn(true, false); // First call returns true, second call returns false
			when(rs.getString("uuid")).thenReturn(uuid_str);
			when(rs.getString("username")).thenReturn(username);
			when(rs.getString("content")).thenReturn(content);
			when(rs.getString("time")).thenReturn(time);
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}
		this.messageDAO.queryMessage(0, 0L);
		assertTrue(errContent.toString().contains("java.text.ParseException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test_query_message() {
		List<Message> expected = new ArrayList<Message>(5);
		for (int index = 0; index < 5; index++) {
			UUID uuid = UUID.randomUUID();
			String username = "USER" + UUID.randomUUID().toString();
			String content = "CONTENT" + UUID.randomUUID().toString();
			Date time = new Date(this.milliTime);
			Message newMessage = new Message(uuid, username, content, time);
			expected.add(newMessage);
		}
		Supplier<Stream<String>> uuids = () -> expected.stream().map(Message::get_uuid).map(UUID::toString);
		Supplier<Stream<String>> usernames = () -> expected.stream().map(Message::get_username);
		Supplier<Stream<String>> contents = () -> expected.stream().map(Message::get_content);
		Supplier<Stream<String>> times = () -> expected.stream().map(Message::get_time)
				.map(date -> (this.format.format(date)));
		ResultSet rs = mock(ResultSet.class);
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
			when(pstmt.executeQuery()).thenReturn(rs);
			when(rs.next()).thenReturn(true, new Boolean[] { true, true, true, true, false }); // First call returns
																								// true, second call
																								// returns false
			when(rs.getString("uuid")).thenReturn(uuids.get().findFirst().get(),
					uuids.get().skip(1).toArray(String[]::new));
			when(rs.getString("username")).thenReturn(usernames.get().findFirst().get(),
					usernames.get().skip(1).toArray(String[]::new));
			when(rs.getString("content")).thenReturn(contents.get().findFirst().get(),
					contents.get().skip(1).toArray(String[]::new));
			when(rs.getString("time")).thenReturn(times.get().findFirst().get(),
					times.get().skip(1).toArray(String[]::new));
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
		}
		List<Message> actual = this.messageDAO.queryMessage(5, milliTime);
		assertEquals(5, actual.size());
		for (int index = 0; index < 5; ++index) {
			assertEquals(expected.get(index), actual.get(index));
		}
		// Creating argument captors for verification
		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
		try {
			verify(pstmt, times(1)).setString(eq(1), stringArgumentCaptor.capture());
			assertEquals(this.format.format(new Date(milliTime)), stringArgumentCaptor.getAllValues().get(0));
			verify(pstmt, times(1)).setInt(eq(2), integerArgumentCaptor.capture());
			assertEquals(5, integerArgumentCaptor.getAllValues().get(0));
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
			fail("Encountered SQLException when verifying arguments of pstmt, see below stack trace for detail.");
		}

	}

	@Test
	public void test_sql_exception_query_updates() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		SQLException test_sqle = new SQLException();
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sqle);
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}

		this.messageDAO.queryUpdates(0L);
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test_query_updates() {
		ResultSet rs = mock(ResultSet.class);
		int expected = 5;
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
			when(pstmt.executeQuery()).thenReturn(rs);
			when(rs.next()).thenReturn(true, false);
			when(rs.getInt(1)).thenReturn(expected);
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
		}
		int actual = this.messageDAO.queryUpdates(milliTime);
		assertEquals(expected, actual);
		// Creating argument captors for verification
		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		try {
			verify(pstmt, times(1)).setString(eq(1), stringArgumentCaptor.capture());
			assertEquals(this.format.format(new Date(milliTime)), stringArgumentCaptor.getAllValues().get(0));
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
			fail("Encountered SQLException when verifying arguments of pstmt, see below stack trace for detail.");
		}

	}

	@Test
	public void test_sql_exception_clear_table() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		SQLException test_sqle = new SQLException();
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenThrow(test_sqle);
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}

		this.messageDAO.clearTable();
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test_clear_table() {
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
		}
		boolean success = this.messageDAO.clearTable();
		assertTrue(success);

	}

	@Test
	public void test_sql_exception_close_statement() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		SQLException test_sqle = new SQLException();
		try {
			doThrow(test_sqle).when(pstmt).close();
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}
		this.messageDAO.closeStatementAndConnection(pstmt, connection);
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test_sql_exception_close_connection() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalOut = System.out;
		PrintStream originalErr = System.err;
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
		SQLException test_sqle = new SQLException();
		try {
			doThrow(test_sqle).when(connection).close();
		} catch (SQLException sqle) {
			// Won't throw any exception in this try/catch block
			sqle.printStackTrace(System.err);
		}
		this.messageDAO.closeStatementAndConnection(pstmt, connection);
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@AfterEach
	public void last() {

	}

}
