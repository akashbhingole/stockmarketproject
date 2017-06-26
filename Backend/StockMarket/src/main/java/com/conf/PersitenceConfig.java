package com.conf;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring configuration class, used for creating persistence-related beans.
 * 
 * @author Glow Team
 */
@Configuration
@EnableTransactionManagement
public class PersitenceConfig {
	
	private static final String NONE = "none";

	private static final String PACKAGE_SCAN = "com.globant.backend.entity";

	private static final String SESSION_CONTEXT = "org.springframework.orm.hibernate5.SpringSessionContext";

	@Value("${database.url}")
	private String url;

	@Value("${database.driver}")
	private String driver;

	@Value("${database.user}")
	private String user;

	@Value("${database.password}")
	private String password;

	@Value("${database.hibernate.show_sql}")
	private String showSql;

	@Value("${database.hibernate.dialect}")
	private String dialect;

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		return dataSource;
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.show_sql", true);
		properties.put("hibernate.dialect", dialect);
		properties.put("hibernate.current_session_context_class", SESSION_CONTEXT);
		properties.put("hibernate.hbm2ddl.auto",NONE);
		return properties;
	}

	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionBuilder.addProperties(getHibernateProperties());
		sessionBuilder.scanPackages(PACKAGE_SCAN);
		return sessionBuilder.buildSessionFactory();
	}

	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
