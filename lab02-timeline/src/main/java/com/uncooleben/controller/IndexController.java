package com.uncooleben.controller;

import com.uncooleben.dao.MessageDBDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.uncooleben.dao.MessageDAO;
import com.uncooleben.dao.MessageMySQLDAO;
import com.uncooleben.dao.MessageDBDAO;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the index controller. When user first visit our web site, he will be
 * dispatched to this controller.
 * 
 * @author Juntao Peng
 */
@Controller
public class IndexController {

	private MessageDAO messageDAO;

	/**
	 * Constructor of this class
	 */
	public IndexController() {
		this.messageDAO = (new MessageDBDAO()).getActualDAO();
	}

	/**
	 * Redirect the user to the ModelAndView of index.jsp. Sets the lastRefreshTime
	 * and number of Message attribute to the view page.
	 * 
	 * @return A ModelAndView object of index.jsp
	 */
	@RequestMapping("/")
	public ModelAndView display() {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("lastRefreshTime", String.valueOf(System.currentTimeMillis()));
		mv.addObject("numberOfMessage", String.valueOf(0));
		return mv;
	}

}
