package com.britenet.contacts.task;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication
public class TaskApplication {

	@Autowired
	public TaskApplication(EntityManagerFactory factory) {
		this.factory = factory;
	}

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	final EntityManagerFactory factory;

	@Bean
	public SessionFactory sessionFactory() {
		return factory.unwrap(SessionFactory.class);
	}

}
