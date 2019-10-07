package com.uncooleben.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.uncooleben.dao.MessageDAO;
import com.uncooleben.model.Message;

@Controller
public class NewMessageController {

private MessageDAO messageDAO;
	
	public NewMessageController() {
		this.messageDAO = new MessageDAO();
	}
	
	@RequestMapping("/newMessage")
	public ModelAndView onSubmit(@RequestParam("username") String username, @RequestParam("content") String content) {
		System.out.println("onSubmit()");
		Message newMessage = new Message(username, content, new Date(System.currentTimeMillis()));
		this.messageDAO.storeMessage(newMessage);
		ModelAndView mv = new ModelAndView("redirect:/");
        return mv;
	}
	
}
