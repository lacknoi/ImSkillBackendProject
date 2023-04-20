package com.imporve.skill.orderservice.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.imporve.skill.orderservice.AppConstant;
import com.imporve.skill.orderservice.dto.OrderFileWrapperRequest;
import com.imporve.skill.orderservice.dto.OrderRequest;
import com.imporve.skill.orderservice.dto.OrderResponse;
import com.imporve.skill.orderservice.model.Order;
import com.imporve.skill.orderservice.model.OrderAttachFile;
import com.imporve.skill.orderservice.repository.ConfigRepository;
import com.imporve.skill.orderservice.repository.OrderRepository;
import com.imporve.skill.orderservice.utils.DateTimeUtils;
import com.imporve.skill.orderservice.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	private final WebClient.Builder webClientBuilder;
	private final OrderRepository orderRepository;
	private final ConfigRepository configRepository;
	
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
		
		return orderList.stream().map(order -> mapOrderToOrderResponse(order)).toList();
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
							.status(order.getStatusCd())
							.created(order.getCreated())
							.createdBy(order.getCreatedBy())
							.lastUpd(order.getLastUpd())
							.lastUpdBy(order.getLastUpdBy()).build();
	}
	
	private Order mapOrderRequestToOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNo(generateOrderNo());
		order.setCreated(new Date());
		order.setCreatedBy(orderRequest.getUserName());
		order.setLastUpd(new Date());
		order.setLastUpdBy(orderRequest.getUserName());
		order.setOrderType(orderRequest.getOrderType());
		order.setStatusCd(AppConstant.STATUS_OPEN);
		order.setAccountName(orderRequest.getAccountName());
		
		if(orderRequest instanceof OrderFileWrapperRequest) {
			List<OrderAttachFile> attachFileList = new ArrayList<>();
			
			OrderFileWrapperRequest fileWrapperRequest = (OrderFileWrapperRequest) orderRequest;
			
			OrderAttachFile attachFile = new OrderAttachFile();
			attachFile.setOrder(order);
			attachFile.setFileName(order.getOrderNo() + "_" + fileWrapperRequest.getFile().getOriginalFilename());
			attachFile.setOriginalFileName(fileWrapperRequest.getFile().getOriginalFilename());
			attachFile.setFileType(fileWrapperRequest.getFile().getContentType());
			attachFile.setCreated(new Date());
			attachFile.setCreatedBy(order.getLastUpdBy());
			attachFile.setLastUpd(new Date());
			attachFile.setLastUpdBy(order.getLastUpdBy());
			
			attachFileList.add(attachFile);
			
			order.setItems(attachFileList);
		}
		
		return order;
	}
	
	public OrderResponse requestCreateAccount(OrderFileWrapperRequest fileWrapperRequest) throws IOException {
		fileWrapperRequest.setOrderType(AppConstant.ORDER_TYPE_REGISTER_ACCOUNT);
		
		OrderResponse orderResponse = createOrder(fileWrapperRequest);
		
		if(fileWrapperRequest.getFile() != null) {
			String path = configRepository.getPath("ORDER_ATTACH_FILE");
			String fileName = orderResponse.getOrderNo() + "_" + fileWrapperRequest.getFile().getOriginalFilename();
			
			FileUtils.saveFile(fileName, path, fileWrapperRequest.getFile());
		}
		
		return orderResponse;
	}
	
	public MultiValueMap<String, HttpEntity<?>> fromFile(File file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));
        return builder.build();
    }
	
	public void approveOrder(OrderRequest orderRequest) throws IOException {
		Order order = orderRepository.findByOrderNo(orderRequest.getOrderNo());
		
		if(StringUtils.equalsIgnoreCase(order.getOrderType(), AppConstant.ORDER_TYPE_REGISTER_ACCOUNT)) {
			MultipartBodyBuilder builder = new MultipartBodyBuilder();
			builder.part("accountName", order.getAccountName());
			builder.part("transactionBy", order.getLastUpdBy());
			

			if(order.getItems() != null && !order.getItems().isEmpty()) {
				String path = configRepository.getPath("ORDER_ATTACH_FILE");
				
				Resource file = FileUtils.getResourceFromFileName(path, order.getItems().get(0).getFileName());
				
				builder.part("file", file);
			}
			
			webClientBuilder.build().post()
						.uri("http://account-service/api/account/create-account-upload-file")
						.contentType(MediaType.MULTIPART_FORM_DATA)
						.body(BodyInserters.fromMultipartData(builder.build()))
						.retrieve()
			            .bodyToMono(String.class)   
			            .block();
		}
	}
	
	public OrderResponse createOrder(OrderRequest orderRequest) {
		Order order = mapOrderRequestToOrder(orderRequest);
		
		orderRepository.save(order);
		
		return mapOrderToOrderResponse(order);
	}
	
	public OrderResponse updateOrder(OrderRequest orderRequest) {
		Order order = orderRepository.findByOrderNo(orderRequest.getOrderNo());
		
		order.setAccountName(orderRequest.getAccountName());
		order.setLastUpdBy(orderRequest.getUserName());
		order.setLastUpd(new Date());
		
		orderRepository.save(order);
		
		return mapOrderToOrderResponse(order);
	}
}
