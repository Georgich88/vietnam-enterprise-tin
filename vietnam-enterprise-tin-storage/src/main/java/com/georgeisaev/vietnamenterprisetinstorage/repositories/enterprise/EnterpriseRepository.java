package com.georgeisaev.vietnamenterprisetinstorage.repositories.enterprise;

import com.georgeisaev.vietnamenterprisetinstorage.domain.enterprise.Enterprise;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface EnterpriseRepository extends ReactiveCassandraRepository<Enterprise, String> {

}
