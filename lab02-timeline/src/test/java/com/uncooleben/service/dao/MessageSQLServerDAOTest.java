package com.uncooleben.service.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.uncooleben.model.Message;
import java.awt.desktop.SystemEventListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;

public class MessageSQLServerDAOTest {
  private MessageSQLServerDAO messageDAO;
  private Connection connection = mock(Connection.class);
  private PreparedStatement pstmt = mock(PreparedStatement.class);
  private Message message = mock(Message.class);
  private ResultSet rs = mock(ResultSet.class);
  private File actualFile = mock(File.class);
  private MultipartFile image = mock(MultipartFile.class);
  private DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  private final String TEST_USERNAME = "testUsername";
  private final String TEST_CONTENT = "testContent";
  private final String TEST_UUID = "0-0-0-0-0";
  private final String TEST_TIME = "2019-10-30 12:00:00";
  private final String TEST_PATH = "testPath";
  private final long TEST_MILLISEC = 1572364800000L; // Equal to TEST_TIME
  private final String TEST_USERNAME2 = "testUsername2";
  private final String TEST_CONTENT2 = "testContent2";
  private final String TEST_UUID2 = "0-0-0-0-1";
  private final String TEST_TIME2 = "2019-10-30 08:00:00";

  class MessageSQLServerDAOFake extends MessageSQLServerDAO {
    @Override
    protected void loadDriver() {}

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
  void testStoreNullMessageWithoutImageThrowsException() {
    assertThrows(NullPointerException.class, () -> messageDAO.storeMessage(null, true));
  }

  @Test
  void testStoreOneMessageWithoutImage() {
    String INSERT =
        "INSERT INTO message(uuid, username, content, time, withImage, path) VALUES(?,?,?,?,?,?)";
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
    // Test function calls' order and capture arguments
    InOrder order = inOrder(pstmt, connection);
    ArgumentCaptor<String> argsCap = ArgumentCaptor.forClass(String.class);
    try {
      order.verify(connection).prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
      order.verify(pstmt, times(4)).setString(anyInt(), argsCap.capture());
      order.verify(pstmt).setBoolean(5, false);
      order.verify(pstmt).setString(6, null);
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
  void testStoreMessageWithoutImageThrowsSQLException() {
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
  void testStoreNullMessageWithImageThrowsException() {
    assertThrows(NullPointerException.class, () -> messageDAO.storeMessage(null, true));
  }

  @Test
  void testStoreOneMessageWithImage() {
    String INSERT =
        "INSERT INTO message(uuid, username, content, time, withImage, path) VALUES(?,?,?,?,?,?)";
    Date date = mock(Date.class);
    boolean succeed = false;
    messageDAO = spy(messageDAO);
    // Stub
    try {
      when(connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
      when(message.get_uuid()).thenReturn(UUID.fromString(TEST_UUID));
      when(message.get_username()).thenReturn(TEST_USERNAME);
      when(message.get_content()).thenReturn(TEST_CONTENT);
      when(message.get_time()).thenReturn(date);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when stubbing.");
    }
    // Test return value
    succeed = messageDAO.storeMessage(message, true);
    assertTrue(succeed);
    // Test function calls' order and capture arguments
    InOrder order = inOrder(pstmt, connection, messageDAO);
    ArgumentCaptor<String> argsCap = ArgumentCaptor.forClass(String.class);
    try {
      order.verify(connection).prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
      order.verify(pstmt, times(4)).setString(anyInt(), argsCap.capture());
      order.verify(pstmt).setBoolean(5, true);
      order.verify(pstmt).setString(eq(6), anyString());
      order.verify(pstmt).execute();
      order.verify(pstmt).close();
      order.verify(connection).close();
      order.verifyNoMoreInteractions();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when testing function calls' order.");
    }
    // Test arguments' order and value
    assertEquals(TEST_USERNAME, argsCap.getAllValues().get(1));
    assertEquals(TEST_CONTENT, argsCap.getAllValues().get(2));
  }

  @Test
  void testStoreMessageWithImageThrowsSQLException() {
    // Change error output stream to capture error output
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    PrintStream originalErr = System.err;
    System.setErr(new PrintStream(errContent));
    messageDAO = spy(messageDAO);
    // Stub
    try {
      when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
      when(message.get_uuid()).thenReturn(UUID.fromString(TEST_UUID));
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when stubbing.");
    }
    // Test return value
    boolean succeed = true;
    succeed = messageDAO.storeMessage(message, true);
    assertFalse(succeed);
    // Test error output
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
    // Change error output stream back to default
    System.setErr(originalErr);
  }

  @Test
  void testQueryMessageByUUIDReturnNoImage() {
    String SELECT = "SELECT * FROM message WHERE uuid=(?)";
    // Stub
    try {
      when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
      when(pstmt.executeQuery()).thenReturn(rs);
      when(rs.getString("uuid")).thenReturn(TEST_UUID);
      when(rs.getString("username")).thenReturn(TEST_USERNAME);
      when(rs.getString("content")).thenReturn(TEST_CONTENT);
      when(rs.getString("time")).thenReturn(TEST_TIME);
      when(rs.getBoolean("withImage")).thenReturn(false);
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
    assertNull(resultList.get(0).get_path());
    // Test function calls' order
    InOrder order = inOrder(connection, pstmt, rs);
    try {
      order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
      order.verify(pstmt).executeQuery();
      order.verify(rs).next();
      order.verify(rs, times(4)).getString(anyString());
      order.verify(rs).getBoolean(anyString());
      order.verify(pstmt).close();
      order.verify(connection).close();
      order.verifyNoMoreInteractions();
    } catch (SQLException sqlE) {
      System.out.println(sqlE.getMessage());
      fail("Exception occurs when testing function calls' order.");
    }
  }

  @Test
  void testQueryMessageByUUIDReturnImage() {
    String SELECT = "SELECT * FROM message WHERE uuid=(?)";
    // Stub
    try {
      when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
      when(pstmt.executeQuery()).thenReturn(rs);
      when(rs.getString("uuid")).thenReturn(TEST_UUID);
      when(rs.getString("username")).thenReturn(TEST_USERNAME);
      when(rs.getString("content")).thenReturn(TEST_CONTENT);
      when(rs.getString("time")).thenReturn(TEST_TIME);
      when(rs.getBoolean("withImage")).thenReturn(true);
      when(rs.getString("path")).thenReturn(TEST_PATH);
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
    assertEquals(TEST_PATH, resultList.get(0).get_path());
    // Test function calls' order
    InOrder order = inOrder(connection, pstmt, rs);
    try {
      order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
      order.verify(pstmt).executeQuery();
      order.verify(rs).next();
      order.verify(rs, times(4)).getString(anyString());
      order.verify(rs).getBoolean(anyString());
      order.verify(rs).getString(anyString());
      order.verify(pstmt).close();
      order.verify(connection).close();
      order.verifyNoMoreInteractions();
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
  void testQueryMessageWithSize1AndMillisec1572364800000WithImage() {
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
      when(rs.getBoolean("withImage")).thenReturn(true);
      when(rs.getString("path")).thenReturn(TEST_PATH);
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
      order.verify(rs).getBoolean(anyString());
      order.verify(rs).getString(anyString());
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
  void testQueryMessageWithSize2AndMillisec1572364800000WithoutImage() {
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
      when(rs.getBoolean("withImage")).thenReturn(false,false);
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
      order.verify(rs).getBoolean(anyString());
      order.verify(rs).next();
      order.verify(rs, times(4)).getString(anyString());
      order.verify(rs).getBoolean(anyString());
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
  void testQueryMessageThrowsSQLException(){
    // Change error output stream to capture error output
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    PrintStream originalErr = System.err;
    System.setErr(new PrintStream(errContent));
    // Stub
    try {
      when(connection.prepareStatement(anyString(),anyInt())).thenThrow(SQLException.class);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when stubbing.");
    }
    //Test return value
    List<Message> result=messageDAO.queryMessage(0,0);
    assertEquals(0,result.size());
    // Test error output
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
    // Change error output stream back to default
    System.setErr(originalErr);
  }

  @Test
  void testQueryUpdates() {
    String SELECT = "SELECT COUNT(*) FROM message WHERE time > ?";
    // Stub
    try {
      when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
      when(pstmt.executeQuery()).thenReturn(rs);
      when(rs.next()).thenReturn(true);
      when(rs.getInt(1)).thenReturn(3);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when stubbing.");
    }
    // Test return value
    int result = messageDAO.queryUpdates(TEST_MILLISEC);
    assertEquals(3, result);
    // Test function calls' order
    InOrder order = inOrder(connection, pstmt, rs);
    try {
      order.verify(connection).prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
      order.verify(pstmt).executeQuery();
      order.verify(rs).next();
      order.verify(rs).getInt(1);
      order.verify(pstmt).close();
      order.verify(connection).close();
      order.verifyNoMoreInteractions();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when testing function calls' order.");
    }
  }

  @Test
  void testQueryUpdatesThrowsSQLException() {
    // Change error output stream to capture error output
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    PrintStream originalErr = System.err;
    System.setErr(new PrintStream(errContent));
    // Stub
    try {
      when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when stubbing.");
    }
    // Test return value
    int result = messageDAO.queryUpdates(TEST_MILLISEC);
    assertEquals(0, result);
    // Test error output
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
    // Change error output stream back to default
    System.setErr(originalErr);
  }

  @Test
  void testClearTable() {
    String DELETE = "DELETE FROM message";
    // Stub
    try {
      when(connection.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when stubbing.");
    }
    // Test return value
    boolean result = messageDAO.clearTable();
    assertTrue(result);
    // Test function calls' order
    InOrder order = inOrder(connection, pstmt);
    try {
      order.verify(connection).prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS);
      order.verify(pstmt).execute();
      order.verify(pstmt).close();
      order.verify(connection).close();
      order.verifyNoMoreInteractions();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when testing function calls' order.");
    }
  }

  @Test
  void testClearTableThrowsSQLException() {
    // Change error output stream to capture error output
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    PrintStream originalErr = System.err;
    System.setErr(new PrintStream(errContent));
    // Stub
    try {
      when(connection.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when stubbing.");
    }
    messageDAO.clearTable();
    // Test error output
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
    // Change error output stream back to default
    System.setErr(originalErr);
  }

  @Test
  void testCloseStatementThrowsSQLException(){
    // Change error output stream to capture error output
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    PrintStream originalErr = System.err;
    System.setErr(new PrintStream(errContent));
    // Stub
    try {
      doThrow(SQLException.class).when(pstmt).close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when stubbing.");
    }
    messageDAO.closeStatementAndConnection(pstmt,connection);
    // Test error output
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
    // Change error output stream back to default
    System.setErr(originalErr);
  }

  @Test
  void testCloseConnectionThrowsSQLException(){
    // Change error output stream to capture error output
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    PrintStream originalErr = System.err;
    System.setErr(new PrintStream(errContent));
    // Stub
    try {
      doThrow(SQLException.class).when(connection).close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      fail("Exception occurs when stubbing.");
    }
    messageDAO.closeStatementAndConnection(pstmt,connection);
    // Test error output
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
    // Change error output stream back to default
    System.setErr(originalErr);
  }
}