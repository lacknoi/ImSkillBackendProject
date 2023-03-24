package com.imporve.skill.orderservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.imporve.skill.orderservice.dto.AccountRequest;
import com.imporve.skill.orderservice.dto.AccountResponse;
import com.imporve.skill.orderservice.dto.OrderItemRequest;
import com.imporve.skill.orderservice.dto.OrderRequest;
import com.imporve.skill.orderservice.model.Order;
import com.imporve.skill.orderservice.model.OrderItem;
import com.imporve.skill.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	private final WebClient.Builder webClientBuilder;
	private final OrderRepository orderRepository;
	
	public void submitOrder(OrderRequest orderRequest) {
		System.out.println("submitOrder");
		
		Order configFile = orderRepository.getById(1L);
		
		List<String> baNos = new ArrayList<>();
		baNos.add("30900000000168");
		baNos.add("31300000806387");
		
		AccountResponse[] accountArr = webClientBuilder.build().get()
				.uri("http://account-service/api/account",
						uriBuilder -> uriBuilder.queryParam("baNo", baNos).build())
				.retrieve()
				.bodyToMono(AccountResponse[].class)
				.block();
		
		List<AccountResponse> accounts = Arrays.asList(accountArr);
		
		for(AccountResponse accountResponse : accounts) {
			System.out.println(accountResponse.getAccntNo() + ":" + accountResponse.getAccntName());
		}
	}
	
	public void createOrderList(OrderRequest orderRequest) {
		for(OrderItemRequest orderItemDto : orderRequest.getOrderItemDtoList()) {
			boolean blacklistStatus = webClientBuilder.build().get()
								    .uri("http://debt-service/api/debt/blacklist/" + orderItemDto.getAccountNo())
								    .retrieve()
								    .bodyToMono(Boolean.class)
								    .block();
		}
				
		
		Order order = new Order();
		order.setOrderNo("O01");
		order.setCreated(new Date());
		order.setCreatedBy(orderRequest.getOrderBy());
		order.setLastUpd(new Date());
		order.setLastUpdBy(orderRequest.getOrderBy());
		
		List<OrderItem> orderLineItems = orderRequest.getOrderItemDtoList()
							                .stream()
							                .map(orderItemDto -> mapToDto(orderItemDto, order))
							                .toList();
		
		order.setItems(orderLineItems);
		
		orderRepository.save(order);
		
		List<AccountRequest> accountRequestList = orderRequest.getOrderItemDtoList().stream()
				.map(orderItemDto ->
						AccountRequest.builder()
						.accountLevel(orderItemDto.getAccountLevel())
						.accountName(orderItemDto.getAccountName()).build()
					).toList();
		
		AccountResponse[] accountArr = webClientBuilder.build().post()
				.uri("http://account-service/api/account")
				.body(Mono.just(accountRequestList), AccountRequest.class)
				.retrieve()
				.bodyToMono(AccountResponse[].class)
				.block();
		
		List<AccountResponse> accounts = Arrays.asList(accountArr);
		
		for(AccountResponse accountResponse : accounts) {
			System.out.println(accountResponse.getAccountId());
		}
	}
	
	public void createOrder(OrderItemRequest orderItemRequest) {
		Order order = new Order();
		order.setOrderNo("O01");
		order.setCreated(new Date());
		order.setCreatedBy(orderItemRequest.getCreateBy());
		order.setLastUpd(new Date());
		order.setLastUpdBy(orderItemRequest.getCreateBy());
		
		List<OrderItem> orderLineItems = new ArrayList<>();
		orderLineItems.add(mapToDto(orderItemRequest, order));
		
		order.setItems(orderLineItems);
		
		orderRepository.save(order);
		
		List<AccountRequest> accountRequestList = orderLineItems.stream()
				.map(orderLineItem ->
						AccountRequest.builder()
						.accountLevel(orderLineItem.getAccountLevel())
						.accountName(orderLineItem.getAccountName()).build()
					).toList();
		
		AccountResponse[] accountArr = webClientBuilder.build().post()
				.uri("http://account-service/api/account")
				.body(Mono.just(accountRequestList), AccountRequest.class)
				.retrieve()
				.bodyToMono(AccountResponse[].class)
				.block();
		
		List<AccountResponse> accounts = Arrays.asList(accountArr);
		
		for(AccountResponse accountResponse : accounts) {
			System.out.println(accountResponse.getAccountId());
		}
	}
	
	private OrderItem mapToDto(OrderItemRequest orderItemDto, Order order) {
		OrderItem orderItem = new OrderItem();
		orderItem.setOrder(order);
		orderItem.setAccountLevel(orderItemDto.getAccountLevel());
		orderItem.setAccountName(orderItemDto.getAccountName());
		orderItem.setCreated(new Date());
		orderItem.setCreatedBy(order.getLastUpdBy());
		orderItem.setLastUpd(new Date());
		orderItem.setLastUpdBy(order.getLastUpdBy());
		
        return orderItem;
    }
}
