package com.imporve.skill.orderservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imporve.skill.orderservice.dto.OrderRequest;
import com.imporve.skill.orderservice.model.Order;
import com.imporve.skill.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
//	private final WebClient webClient;
	private final OrderRepository orderRepository;
	
	public void submitOrder(OrderRequest orderRequest) {
		System.out.println("submitOrder");
		
		Order configFile = orderRepository.getById(1L);
		
//		String s = webClient.get()
//				.uri("http://localhost:8080/api/account/accounts")
//				.retrieve()
//				.bodyToMono(String.class)
//				.block();
//		
		System.out.println(configFile.getParamType());
	}
}
