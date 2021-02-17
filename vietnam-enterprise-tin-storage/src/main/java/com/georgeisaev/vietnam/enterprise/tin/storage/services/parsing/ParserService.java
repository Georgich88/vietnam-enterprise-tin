package com.georgeisaev.vietnam.enterprise.tin.storage.services.parsing;


import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import com.georgeisaev.vietnam.enterprise.tin.storage.parsers.MinistryPlanningInvestmentSiteParser;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ParserService {

	private final ExecutorService pool;
	public final Set<String> toParse;

	public ParserService() {
		pool = Executors.newCachedThreadPool();
		toParse = new ConcurrentSkipListSet<>();
	}

	public List<Enterprise> process(String request) throws ConnectException, ExecutionException, InterruptedException {
		if (toParse.contains(request)) {
			throw new ConnectException("");
		}
		return pool.submit(new ParsingTask(request)).get();
	}

	class ParsingTask implements Callable<List<Enterprise>> {

		private final String request;

		ParsingTask(String request) {
			this.request = request;
		}

		@Override
		public List<Enterprise> call() throws Exception {
			List<Enterprise> result = new MinistryPlanningInvestmentSiteParser(request).parse();
			toParse.remove(request);
			return result;
		}

	}

}



