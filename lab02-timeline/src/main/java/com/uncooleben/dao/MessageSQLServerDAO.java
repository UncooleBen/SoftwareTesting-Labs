package com.uncooleben.dao;

import com.uncooleben.model.Message;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MessageSQLServerDAO implements MessageDAO {
  private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private static String mysqlUrl = "jdbc:sqlserver://localhost:1433;"
      + "databaseName=timeline";
  private static String mysqlUser = "sa";
  private static String mysqlPassword = "root";
  private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  protected void loadDriver() {
    try {
      Class.forName(driver);
    } catch (Exception classNotFoundException) {
      classNotFoundException.printStackTrace(System.err);
    }
  }

  protected Connection getConnection() throws SQLException {
    Connection conn = null;
    loadDriver();
    conn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);
    return conn;
  }

  protected void closeStatementAndConnection(PreparedStatement pstmt, Connection conn) {
    if (pstmt != null) {
      try {
        pstmt.close();
      } catch (SQLException sqle) {
        sqle.printStackTrace(System.err);
      }
    }

    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException sqle) {
        sqle.printStackTrace(System.err);
      }
    }

  }

  @Override
  public boolean storeMessage(Message message) {
    String INSERT = "INSERT INTO message(uuid, username, content, time) " + "VALUES(?,?,?,?)";
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
      pstmt.setString(1, message.get_uuid().toString());
      pstmt.setString(2, message.get_username());
      pstmt.setString(3, message.get_content());
      pstmt.setString(4, format.format(message.get_time()));
      pstmt.execute();
      return true;
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.err);
    } finally {
      closeStatementAndConnection(pstmt, conn);
    }
    return false;
  }

  @Override
  public List<Message> queryMessageByUUID(UUID uuid) {
    String SELECT = "SELECT * FROM message WHERE uuid=(?)";
    List<Message> result_list = new ArrayList<Message>();
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
      pstmt.setString(1, uuid.toString());
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        Message temp_message = new Message(UUID.fromString(rs.getString("uuid")), rs.getString("username"),
            rs.getString("content"), format.parse(rs.getString("time")));
        result_list.add(temp_message);
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.err);
    } catch (ParseException pe) {
      pe.printStackTrace(System.err);
    } finally {
      closeStatementAndConnection(pstmt, conn);
    }
    return result_list;
  }

  @Override
  public List<Message> queryMessage(int size, long millisec) {
    String SELECT = "SELECT TOP "
        + size
        + " * FROM message WHERE time <= ? ORDER BY time DESC";
    List<Message> result_list = new ArrayList<Message>();
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
      pstmt.setString(1, format.format(new Date(millisec)));
      //pstmt.setInt(2, size);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        Message temp_message = new Message(UUID.fromString(rs.getString("uuid")), rs.getString("username"),
            rs.getString("content"), format.parse(rs.getString("time")));
        temp_message.set_ago(millisec);
        result_list.add(temp_message);
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.err);
    } catch (ParseException pe) {
      pe.printStackTrace(System.err);
    } finally {
      closeStatementAndConnection(pstmt, conn);
    }
    return result_list;
  }

  @Override
  public int queryUpdates(long millisec) {
    String formattedDate = format.format(new Date(millisec));
    String SELECT = "SELECT COUNT(*) FROM message WHERE time > ?";
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
      pstmt.setString(1, formattedDate);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.err);
    } finally {
      closeStatementAndConnection(pstmt, conn);
    }
    return 0;
  }

  public void clearTable() {
    String DELETE = "DELETE FROM message";
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS);
      pstmt.execute();
    } catch (SQLException sqle) {
      sqle.printStackTrace(System.err);
    } finally {
      closeStatementAndConnection(pstmt, conn);
    }
  }

}