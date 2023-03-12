package com.imporve.skill.debtservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtRequest {
	private String baNo;
	private String transactionBy;
}
