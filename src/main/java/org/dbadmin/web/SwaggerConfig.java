package org.dbadmin.web;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.ResponseEntity;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;

/**
 *  Created by Jake Lee on 7/21/16.
 *  Configuration for Swagger
 */

@Configuration
// Enables Swagger
@EnableSwagger 
// Mandatory scan for components in the swagger package
@ComponentScan(basePackages = "com.mangofactory.swagger") 
//I have strapped in properties here which contains the application contextpath and version
@PropertySource(value = "classpath:spring/data-access.properties")
public class SwaggerConfig {
 
    @Value("${swagger.basepath}")
    private String appBasepath;
    
    @Value("${swagger.version}")
    private String appVersion;
    
    @Autowired
    private SpringSwaggerConfig springSwaggerConfig;
    
 
    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
            .apiInfo(apiInfo())
            .apiVersion(appVersion)
            .pathProvider(apiPathProvider())
            .ignoredParameterTypes(ResponseEntity.class); // Supposedly fixes a bug in Swagger UI
    }
    
    /**
     * API INFO
     * @return
     */
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "DFG APIs",                         // Title of your API
                "APIs for Data Information Service",// Description of your API
                "http://terms",                     // Url to usage terms for your API, if you have any
                "admin@scryanalytics.com",          // 
                "Apache 2.0",                       // Name and URL of license for users of your API
                "http://www.apache.org/licenses/LICENSE-2.0.html");
        return apiInfo;
    }
 
    
    /**
     * Class that provides your applications url context path
     */
    @Bean
    public ApiPathProvider apiPathProvider() {
        ApiPathProvider apiPathProvider = new ApiPathProvider(appBasepath);
        apiPathProvider.setDefaultSwaggerPathProvider(springSwaggerConfig.defaultSwaggerPathProvider());
        return apiPathProvider;
    }
    
    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}