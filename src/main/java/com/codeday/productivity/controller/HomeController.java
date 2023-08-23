package com.codeday.productivity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HomeController responsible for handling
 * web requests for the home route ("/"). It provides methods for serving
 * the homepage of the application.
 *
 * @author Nahom Alemu
 * @version 1.0
 */
@RestController
@RequestMapping(path = "/")
public class HomeController {

    /**
     * Serves the homepage by returning a welcome message.
     *
     * <p>
     * This handler method is mapped to the root URL ("/") and is invoked
     * when a GET request is made to this URL. It returns a string containing
     * the welcome message to be displayed on the homepage/ default url after deployment.
     *
     * </p>
     *
     * @return A String containing the welcome message for the homepage.
     */
    @GetMapping
    public String home() {
        return "Welcome to CodeDay Productivity Application!";
    }
}
