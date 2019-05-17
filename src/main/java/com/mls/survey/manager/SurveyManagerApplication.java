package com.mls.survey.manager;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@SpringBootApplication
public class SurveyManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurveyManagerApplication.class, args);
	}

	@Autowired
	private Environment env;

	@Bean
	public DataSource mysqlDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(env.getProperty("app.db.url"));
		dataSource.setUsername(env.getProperty("app.db.user"));
		dataSource.setPassword(env.getProperty("app.db.password"));
		dataSource.setSchema("public");
		return dataSource;
	}
}
