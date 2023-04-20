package com.imporve.skill.transactionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.imporve.skill.transactionservice.model.AccountBalanceTransaction;

public interface AccountBalanceTransactionRepository extends JpaRepository<AccountBalanceTransaction, Integer>{
	@Query(value = "SELECT ISNULL(max(RIGHT(transactionNo, 4)) + 1, 1) transactionNo from AccountBalanceTransaction")
	Long getNextValOrderNoSequence();
}
