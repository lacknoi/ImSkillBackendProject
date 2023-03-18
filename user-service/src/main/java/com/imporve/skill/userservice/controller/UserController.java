package com.imporve.skill.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	 @GetMapping("/login")
	 @ResponseStatus(HttpStatus.OK)
	 public String listAll() {
        System.out.println("listAll"); 
		 
        return "accountbalances";
	 }
}
