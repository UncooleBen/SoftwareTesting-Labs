package com.uncooleben.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.uncooleben.dao.MessageDAO;

@Controller
public class IndexController {

	private MessageDAO messageDAO;
	private int newMessages;
	
	public IndexController() {
		this.messageDAO = new MessageDAO();
		this.newMessages = 0;
	}

	@RequestMapping("/")
    public ModelAndView display()  
    {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("newMessages", String.valueOf(newMessages));
        return mv;
    }
	
}
