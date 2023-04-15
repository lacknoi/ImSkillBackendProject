package com.imporve.skill.orderservice.model;

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
@Table(schema = "debt", name = "TEST_CONFIG")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer configId;
	private String type;
	private String keyword;
	private String value;
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
}
