package com.georgeisaev.vietnam.enterprise.tin.storage.services.parsing;


import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import com.georgeisaev.vietnam.enterprise.tin.storage.parsers.MinistryPlanningInvestmentSiteParser;
import com.georgeisaev.vietnam.enterprise.tin.storage.services.enterprise.EnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

@Slf4j
@Service
public class ParserService {

	private final EnterpriseService enterpriseService;
	private final ForkJoinPool pool;
	private final Set<String> toParse;

	@Autowired
	public ParserService(EnterpriseService enterpriseService) {
		this.enterpriseService = enterpriseService;
		pool = ForkJoinPool.commonPool();
		toParse = new ConcurrentSkipListSet<>();
	}

	/**
	 * @param request
	 * @return
	 * @throws ConnectException
	 */
	public void process(String request) throws ConnectException {
		if (toParse.contains(request)) {
			throw new ConnectException("");
		}
		pool.execute(new ParsingAction(request));
	}

	class ParsingAction extends RecursiveAction {

		private final String request;

		ParsingAction(String request) {
			this.request = request;
		}

		@Override
		protected void compute() {
			try {
				List<Enterprise> result = new MinistryPlanningInvestmentSiteParser(request).parse();
				SavingAction savingAction = new SavingAction(request, result);
				savingAction.fork();
			} catch (IOException e) {
				log.error("Cannot parse request: " + request, e);
				toParse.remove(request);
				join();
			}
		}

	}

	class SavingAction extends RecursiveAction {

		private final String request;
		private final List<Enterprise> enterprises;

		public SavingAction(String request, List<Enterprise> enterprises) {
			this.request = request;
			this.enterprises = enterprises;
		}

		@Override
		protected void compute() {
			try {
				enterpriseService.saveAll(enterprises);
			} catch (Exception e) {
				log.error("Cannot save enterprises", e);
			}
			toParse.remove(request);
			join();
		}

	}

}



