package com.imporve.skill.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
	private String accountName;
	private String userName;
	private String orderType;
	private String orderNo;
}
