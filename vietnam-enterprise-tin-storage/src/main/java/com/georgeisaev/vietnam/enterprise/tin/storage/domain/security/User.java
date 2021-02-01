package com.georgeisaev.vietnam.enterprise.tin.storage.domain.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("usr_users")
public class User implements Serializable {

	@Id
	@PrimaryKey
	@Column("username")
	@Email
	@NotEmpty
	private String username;
	@Column("password")
	@Length(min = 5)
	@NotEmpty
	private String password;

}
