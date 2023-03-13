package com.imporve.skill.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
	private String accountName;
	private String accountLevel;
	private String accountNo;
}
