package org.dbadmin.web;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *  Created by Jake Lee on 7/21/16.
 *  Create dispatcher servlet for Swagger
 */

public class AppInitializer implements WebApplicationInitializer {
	
    private static final String CONFIG_LOCATION = "org.dbadmin.web.SwaggerConfig.java";
    private static final String MAPPING_URL = "/swagger/*";
 
	@Override
	public void onStartup(ServletContext servletContext)throws ServletException {
		
		//Context
		WebApplicationContext context = getContext();
		
		//Dispatcher
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
				"DispatcherServlet", new DispatcherServlet(context));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping(MAPPING_URL);
		
        // UTF-8 encoding filter
        FilterRegistration charEncodingfilterReg = servletContext.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
        charEncodingfilterReg.setInitParameter("encoding", "UTF-8");
        charEncodingfilterReg.setInitParameter("forceEncoding", "true");
        charEncodingfilterReg.addMappingForUrlPatterns(null, false, "/*");
	}
 
	private AnnotationConfigWebApplicationContext getContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		
		// The config location is set to the same package where my SwaggerConfig class resides.
		context.setConfigLocation(CONFIG_LOCATION);
		return context;
	}
}
