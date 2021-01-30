package com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table("ent_enterprises")
public class Enterprise implements Serializable {

	// region Fields

	@Id
	@PrimaryKey
	@Column("tin")
	private String tin;
	@Column("name")
	private String name;
	@Column("foreign_name")
	private String foreignName;
	@Column("short_name")
	private String shortName;
	@Column("status")
	private String status;
	@Column("type")
	private String type;
	@Column("foundation_date")
	private LocalDate foundationDate;
	@Column("representative")
	private String representative;
	@Column("legal_address")
	private String legalAddress;
	@Column("creation_date")
	private LocalDateTime creationDate;
	@Column("source")
	@CassandraType(type = CassandraType.Name.TEXT)
	private InformationSource source;

	// endregion

	public void prePersist() {
		if (creationDate == null) {
			creationDate = LocalDateTime.now();
		}
	}

	// region Getters and setters

	public String getSource() {
		return source == null ? null : source.name();
	}

	public void setSource(String source) {
		this.source = InformationSource.valueOf(source);
	}

	// endregion

}
