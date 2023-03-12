package com.imporve.skill.debtservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imporve.skill.debtservice.model.AccountBalance;


public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {
}
