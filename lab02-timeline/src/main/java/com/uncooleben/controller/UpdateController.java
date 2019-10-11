package com.uncooleben.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uncooleben.dao.MessageDAO;
import com.uncooleben.dao.MessageMySQLDAO;

@Controller
public class UpdateController {

	private MessageDAO messageDAO;

	public UpdateController() {
		this.messageDAO = new MessageMySQLDAO();
	}

	@RequestMapping("/update")
	@ResponseBody
	public String update(String lastRefreshTime) {
		String result = " Update(s)";
		long lastRefreshTimeLong = Long.parseLong(lastRefreshTime);
		int new_messages = this.messageDAO.queryUpdates(lastRefreshTimeLong);
		result = new_messages + result;
		return result;
	}

}
