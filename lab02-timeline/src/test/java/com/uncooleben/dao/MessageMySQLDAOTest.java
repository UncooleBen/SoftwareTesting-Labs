package com.uncooleben.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.uncooleben.model.Message;

public class MessageMySQLDAOTest {

	private Connection connection = mock(Connection.class);
	private PreparedStatement pstmt = mock(PreparedStatement.class);
	private MessageDAO messageDAO;
	private long time;

	class TestableMessageMySQLDAO extends MessageMySQLDAO {

		@Override
		protected void loadDriver() {

		}

		@Override
		protected Connection getConnection() throws SQLException {
			return connection;
		}
	}

	@Before
	public void init() {
		this.messageDAO = new TestableMessageMySQLDAO();
		this.time = System.currentTimeMillis();
	}

	@Test
	public void store_one_message() {
		Date date = new Date(time);
		Message message = new Message("james", "im bond", date);

		try {
			when(connection.prepareStatement(anyString(), anyInt())).thenReturn(pstmt);
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
		}

		boolean succeeded = this.messageDAO.storeMessage(message);
		assertTrue(succeeded);

		// Creating argument captors for verification
		ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Integer> intArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
		try {
			verify(pstmt, times(4)).setString(anyInt(), stringArgumentCaptor.capture());
			assertEquals("james", stringArgumentCaptor.getAllValues().get(1));
			assertEquals("im bond", stringArgumentCaptor.getAllValues().get(2));
		} catch (SQLException sqle) {
			sqle.printStackTrace(System.err);
			fail("Encountered SQLException when verifying arguments of pstmt, see below stack trace for detail.");
		}

	}

	@After
	public void last() {

	}

}
