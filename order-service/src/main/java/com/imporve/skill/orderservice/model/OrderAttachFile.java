package com.imporve.skill.orderservice.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "debt", name = "TEST_ORDER_ATTACH_FILE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAttachFile {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderAttachFileId;
	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	private String fileName;
	private String originalFileName;
	private String fileType;
	
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
}
