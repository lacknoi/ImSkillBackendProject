package com.imporve.skill.orderservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.imporve.skill.orderservice.dto.OrderFileWrapperRequest;
import com.imporve.skill.orderservice.dto.OrderRequest;
import com.imporve.skill.orderservice.dto.OrderResponse;
import com.imporve.skill.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService orderService;
	
	@GetMapping("/{order-no}")
	@ResponseStatus(HttpStatus.OK)
	public OrderResponse getOrderByNo(@PathVariable("order-no") String orderNo) {
		return orderService.getOrderByNo(orderNo);
	}
	 
	@DeleteMapping("/delete-order/{order-no}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> deleteOrderByNo(@PathVariable("order-no") String orderNo) {
		orderService.deleteOrderByNo(orderNo);
		
		return new ResponseEntity<>(orderNo, HttpStatus.OK);
	}
	 
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<OrderResponse> listAlls() {
        return orderService.getOrderList(null);
	}
	 
	@PostMapping("/create-order")
	public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderItemRequest) {
		orderService.createOrder(orderItemRequest);
		 
		return new ResponseEntity<>("createOrder", HttpStatus.OK);
	}
	
	@PostMapping("/create-account")
	public ResponseEntity<OrderResponse> requestCreateAccount(OrderFileWrapperRequest fileWrapperRequest) throws IOException {
		OrderResponse orderResponse = orderService.requestCreateAccount(fileWrapperRequest);
		 
		return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
	}
	
	@PostMapping("/approve-order")
	public ResponseEntity<String> approveOrder(@RequestBody OrderRequest itemRequest) {
		 
		return new ResponseEntity<>("createOrder", HttpStatus.CREATED);
	}
}
