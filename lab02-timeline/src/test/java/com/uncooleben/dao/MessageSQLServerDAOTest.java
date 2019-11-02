package com.uncooleben.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import com.mysql.cj.protocol.Resultset;
import com.uncooleben.controller.TestController;
import com.uncooleben.model.Message;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

public class MessageSQLServerDAOTest {
  private MessageSQLServerDAO messageDAO;
  private Connection connection=mock(Connection.class);
  private PreparedStatement pstmt=mock(PreparedStatement.class);
  private Message message=mock(Message.class);
  private ResultSet rs=mock(ResultSet.class);
  final private String TEST_USERNAME="testUsername";
  final private String TEST_CONTENT="testContent";
  final private String TEST_UUID="0-0-0-0-0";
  final private String TEST_TIME="2019-10-30 12:00";

  class MessageSQLServerDAOFake extends MessageSQLServerDAO
  {
    @Override
    protected void loadDriver()
    {

    }
    @Override
    protected Connection getConnection()
    {
      return connection;
    }
  }

  @BeforeEach
  void init()
  {
    messageDAO=new MessageSQLServerDAOFake();
  }

  @Test
  void testStoreMessageThrowsNullPointerException()
  {
    assertThrows(NullPointerException.class,()->messageDAO.storeMessage(null));
  }

  @Test
  void testStoreOneMessage()
  {
    String INSERT="INSERT INTO message(uuid, username, content, time) VALUES(?,?,?,?)";
    Date date=mock(Date.class);
    boolean succeed=false;
    //Stub
    try{
      when(connection.prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
      when(message.get_uuid()).thenReturn(UUID.fromString(TEST_UUID));
      when(message.get_username()).thenReturn(TEST_USERNAME);
      when(message.get_content()).thenReturn(TEST_CONTENT);
      when(message.get_time()).thenReturn(date);
    }catch (SQLException sqlE){
      System.out.println(sqlE.getMessage());
      fail("Exception occurs when stubbing.");
    }
    //Test return value
    succeed=messageDAO.storeMessage(message);
    assertTrue(succeed);
    //Test function calls
    try{
      verify(connection).prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS);
      verify(pstmt,times(4)).setString(anyInt(),anyString());
      verify(pstmt).execute();
      verify(pstmt).close();
      verify(connection).close();
    }catch (SQLException sqlE){
      System.out.println(sqlE.getMessage());
      fail("Exception occurs when testing function calls.");
    }
    //Test function calls' order and capture arguments
    InOrder order=inOrder(pstmt,connection);
    ArgumentCaptor<String> argsCap=ArgumentCaptor.forClass(String.class);
    try{
      order.verify(connection).prepareStatement(INSERT,Statement.RETURN_GENERATED_KEYS);
      order.verify(pstmt,times(4)).setString(anyInt(),argsCap.capture());
      order.verify(pstmt).execute();
      order.verify(pstmt).close();
      order.verify(connection).close();
      order.verifyNoMoreInteractions();
    }catch (SQLException sqlE){
      System.out.println(sqlE.getMessage());
      fail("Exception occurs when testing function calls' order.");
    }
    //Test arguments' order and value
    assertEquals(TEST_USERNAME,argsCap.getAllValues().get(1));
    assertEquals(TEST_CONTENT,argsCap.getAllValues().get(2));
  }

  @Test
  void testStoreMessageThrowsSQLException()
  {
    //Change error output stream to capture error output
    ByteArrayOutputStream errContent=new ByteArrayOutputStream();
    PrintStream originalErr=System.err;
    System.setErr(new PrintStream(errContent));
    //Stub
    try{
      when(connection.prepareStatement(anyString(),anyInt())).thenThrow(SQLException.class);
    }catch (SQLException sqlE){
      System.out.println(sqlE.getMessage());
      fail("Exception occurs when stubbing.");
    }
    //Test return value
    boolean succeed=true;
    succeed=messageDAO.storeMessage(message);
    assertFalse(succeed);
    //Test error output
    assertTrue(errContent.toString().contains("java.sql.SQLException"));
    //Change error output stream back to default
    System.setErr(originalErr);
  }

  @Test
  void testQueryMessageByUUID()
  {
    String SELECT="SELECT * FROM message WHERE uuid=(?)";
    //Stub
    try{
      when(connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
      when(pstmt.executeQuery()).thenReturn(rs);
      when(rs.getString("uuid")).thenReturn(TEST_UUID);
      when(rs.getString("username")).thenReturn(TEST_USERNAME);
      when(rs.getString("content")).thenReturn(TEST_CONTENT);
      when(rs.getString("time")).thenReturn(TEST_TIME);
    }catch (SQLException sqlE){
      sqlE.getMessage();
      fail("Exception occurs when stubbing.");
    }

  }
}
