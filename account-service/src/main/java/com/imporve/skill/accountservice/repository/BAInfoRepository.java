package com.imporve.skill.accountservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imporve.skill.accountservice.model.BAInfo;

public interface BAInfoRepository extends JpaRepository<BAInfo, Long> {
	BAInfo findByBaNo(String baNo);
	List<BAInfo> findByBaNoIn(List<String> baNo);
	BAInfo findByCaNo(String caNo);
}
