package com.georgeisaev.vietnam.enterprise.tin.storage.services.enterprise;

import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import com.georgeisaev.vietnam.enterprise.tin.storage.repositories.enterprise.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnterpriseService {

	private final EnterpriseRepository enterpriseRepository;

	// region Constructors

	@Autowired
	public EnterpriseService(EnterpriseRepository enterpriseRepository) {
		this.enterpriseRepository = enterpriseRepository;
	}

	// endregion

	// region Creation and updating

	@SuppressWarnings("UnusedReturnValue")
	public Enterprise save(Enterprise enterprise) {
		return enterpriseRepository.save(enterprise);
	}

	public List<Enterprise> saveAll(List<Enterprise> enterprises) {
		enterprises.forEach(Enterprise::prePersist);
		return enterpriseRepository.saveAll(enterprises);
	}

	// endregion

	// region Reading

	public Optional<Enterprise> findById(String id) {
		return enterpriseRepository.findById(id);
	}

	// TODO: add filter
	public List<Enterprise> findAll(String value) {
		return enterpriseRepository.findAll();
	}

	// endregion

	// region Deletion

	public void delete(Enterprise enterprise) {
		enterpriseRepository.delete(enterprise);
	}

	// endregion

	public long count() {
		return enterpriseRepository.count();
	}

}
