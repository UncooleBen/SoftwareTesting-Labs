package com.uncooleben.controller;

import com.uncooleben.dao.MessageDBDAO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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
	@RequestMapping(value = "/newMessage",method = RequestMethod.POST)
	public ModelAndView onSubmit(@RequestParam("username") String username,
			@RequestParam("content") String content,
			@RequestParam(value = "image",required = false) MultipartFile image
			) {
		System.out.println("onSubmit()");
//		System.out.println(username);
//		System.out.println(content);
		System.out.println(image.getOriginalFilename());
		System.out.println(image.getSize());
		Message newMessage = new Message(username, content, new Date(System.currentTimeMillis()));
		System.out.println(newMessage);
		if(image.isEmpty())
		{
			this.messageDAO.storeMessage(newMessage);
		}
		else
		{
			this.messageDAO.storeMessage(newMessage,image);
		}
		ModelAndView mv = new ModelAndView("redirect:/");
		return mv;
	}

}
