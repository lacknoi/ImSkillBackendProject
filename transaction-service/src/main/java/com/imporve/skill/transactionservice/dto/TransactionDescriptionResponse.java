package com.imporve.skill.transactionservice.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDescriptionResponse {
	private String transactionNo;
	private String transactionType;
	private BigDecimal requestAmount;
	private BigDecimal totalAmount;
	private Date transactionDate;
	private String transactionBy;
}
