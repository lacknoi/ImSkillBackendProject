package com.imporve.skill.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
	private String accountName;
	private String accountLevel;
	private String transactionBy;
}
