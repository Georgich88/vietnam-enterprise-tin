package com.georgeisaev.vietnam.enterprise.tin.storage.enterprise;

import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import com.georgeisaev.vietnam.enterprise.tin.storage.services.enterprise.EnterpriseService;
import com.georgeisaev.vietnam.enterprise.tin.storage.services.parsing.ParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("enterprises")
public class EnterpriseRestController {

	private final EnterpriseService enterpriseService;
	private final ParserService parserService;

	@Autowired
	public EnterpriseRestController(EnterpriseService enterpriseService, ParserService parserService) {
		this.enterpriseService = enterpriseService;
		this.parserService = parserService;
	}

	@GetMapping("/{id}")
	public Optional<Enterprise> findById(@PathVariable String id) {
		return enterpriseService.findById(id);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public List<Enterprise> saveAll(@RequestBody List<Enterprise> enterprises) {
		return enterpriseService.saveAll(enterprises);
	}

	@GetMapping(value = "/parse/{id}", produces = APPLICATION_JSON_VALUE)
	public Optional<Enterprise> parse(@PathVariable String id) throws ConnectException {
		parserService.process(id);
		return Optional.empty();
	}

}
