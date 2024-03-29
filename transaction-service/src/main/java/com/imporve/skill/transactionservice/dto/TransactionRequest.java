package com.imporve.skill.transactionservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
	private String accountNo;
	private String destBank;
	private String destAccountNo;
	private BigDecimal amount;
	private String userName;
}
