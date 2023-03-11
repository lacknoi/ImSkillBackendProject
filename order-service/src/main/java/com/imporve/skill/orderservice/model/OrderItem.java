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
@Table(name = "TEST_ORDER_ITEM", schema = "debt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;
	@ManyToOne
	@JoinColumn(name="order_id", nullable=false)
    private Order order;
    private Integer accountId;
	private String accountName;
	private String accountLevel;
	private Date created;
	private String createdBy;
	private Date lastUpd;
	private String lastUpdBy;
}
