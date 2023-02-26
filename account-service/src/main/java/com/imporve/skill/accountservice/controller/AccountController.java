package com.imporve.skill.accountservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {
	 @GetMapping("/accounts")
	 public String listAll() {
        System.out.println("listAll"); 
		 
        return "accounts";
	 }

	 @GetMapping("/bano/{ba-no}")
	 @ResponseStatus(HttpStatus.OK)
	 public String getAccountByBANo(@PathVariable("ba-no") String baNo) {
        System.out.println("BA No " + baNo); 
		 
        return "getAccountByBANo";
	 }
}
