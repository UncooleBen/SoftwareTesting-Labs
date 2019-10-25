package com.uncooleben.controller;

import com.uncooleben.dao.MessageDBDAO;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.uncooleben.dao.MessageDAO;
import com.uncooleben.dao.MessageMySQLDAO;
import com.uncooleben.model.Message;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the new message controller. When user comment a new message, methods
 * in this class will be called to perform update and refresh for the view.
 * 
 * @author Juntao Peng
 */
@Controller
public class NewMessageController {

	private MessageDAO messageDAO;

	/**
	 * Constructor
	 */
	public NewMessageController() {
		this.messageDAO = (new MessageDBDAO()).getActualDAO();
	}

	/**
	 * This method is called when user hits the submit button in web page
	 * 
	 * @param username A string of username
	 * @param content  A string of message content
	 * 
	 * @return A ModelAndView object of refresh of the current web page
	 */
	@RequestMapping("/newMessage")
	public ModelAndView onSubmit(@RequestParam("username") String username, @RequestParam("content") String content) {
		System.out.println("onSubmit()");
		Message newMessage = new Message(username, content, new Date(System.currentTimeMillis()));
		System.out.println(newMessage);
		this.messageDAO.storeMessage(newMessage);
		ModelAndView mv = new ModelAndView("redirect:/");
		return mv;
	}

}
