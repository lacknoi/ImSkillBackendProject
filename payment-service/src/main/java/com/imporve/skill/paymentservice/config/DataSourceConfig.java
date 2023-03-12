package com.imporve.skill.paymentservice.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource("file:${batch.path.config}") 
public class DataSourceConfig {
	//- file:/home/jboss/batch_java/batch-config.properties
	//- file:D:\\workspace\\batch-config.properties
	//- file:${batch.path.config}
	//- classpath:batch-config.properties
	
	@Value("${jdbc.im.username}")
	private String userName;
	@Value("${jdbc.im.password}")
	private String password;
	@Value("${jdbc.im.url}")
	private String url;
	@Value("${jdbc.im.driverClassName}")
	private String driverClassName;
	
	
	@Bean
    public DataSource getDataSource() {
		DriverManagerDataSource dataSourceBuilder = new DriverManagerDataSource();
        dataSourceBuilder.setDriverClassName(driverClassName);
        dataSourceBuilder.setUrl(url);
        dataSourceBuilder.setUsername(userName);
        dataSourceBuilder.setPassword(password);
        return dataSourceBuilder;
    }
}
