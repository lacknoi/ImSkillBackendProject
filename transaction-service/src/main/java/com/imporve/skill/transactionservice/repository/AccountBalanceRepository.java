package com.imporve.skill.transactionservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imporve.skill.transactionservice.model.AccountBalance;


public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Integer> {

}
