package com.georgeisaev.vietnamenterprisetinstorage.controllers.rest.enterprise;

import com.georgeisaev.vietnamenterprisetinstorage.domain.enterprise.Enterprise;
import com.georgeisaev.vietnamenterprisetinstorage.services.enterprise.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(
		"enterprises")
public class EnterpriseRestController {

	private final EnterpriseService enterpriseService;

	@Autowired
	public EnterpriseRestController(EnterpriseService enterpriseService) {
		this.enterpriseService = enterpriseService;
	}

	@GetMapping("/{id}")
	public Mono<Enterprise> findById(@PathVariable String id) {
		return enterpriseService.findById(id);
	}

}
