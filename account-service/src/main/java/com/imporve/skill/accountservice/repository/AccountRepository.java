package com.imporve.skill.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imporve.skill.accountservice.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
