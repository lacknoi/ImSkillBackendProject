package com.imporve.skill.orderservice.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
	private Integer orderId;
	private String orderNo;
	private String status;
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
}
