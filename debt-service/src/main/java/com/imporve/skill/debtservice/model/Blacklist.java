package com.imporve.skill.debtservice.model;

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
@Table(name = "TEST_BLACKLIST", schema = "debt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blacklist {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer blacklistId;
	private String baNo;
	private String statusCd;
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
}
