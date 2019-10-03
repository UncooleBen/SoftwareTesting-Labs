package com.uncooleben.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

	@RequestMapping("/ajax")
	public String test() {
		return "ajax_test_page";
	}
	
	@RequestMapping("/get_time")
	@ResponseBody
	public String get_server_time() {
		System.out.println("==========get_server_time()===========");
		Date d = new Date();
		return d.toString();
	}
}
