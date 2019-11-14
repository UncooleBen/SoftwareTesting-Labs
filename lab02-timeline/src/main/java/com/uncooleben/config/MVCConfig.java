package com.uncooleben.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.uncooleben.service.dao.MessageDAO;
import com.uncooleben.service.dao.MessageDBDAO;
import com.uncooleben.service.file.FAO;
import com.uncooleben.service.file.FAOImpl;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the configuration class for SpringMVC Dispatcher, which serves as
 * web.xml in normal Java Web Apps.
 * 
 * @author Juntao Peng
 */
@EnableWebMvc
@Configuration
@ComponentScan({ "com.uncooleben" })
public class MVCConfig {

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix("/WEB-INF/jsp/");
		vr.setSuffix(".jsp");
		return vr;
	}

	@Bean(name = "multipartResolver")
	public StandardServletMultipartResolver getStandardServletMultipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Bean(name = "messageDAO")
	public MessageDAO getMessageDAO() {
		return (new MessageDBDAO().getActualDAO());
	}

	@Bean(name = "fao")
	public FAO getFAO() {
		return new FAOImpl();
	}

}
