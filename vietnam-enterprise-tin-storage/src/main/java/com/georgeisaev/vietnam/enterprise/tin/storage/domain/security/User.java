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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("usr_users")
public class User {

	@Id
	@PrimaryKey
	@Column("user_id")
	private int id;
	@Column("user_name")
	@Length(min = 5, message = "*Your user name must have at least 5 characters")
	@NotEmpty(message = "*Please provide a user name")
	private String userName;
	@Column("email")
	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	private String email;
	@Column("password")
	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	private String password;
	@Column("name")
	@NotEmpty(message = "*Please provide your name")
	private String name;
	@Column("last_name")
	@NotEmpty(message = "*Please provide your last name")
	private String lastName;
	@Column("active")
	private Boolean active;
//	@ManyToMany(cascade = CascadeType.MERGE)
//	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//	private Set<Role> roles;

}
