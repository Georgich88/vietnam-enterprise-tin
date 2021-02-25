package com.georgeisaev.vietnam.enterprise.tin.storage.repositories.enterprise;

import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnterpriseRepository extends CassandraRepository<Enterprise, String> {

}