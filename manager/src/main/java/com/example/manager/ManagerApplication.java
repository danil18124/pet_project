package com.example.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ManagerApplication {

	// docker run --name manager-db-pet- -p 5433:5432 -e POSTGRES_DB=manager -e POSTGRES_USER=manager -e POSTGRES_PASSWORD=manager postgres:14
	
	public static void main(String[] args) {
		SpringApplication.run(ManagerApplication.class, args);
	}

}
