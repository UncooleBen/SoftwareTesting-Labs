package com.uncooleben.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.uncooleben.dao.MessageDAO;

@Controller
public class IndexController {

	private MessageDAO messageDAO;
	
	public IndexController() {
		this.messageDAO = new MessageDAO();
	}

	@RequestMapping("/")
    public ModelAndView display()  
    {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("lastRefreshTime", String.valueOf(System.currentTimeMillis()));
		mv.addObject("numberOfMessage", String.valueOf((int)3));
		mv.addObject("messageList", messageDAO.queryMessage(3, System.currentTimeMillis()));
        return mv;
    }
	
}
