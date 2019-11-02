package com.uncooleben.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the configuration class for SpringMVC.
 * 
 * @author Juntao Peng
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvc
@ComponentScan({ "com.uncooleben" })
public class MVCConfig extends WebMvcConfigurerAdapter {
  @Bean(name = "multipartResolver")
  public CommonsMultipartResolver multipartResolver()
  {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(20971520);	//Max size:20MB
    return multipartResolver;
  }
}
