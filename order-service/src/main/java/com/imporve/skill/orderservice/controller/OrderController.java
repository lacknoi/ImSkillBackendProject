package com.imporve.skill.orderservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imporve.skill.orderservice.dto.OrderRequest;
import com.imporve.skill.orderservice.service.OrderService;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;
	
	 @GetMapping("/order")
	 @ResponseStatus(HttpStatus.OK)
	 public String listAll() {
		orderService.submitOrder(new OrderRequest());
		 
        return "order";
	 }
}