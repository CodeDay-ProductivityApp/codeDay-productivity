package com.codeday.productivity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * ProductivityApplication is the main entry point for the Spring Boot application.
 * This class bootstraps the Spring Boot application with the help of {@link SpringApplication}.
 * <p>
 * The class is also annotated with {@link RestController} which indicates that it can
 * handle incoming web requests.
 * </p>
 * <p>
 * The {@link SpringBootApplication} annotation indicates that it's a primary configuration class
 * and also triggers auto-configuration and component scanning.
 * </p>
 *
 * @author [Nahom Alemu]
 * @version 1.0
 * @date 22-08-23
 */
@SpringBootApplication
@RestController
public class ProductivityApplication {

	private static final Logger LOGGER = LogManager.getLogger(ProductivityApplication.class);

	/**
	 * This is the main method that serves as an entry point for the Java application.
	 * When the application starts, it triggers the Spring Boot's SpringApplication
	 * to bootstrap the app.
	 * API documentation is available at:
	 * <a href="https://codeday-productivity-app.azurewebsites.net/swagger-ui/index.html">
	 * Swagger UI
	 * </a>
	 * OpenAPIs descriptions can be found at:
	 * <a href="https://codeday-productivity-app.azurewebsites.net/v3/api-docs">
	 * OpenAPI JSON
	 * </a>
	 *
	 * @param args Arguments passed to the application. Not specifically used in this application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProductivityApplication.class, args);

		LOGGER.info("ProductivityApplication started successfully!");
	}


}
