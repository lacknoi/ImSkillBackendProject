package com.imporve.skill.accountservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.imporve.skill.accountservice.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByAccountLevelAndAccountNo(String accountLevel, String accountNo);
	Account findByAccountNo(String accountNo);
	
	@Query("select ba from Account ba inner join Account ca on ca.accountId = ba.masterId where ca.accountNo = :masterNo")
	List<Account> findByMasterNo(String masterNo);
	
	@Query(value = "SELECT ISNULL(max(RIGHT(accountNo, 4)) + 1, 1) account_no from Account where accountNo like :accountNo")
	Long getNextValAccountNoSequence(String accountNo);
}
