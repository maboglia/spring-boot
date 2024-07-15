package com.maboglia.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class Router {

	
	Logger logger = LoggerFactory.getLogger(Router.class);
	
	@GetMapping("home")
	public String home() {
		
		logger.error("errore");
		logger.warn("warning");
		logger.trace("entrato pippo");
		
		return "home";
	}
	
	
}
