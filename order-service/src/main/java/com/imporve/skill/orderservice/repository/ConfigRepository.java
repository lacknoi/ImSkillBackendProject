package com.imporve.skill.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.imporve.skill.orderservice.model.Config;

public interface ConfigRepository extends JpaRepository<Config, Integer>{
	@Query(value = "Select value from Config where type = 'PATH' and keyword = :keyword")
	String getPath(String keyword);
}
