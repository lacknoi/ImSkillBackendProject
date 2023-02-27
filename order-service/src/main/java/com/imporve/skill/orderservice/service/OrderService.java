package com.imporve.skill.orderservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.imporve.skill.orderservice.dto.AccountResponse;
import com.imporve.skill.orderservice.dto.OrderRequest;
import com.imporve.skill.orderservice.model.Order;
import com.imporve.skill.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	private final WebClient webClient;
	private final OrderRepository orderRepository;
	
	public void submitOrder(OrderRequest orderRequest) {
		System.out.println("submitOrder");
		
		Order configFile = orderRepository.getById(1L);
		
		List<String> baNos = new ArrayList<>();
		baNos.add("30900000000168");
		baNos.add("31300000806387");
		
		AccountResponse[] accountArr = webClient.get()
				.uri("http://localhost:8080/api/account",
						uriBuilder -> uriBuilder.queryParam("baNo", baNos).build())
				.retrieve()
				.bodyToMono(AccountResponse[].class)
				.block();
		
		List<AccountResponse> accounts = Arrays.asList(accountArr);
		
		for(AccountResponse accountResponse : accounts) {
			System.out.println(accountResponse.getAccntNo() + ":" + accountResponse.getAccntName());
		}
	}
}
