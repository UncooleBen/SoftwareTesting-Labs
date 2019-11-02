package com.uncooleben.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the configuration class for SpringMVC Dispatcher, which serves as
 * web.xml in normal Java Web Apps.
 * 
 * @author Juntao Peng
 */
@Configuration
@ComponentScan({ "com.uncooleben" })
public class DispatcherConfig {

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix("/WEB-INF/jsp/");
		vr.setSuffix(".jsp");
		return vr;
	}

}
