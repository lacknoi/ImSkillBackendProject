package com.imporve.skill.debtservice.model;

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
@Table(name = "TEST_ACCOUNT_BALANCE", schema = "debt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalance {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountBalanceId;
	private String baNo;
	private BigDecimal invoiceTotalBalance;
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
}
