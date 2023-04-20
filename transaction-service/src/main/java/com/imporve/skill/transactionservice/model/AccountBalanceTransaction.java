package com.imporve.skill.transactionservice.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "debt", name = "TEST_ACCOUNT_BALANCE_TRANSACTION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transactionId;
	private String accountNo;
	private String actionType;
	private String transactionNo;
	private BigDecimal requestAmount;
	private BigDecimal totalBalance;
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
}
