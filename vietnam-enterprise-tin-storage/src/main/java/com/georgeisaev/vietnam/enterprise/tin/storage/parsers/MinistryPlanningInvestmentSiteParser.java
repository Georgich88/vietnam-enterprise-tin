package com.georgeisaev.vietnam.enterprise.tin.storage.parsers;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class MinistryPlanningInvestmentSiteParser {

	private final String searchRequest;

	public List<Enterprise> parse() throws IOException {

		try (final WebClient webClient = new WebClient()) {

			webClient.setAjaxController(new NicelyResynchronizingAjaxController());

			// Preparing search form
			HtmlPage searchResultPage = searchResults(webClient);

			// Processing search results
			return extractEnterprises(searchResultPage);

		} catch (Exception e) {
			log.error("Error occurred during the parsing", e);
		}

		return Collections.emptyList();
	}

	private HtmlPage searchResults(WebClient webClient) throws IOException {
		final HtmlPage homePage = webClient.getPage("https://dichvuthongtin.dkkd.gov.vn/inf/default.aspx");
		HtmlTextInput searchRequestInputElement = homePage.getHtmlElementById("ctl00_FldSearch");
		searchRequestInputElement.setValueAttribute(searchRequest);
		HtmlForm searchForm = homePage.getForms().get(0);
		HtmlSubmitInput submit = searchForm.getOneHtmlElementByAttribute("input", "type", "submit");
		return submit.click();
	}

	private List<Enterprise> extractEnterprises(HtmlPage searchResultPage) throws IOException {
		final List<Enterprise> enterprises = new ArrayList<>();
		searchResultPage.getForms();
		final HtmlTable searchTable = searchResultPage.getHtmlElementById("ctl00_C_UC_ENT_LIST1_CtlList");
		List<HtmlTableRow> searchTableRows = searchTable.getRows();
		for (int i = 1; i < searchTableRows.size(); i++) {
			HtmlTableRow resultRow = searchTableRows.get(i);
			// Clicking on the first cell to get an enterprise homePage
			if (!resultRow.getCells().isEmpty()) {
				List<HtmlAnchor> anchors = resultRow.getCell(0).getByXPath("a");
				HtmlPage enterprisePage = anchors.get(0).click();
				Optional<Enterprise> foundEnterprise = parseEnterprisePage(enterprisePage);
				foundEnterprise.ifPresent(enterprises::add);
			}
		}
		return enterprises;
	}

	private Optional<Enterprise> parseEnterprisePage(HtmlPage page) {
		try {
			Enterprise enterprise = new Enterprise();
			enterprise.setTin(page.getHtmlElementById("ctl00_C_ENTERPRISE_GDT_CODEFld").asText());
			enterprise.setName(page.getHtmlElementById("ctl00_C_NAMEFld").asText());
			enterprise.setForeignName(page.getHtmlElementById("ctl00_C_NAME_FFld").asText());
			enterprise.setShortName(page.getHtmlElementById("ctl00_C_SHORT_NAMEFld").asText());
			enterprise.setStatus(page.getHtmlElementById("ctl00_C_STATUSNAMEFld").asText());
			enterprise.setType(page.getHtmlElementById("ctl00_C_ENTERPRISE_TYPEFld").asText());
			// TODO: Representative
			enterprise.setLegalAddress(page.getHtmlElementById("ctl00_C_HO_ADDRESS").asText());
			return Optional.of(enterprise);
		} catch (Exception e) {
			log.error("Error occurred during the parsing", e);
			return Optional.empty();
		}
	}

	public static void main(String[] args) throws IOException {
		var enterprises = new MinistryPlanningInvestmentSiteParser("0107425566").parse();
		log.info(enterprises.toString());
	}

}
