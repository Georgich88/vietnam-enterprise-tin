package com.georgeisaev.vietnam.enterprise.tin.storage.parsers;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class MinistryPlanningInvestmentSiteParser {

	private final String searchRequest;

	public List<Enterprise> parse() throws IOException {
		try (final WebClient webClient = new WebClient()) {
			final HtmlPage page = webClient.getPage("https://dichvuthongtin.dkkd.gov.vn/inf/default.aspx");
			HtmlTextInput messageText = page.getHtmlElementById("ctl00_FldSearch");
			messageText.setValueAttribute(searchRequest);
			HtmlForm form = page.getForms().get(0);
			HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
			HtmlPage newPage = submit.click();
			newPage.getForms();
		}
		return Collections.emptyList();
	}


	public static void main(String[] args) throws IOException {
		var enterprises = new MinistryPlanningInvestmentSiteParser("1C Vietnam").parse();
	}

}
