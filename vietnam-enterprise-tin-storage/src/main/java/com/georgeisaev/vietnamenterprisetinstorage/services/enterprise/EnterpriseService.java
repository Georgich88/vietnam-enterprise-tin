package com.georgeisaev.vietnamenterprisetinstorage.services.enterprise;

import com.georgeisaev.vietnamenterprisetinstorage.domain.enterprise.Enterprise;
import com.georgeisaev.vietnamenterprisetinstorage.repositories.enterprise.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EnterpriseService {

	private final EnterpriseRepository enterpriseRepository;

	@Autowired
	public EnterpriseService(EnterpriseRepository enterpriseRepository) {
		this.enterpriseRepository = enterpriseRepository;
	}

	public Mono<Enterprise> findById(String id) {
		return enterpriseRepository.findById(id);
	}

}
