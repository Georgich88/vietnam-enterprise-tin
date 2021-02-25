package com.georgeisaev.vietnam.enterprise.tin.storage.repositories.security;

import com.georgeisaev.vietnam.enterprise.tin.storage.security.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CassandraRepository<User, String> {

	User findByUsername(String username);

}
