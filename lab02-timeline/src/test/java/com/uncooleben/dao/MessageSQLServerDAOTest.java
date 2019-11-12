package com.uncooleben.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.uncooleben.model.Message;
import com.uncooleben.service.dao.MessageSQLServerDAO;

public class MessageSQLServerDAOTest {
	private MessageSQLServerDAO messageDAO;
	private Connection connection = mock(Connection.class);
	private PreparedStatement pstmt = mock(PreparedStatement.class);
	private Message message = mock(Message.class);
	private ResultSet rs = mock(ResultSet.class);
	private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private final String TEST_USERNAME = "testUsername";
	private final String TEST_CONTENT = "testContent";
	private final String TEST_UUID = "0-0-0-0-0";
	private final String TEST_TIME = "2019-10-30 12:00:00";
	private final long TEST_MILLISEC = 1572364800000L; // Equal to TEST_TIME
	private final String TEST_USERNAME2 = "testUsername2";
	private final String TEST_CONTENT2 = "testContent2";
	private final String TEST_UUID2 = "0-0-0-0-1";
	private final String TEST_TIME2 = "2019-10-30 08:00:00";

	class MessageSQLServerDAOFake extends MessageSQLServerDAO {
		@Override
		protected void loadDriver() {
		}

		@Override
		protected Connection getConnection() {
			return connection;
		}
	}

	@BeforeEach
	void init() {
		messageDAO = new MessageSQLServerDAOFake();
	}

	@Test
	void testStoreNullMessageThrowsException() {
		assertThrows(NullPointerException.class, () -> messageDAO.storeMessage(null, false));
	}

	@Test
	void testStoreOneMessage() {
		String INSERT = "INSERT INTO message(uuid, username, content, time, withImage, path) " + "VALUES(?,?,?,?,?,?)";
		Date date = mock(Date.class);
		boolean succeed = false;
		// Stub
		try {
			when(connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
			when(message.get_uuid()).thenReturn(UUID.fromString(TEST_UUID));
			when(message.get_username()).thenReturn(TEST_USERNAME);
			when(message.get_content()).thenReturn(TEST_CONTENT);
			when(message.get_time()).thenReturn(date);
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when stubbing.");
		}
		// Test return value
		succeed = messageDAO.storeMessage(message, false);
		assertTrue(succeed);
		// Test function calls
		try {
			verify(connection).prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			verify(pstmt, times(4)).setString(anyInt(), anyString());
			verify(pstmt).execute();
			verify(pstmt).close();
			verify(connection).close();
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when testing function calls.");
		}
		// Test function calls' order and capture arguments
		InOrder order = inOrder(pstmt, connection);
		ArgumentCaptor<String> argsCap = ArgumentCaptor.forClass(String.class);
		try {
			order.verify(connection).prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			order.verify(pstmt, times(4)).setString(anyInt(), argsCap.capture());
			order.verify(pstmt).execute();
			order.verify(pstmt).close();
			order.verify(connection).close();
			order.verifyNoMoreInteractions();
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when testing function calls' order.");
		}
		// Test arguments' order and value
		assertEquals(TEST_USERNAME, argsCap.getAllValues().get(1));
		assertEquals(TEST_CONTENT, argsCap.getAllValues().get(2));
	}

	@Test
	void testStoreMessageThrowsSQLException() {
		// Change error output stream to capture error output
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalErr = System.err;
		System.setErr(new PrintStream(errContent));
		// Stub
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when stubbing.");
		}
		// Test return value
		boolean succeed = true;
		succeed = messageDAO.storeMessage(message, false);
		assertFalse(succeed);
		// Test error output
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		// Change error output stream back to default
		System.setErr(originalErr);
	}

	@Test
	void testQueryMessageByUUID() {
		String SELECT = "SELECT * FROM message WHERE uuid=(?)";
		// Stub
		try {
			when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
			when(pstmt.executeQuery()).thenReturn(rs);
			when(rs.getString("uuid")).thenReturn(TEST_UUID);
			when(rs.getString("username")).thenReturn(TEST_USERNAME);
			when(rs.getString("content")).thenReturn(TEST_CONTENT);
			when(rs.getString("time")).thenReturn(TEST_TIME);
			when(rs.next()).thenReturn(true, false); // Only one item in ResultSet
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when stubbing.");
		}
		// Test return value
		List<Message> resultList = messageDAO.queryMessageByUUID(UUID.fromString(TEST_UUID));
		assertEquals(1, resultList.size());
		assertEquals(UUID.fromString(TEST_UUID), resultList.get(0).get_uuid());
		assertEquals(TEST_USERNAME, resultList.get(0).get_username());
		assertEquals(TEST_CONTENT, resultList.get(0).get_content());
		assertEquals(TEST_TIME, dateFormatter.format(resultList.get(0).get_time()));
		// Test function calls' order
		InOrder order = inOrder(connection, pstmt, rs);
		try {
			order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
			order.verify(pstmt).executeQuery();
			order.verify(rs).next();
			order.verify(rs, times(4)).getString(anyString());
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when testing function calls' order.");
		}
	}

	@Test
	void testQueryMessageByNullUUIDThrowsException() {
		assertThrows(NullPointerException.class, () -> messageDAO.queryMessageByUUID(null));
	}

	@Test
	void testQueryMessageByUUIDThrowsSQLExceptionWhenPreparingStatement() {
		// Change error output stream to capture error output
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalErr = System.err;
		System.setErr(new PrintStream(errContent));
		// Stub
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when stubbing.");
		}
		// Test return value
		List<Message> resultList = messageDAO.queryMessageByUUID(UUID.fromString(TEST_UUID));
		assertTrue(resultList.isEmpty()); // resultList should be empty
		// Test error output
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		// Change error output stream back to default
		System.setErr(originalErr);
	}

	@Test
	void testQueryMessageByUUIDThrowsSQLExceptionWhenExecutingQuery() {
		// Change error output stream to capture error output
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		PrintStream originalErr = System.err;
		System.setErr(new PrintStream(errContent));
		// Stub
		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
			when(pstmt.executeQuery()).thenThrow(SQLException.class);
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when stubbing.");
		}
		// Test return value
		List<Message> resultList = messageDAO.queryMessageByUUID(UUID.fromString(TEST_UUID));
		assertTrue(resultList.isEmpty()); // resultList should be empty
		// Test error output
		assertTrue(errContent.toString().contains("java.sql.SQLException"));
		// Change error output stream back to default
		System.setErr(originalErr);
	}

	@Test
	void testQueryMessageWithSize1AndMillisec1572364800000() {
		String SELECT = "SELECT TOP 1 * FROM message WHERE time <= ? ORDER BY time DESC";
		// Stub
		try {
			when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
			when(pstmt.executeQuery()).thenReturn(rs);
			when(rs.next()).thenReturn(true, false); // rs has only 1 item
			when(rs.getString("uuid")).thenReturn(TEST_UUID);
			when(rs.getString("username")).thenReturn(TEST_USERNAME);
			when(rs.getString("content")).thenReturn(TEST_CONTENT);
			when(rs.getString("time")).thenReturn(TEST_TIME);
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when stubbing");
		}
		// Test return value
		List<Message> resultList = messageDAO.queryMessage(1, TEST_MILLISEC);
		assertEquals(1, resultList.size());
		assertEquals(UUID.fromString(TEST_UUID), resultList.get(0).get_uuid());
		assertEquals(TEST_USERNAME, resultList.get(0).get_username());
		assertEquals(TEST_CONTENT, resultList.get(0).get_content());
		assertEquals(TEST_TIME, dateFormatter.format(resultList.get(0).get_time()));
		// Test function calls' order
		InOrder order = inOrder(connection, pstmt, rs);
		try {
			order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
			order.verify(pstmt).executeQuery();
			order.verify(rs).next();
			order.verify(rs, times(4)).getString(anyString());
			order.verify(rs).next();
			order.verify(pstmt).close();
			order.verify(connection).close();
			order.verifyNoMoreInteractions();
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when testing function calls' order");
		}
	}

	@Test
	void testQueryMessageWithSize2AndMillisec1572364800000() {
		String SELECT = "SELECT TOP 2 * FROM message WHERE time <= ? ORDER BY time DESC";
		// Stub
		try {
			when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
			when(pstmt.executeQuery()).thenReturn(rs);
			when(rs.next()).thenReturn(true, true, false); // rs has 2 items
			when(rs.getString("uuid")).thenReturn(TEST_UUID, TEST_UUID2);
			when(rs.getString("username")).thenReturn(TEST_USERNAME, TEST_USERNAME2);
			when(rs.getString("content")).thenReturn(TEST_CONTENT, TEST_CONTENT2);
			when(rs.getString("time")).thenReturn(TEST_TIME, TEST_TIME2);
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when stubbing");
		}
		// Test return value
		List<Message> resultList = messageDAO.queryMessage(2, TEST_MILLISEC);
		assertEquals(2, resultList.size());
		assertEquals(UUID.fromString(TEST_UUID), resultList.get(0).get_uuid());
		assertEquals(TEST_USERNAME, resultList.get(0).get_username());
		assertEquals(TEST_CONTENT, resultList.get(0).get_content());
		assertEquals(TEST_TIME, dateFormatter.format(resultList.get(0).get_time()));
		assertEquals(UUID.fromString(TEST_UUID2), resultList.get(1).get_uuid());
		assertEquals(TEST_USERNAME2, resultList.get(1).get_username());
		assertEquals(TEST_CONTENT2, resultList.get(1).get_content());
		assertEquals(TEST_TIME2, dateFormatter.format(resultList.get(1).get_time()));
		// Test function calls' order
		InOrder order = inOrder(connection, pstmt, rs);
		try {
			order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
			order.verify(pstmt).executeQuery();
			order.verify(rs).next();
			order.verify(rs, times(4)).getString(anyString());
			order.verify(rs).next();
			order.verify(rs, times(4)).getString(anyString());
			order.verify(rs).next();
			order.verify(pstmt).close();
			order.verify(connection).close();
			order.verifyNoMoreInteractions();
		} catch (SQLException sqlE) {
			System.out.println(sqlE.getMessage());
			fail("Exception occurs when testing function calls' order");
		}
	}
}
