package com.georgeisaev.vietnam.enterprise.tin.storage.controllers.rest.enterprise;

import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import com.georgeisaev.vietnam.enterprise.tin.storage.services.enterprise.EnterpriseService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("enterprises")
public class EnterpriseRestController {

	private final EnterpriseService enterpriseService;

	@Autowired
	public EnterpriseRestController(EnterpriseService enterpriseService) {
		this.enterpriseService = enterpriseService;
	}

	@GetMapping("/{id}")
	public Optional<Enterprise> findById(@PathVariable String id) {
		return enterpriseService.findById(id);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public List<Enterprise> saveAll(@RequestBody List<Enterprise> enterprises) {
		return enterpriseService.saveAll(enterprises);
	}

}
