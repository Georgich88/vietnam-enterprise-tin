package com.georgeisaev.vietnamenterprisetinstorage.domain.enterprise;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Table
@Data
@NoArgsConstructor
public class Enterprise {

	@PrimaryKey
	private String tin;
	private String name;
	private String foreignName;
	private String shortName;
	private String status;
	private String type;
	private Date foundationDate;
	private String representative;
	private String legalAddress;

}
