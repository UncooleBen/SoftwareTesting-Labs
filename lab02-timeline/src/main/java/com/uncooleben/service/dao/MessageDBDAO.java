package com.uncooleben.service.dao;

public class MessageDBDAO {
	private static String platform = "1"; // 0 stands for MySQL, 1 stands for SQLServer
	private MessageDAO actualDAO;

	public MessageDAO getActualDAO() {
		return actualDAO;
	}

	public MessageDBDAO() {
		if (platform.equals("0")) {
			actualDAO = new MessageDaoMysqlImpl();
		} else if (platform.equals("1")) {
			actualDAO = new MessageSQLServerDAO();
		}
	}
}
