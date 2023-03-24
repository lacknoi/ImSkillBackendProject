package com.imporve.skill.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
	private String accountName;
	private String accountLevel;
	private String accountNo;
	private String createBy;
}
