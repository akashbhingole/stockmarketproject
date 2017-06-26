package com.conf;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.conf.filter.AuthenticationFilter;

/**
 * Spring configuration class, used for creating web config-related beans.
 * 
 * @author Glow Team
 */
@Configuration
public class WebConfig {

	@Bean
	public Filter authenticationFilter() {
		return new AuthenticationFilter();
	}

	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}
	
	 @Bean
	 public FilterRegistrationBean authenticationFilterRegistration() {
	    FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(authenticationFilter());
	    registration.addUrlPatterns("/v1/*");
	    registration.setOrder(1);
	    return registration;
	 }

}