package com.imporve.skill.paymentservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
	
	 @GetMapping("/paymentprofiles")
	 @ResponseStatus(HttpStatus.OK)
	 public String listAll() {
        System.out.println("listAll"); 
		 
        return "accountbalances";
	 }
}
