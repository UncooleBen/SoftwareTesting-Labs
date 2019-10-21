package com.uncooleben.controller;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.uncooleben.config.MVCConfig;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the front controller class for SpringMVC.
 * 
 * @author Juntao Peng
 */
public class FrontController extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { MVCConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
