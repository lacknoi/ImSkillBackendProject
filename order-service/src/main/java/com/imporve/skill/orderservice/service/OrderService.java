package com.imporve.skill.orderservice.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.imporve.skill.orderservice.AppConstant;
import com.imporve.skill.orderservice.dto.AccountFileWrapperRequest;
import com.imporve.skill.orderservice.dto.AccountRequest;
import com.imporve.skill.orderservice.dto.AccountResponse;
import com.imporve.skill.orderservice.dto.OrderItemFileWrapperRequest;
import com.imporve.skill.orderservice.dto.OrderItemRequest;
import com.imporve.skill.orderservice.dto.OrderRequest;
import com.imporve.skill.orderservice.dto.OrderResponse;
import com.imporve.skill.orderservice.model.Order;
import com.imporve.skill.orderservice.model.OrderItem;
import com.imporve.skill.orderservice.repository.OrderRepository;
import com.imporve.skill.orderservice.utils.DateTimeUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	private final WebClient.Builder webClientBuilder;
	private final OrderRepository orderRepository;
	
//	public void submitOrder(OrderRequest orderRequest) {
//		System.out.println("submitOrder");
//		
//		Order configFile = orderRepository.getById(1L);
//		
//		List<String> baNos = new ArrayList<>();
//		baNos.add("30900000000168");
//		baNos.add("31300000806387");
//		
//		AccountResponse[] accountArr = webClientBuilder.build().get()
//				.uri("http://account-service/api/account",
//						uriBuilder -> uriBuilder.queryParam("baNo", baNos).build())
//				.retrieve()
//				.bodyToMono(AccountResponse[].class)
//				.block();
//		
//		List<AccountResponse> accounts = Arrays.asList(accountArr);
//		
//		for(AccountResponse accountResponse : accounts) {
//			System.out.println(accountResponse.getAccntNo() + ":" + accountResponse.getAccntName());
//		}
//	}
	
	public void deleteOrderByNo(String orderNo) {
		orderRepository.deleteByOrderNo(orderNo);
	}
	
	public OrderResponse getOrderByNo(String orderNo) {
		Order order = orderRepository.findByOrderNo(orderNo);
		
		return mapOrderToOrderResponse(order);
	}
	
	public List<OrderResponse> getOrderList(OrderRequest orderRequest) {
		List<Order> orderList = orderRepository.findAll();
		
		return orderList.stream().map(order -> OrderResponse.builder()
										.orderId(order.getOrderId())
										.orderNo(order.getOrderNo())
										.created(order.getCreated())
										.createdBy(order.getCreatedBy())
										.lastUpd(order.getLastUpd())
										.lastUpdBy(order.getLastUpdBy()).build()).toList();
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
							                .map(orderItemDto -> mapOrderItemRequestToOrderItem(orderItemDto, order))
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
	
	private String generateOrderNo() {
		String dateFor = DateTimeUtils.formatDate(AppConstant.ORDER_NO_FORMAT, DateTimeUtils.currentDate());
		Long accountNoSeq = orderRepository.getNextValOrderNoSequence(dateFor + "%");
		
		return dateFor + String.format("%04d", accountNoSeq);
	}
	
	private OrderResponse mapOrderToOrderResponse(Order order) {
		if(order == null) return new OrderResponse();
		
		return OrderResponse.builder().orderId(order.getOrderId())
							.orderNo(order.getOrderNo())
							.created(order.getCreated())
							.createdBy(order.getCreatedBy())
							.lastUpd(order.getLastUpd())
							.lastUpdBy(order.getLastUpdBy()).build();
	}
	
	private Order mapOrderItemRequestToOrder(OrderItemRequest orderItemRequest) {
		Order order = new Order();
		order.setOrderNo(generateOrderNo());
		order.setCreated(new Date());
		order.setCreatedBy(orderItemRequest.getCreateBy());
		order.setLastUpd(new Date());
		order.setLastUpdBy(orderItemRequest.getCreateBy());
		
		List<OrderItem> orderLineItems = new ArrayList<>();
		orderLineItems.add(mapOrderItemRequestToOrderItem(orderItemRequest, order));
		
		order.setItems(orderLineItems);
		
		return order;
	}
	
	private OrderItem mapOrderItemRequestToOrderItem(OrderItemRequest orderItemDto, Order order) {
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
	
	public OrderResponse createOrderFileWrapper(OrderItemFileWrapperRequest fileWrapperRequest) {
		return createOrder(fileWrapperRequest);
	}
	
	public MultiValueMap<String, HttpEntity<?>> fromFile(File file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));
        return builder.build();
    }
	
	public OrderResponse createOrder(OrderItemRequest orderItemRequest) {
		Order order = mapOrderItemRequestToOrder(orderItemRequest);
		
		orderRepository.save(order);
		
		if(orderItemRequest instanceof OrderItemFileWrapperRequest) {
			OrderItemFileWrapperRequest fileWrapperRequest = (OrderItemFileWrapperRequest) orderItemRequest;
			
			AccountFileWrapperRequest request = new AccountFileWrapperRequest();
			request.setAccountLevel(orderItemRequest.getAccountLevel());
			request.setAccountName(orderItemRequest.getAccountName());
			request.setTransactionBy(orderItemRequest.getCreateBy());
			request.setFile(fileWrapperRequest.getFile());
			
			MultipartBodyBuilder builder = new MultipartBodyBuilder();
			builder.part("accountName", orderItemRequest.getAccountName());
			builder.part("accountLevel", orderItemRequest.getAccountLevel());
			builder.part("transactionBy", orderItemRequest.getCreateBy());
			builder.part("file", fileWrapperRequest.getFile().getResource());
			
			webClientBuilder.build().post()
							.uri("http://account-service/api/account/create-account-upload-file")
							.contentType(MediaType.MULTIPART_FORM_DATA)
							.body(BodyInserters.fromMultipartData(builder.build()))
							.retrieve()
			                .bodyToMono(String.class)   
			                .block();
		}
		
		
//		List<AccountRequest> accountRequestList = order.getItems().stream()
//				.map(orderLineItem ->
//						AccountRequest.builder()
//						.accountLevel(orderLineItem.getAccountLevel())
//						.accountName(orderLineItem.getAccountName()).build()
//					).toList();
//		
//		AccountResponse[] accountArr = webClientBuilder.build().post()
//				.uri("http://account-service/api/account")
//				.body(Mono.just(accountRequestList), AccountRequest.class)
//				.retrieve()
//				.bodyToMono(AccountResponse[].class)
//				.block();
//		
//		List<AccountResponse> accounts = Arrays.asList(accountArr);
		
		return mapOrderToOrderResponse(order);
	}
}
