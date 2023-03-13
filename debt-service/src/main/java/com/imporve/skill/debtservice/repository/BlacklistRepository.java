package com.imporve.skill.debtservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imporve.skill.debtservice.model.Blacklist;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
	Blacklist findByBaNo(String baNo);
}
