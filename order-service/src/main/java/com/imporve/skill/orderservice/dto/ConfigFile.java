package com.imporve.skill.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigFile {
	private Integer maxPerFile;
	private String path;
	private String fileName;
	private String formatDate;
}
