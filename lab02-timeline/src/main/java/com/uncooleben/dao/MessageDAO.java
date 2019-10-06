package com.uncooleben.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.UUID;

import com.uncooleben.model.Message;

public class MessageDAO {
	
	private Connection conn = null;
	private SimpleDateFormat format;
	
	public MessageDAO() {
		try {
			String mysqlUser = "root";
			String mysqlPassword = "root";
			String mysqlUrl = "jdbc:mysql://localhost:3306/lab02-timeline?serverTimezone=UTC";
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword);
		    this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} catch(Exception e) {
		   e.printStackTrace();
		}
	}
	
	public void storeMessage(Message message) {
		String INSERT = "INSERT INTO message(uuid, username, content, time) " + "VALUES(?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, message.get_uuid().toString());
			pstmt.setString(2, message.get_username());
			pstmt.setString(3, message.get_content());
			pstmt.setString(4, format.format(message.get_time()));
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Message> queryMessage(UUID uuid) {
		String SELECT = "SELECT * FROM message WHERE uuid=(?)";
		List<Message> result_list = new ArrayList<Message>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, uuid.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Message temp_message = new Message(UUID.fromString(rs.getString("uuid")), rs.getString("username"), rs.getString("content"), format.parse(rs.getString("time")));
				result_list.add(temp_message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result_list;
	}
	
	public List<Message> queryMessage(int size, long millisec) {
		String SELECT = "SELECT * FROM message WHERE time <= ? ORDER BY time DESC LIMIT ?";
		List<Message> result_list = new ArrayList<Message>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, format.format(new Date(millisec)));
			pstmt.setInt(2, size);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Message temp_message = new Message(UUID.fromString(rs.getString("uuid")), rs.getString("username"), rs.getString("content"), format.parse(rs.getString("time")));
				temp_message.set_ago(millisec);
				result_list.add(temp_message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result_list;
	}
	
	public int queryUpdates(long millisec) {
		String formattedDate = format.format(new Date(millisec));
		String SELECT = "SELECT COUNT(*) FROM message WHERE time > ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, formattedDate);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void finalize() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
