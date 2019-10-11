package com.uncooleben.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.uncooleben.dao.MessageDAO;
import com.uncooleben.dao.MessageMySQLDAO;
import com.uncooleben.model.Message;

@Controller
public class MoreController {

	private MessageDAO messageDAO;

	public MoreController() {
		this.messageDAO = new MessageMySQLDAO();
	}

	@RequestMapping("/more")
	@ResponseBody
	public String more(String numberOfMessage, String lastRefreshTime) {
		List<Message> messages = this.messageDAO.queryMessage(Integer.parseInt(numberOfMessage) + 3,
				Long.parseLong(lastRefreshTime));
		Gson gson = new Gson();
		String json = gson.toJson(messages);
		return json;
	}
}
