package com.georgeisaev.vietnam.enterprise.tin.storage.parsers;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.InformationSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Provides parsing mechanics for <a href="https://dichvuthongtin.dkkd.gov.vn">Ministry portal</a>
 *
 * @author Georgy Isaev
 */
@Slf4j
@AllArgsConstructor
public class MinistryPlanningInvestmentSiteParser implements EnterpriseParser {

	private static final String URL_PATH_DEFAULT_PAGE = "https://dichvuthongtin.dkkd.gov.vn/inf/default.aspx";
	private static final String MSG_ERR_PARSING = "Error occurred during the parsing";

	// region HTML-field ids

	public static final String HTML_ELEMENT_SEARCH_TABLE = "ctl00_C_UC_ENT_LIST1_CtlList";
	public static final String HTML_ELEMENT_SEARCH_FIELD = "ctl00_FldSearch";
	public static final String HTML_ELEMENT_ENTERPRISE_TIN_FLD = "ctl00_C_ENTERPRISE_GDT_CODEFld";
	public static final String HTML_ELEMENT_ENTERPRISE_NAME_FLD = "ctl00_C_NAMEFld";
	public static final String HTML_ELEMENT_ENTERPRISE_FOREIGN_NAME_FLD = "ctl00_C_NAME_FFld";
	public static final String HTML_ELEMENT_ENTERPRISE_SHORT_NAME_FLD = "ctl00_C_SHORT_NAMEFld";
	public static final String HTML_ELEMENT_ENTERPRISE_STATUS_FLD = "ctl00_C_STATUSNAMEFld";
	public static final String HTML_ELEMENT_ENTERPRISE_TYPE_FLD = "ctl00_C_ENTERPRISE_TYPEFld";
	public static final String HTML_ELEMENT_ENTERPRISE_ADDRESS_FLD = "ctl00_C_HO_ADDRESS";
	public static final String HTML_ELEMENT_ANCHOR = "a";
	public static final String HTML_ELEMENT_TABLE_DATA_CELL = "td";
	public static final String HTML_XPATH_PAGER = "//tr[@class='Pager']/td/table";

	// endregion

	private final String searchRequest;

	// region Parsing logic

	/**
	 * Parses an enterprise using {@code searchRequest}.
	 *
	 * @return the parsed enterprise
	 * @throws IOException
	 */
	@Override
	public List<Enterprise> parse() throws IOException {

		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {

			webClient.setAjaxController(new NicelyResynchronizingAjaxController());

			// Preparing search form
			HtmlPage searchResultPage = searchResults(webClient);

			// Processing search results
			return extractEnterprises(searchResultPage);

		} catch (Exception e) {
			log.error(MSG_ERR_PARSING, e);
		}

		return Collections.emptyList();
	}

	private HtmlPage searchResults(WebClient webClient) throws IOException {
		final HtmlPage homePage = ((HtmlPage) webClient.getPage(URL_PATH_DEFAULT_PAGE)) // opening home page
				.getHtmlElementById("ctl00_ln_en").click(); // switching language to English
		HtmlTextInput searchRequestInputElement = homePage.getHtmlElementById(HTML_ELEMENT_SEARCH_FIELD);
		searchRequestInputElement.setValueAttribute(searchRequest);
		HtmlForm searchForm = homePage.getForms().get(0);
		HtmlSubmitInput submit = searchForm.getOneHtmlElementByAttribute("input", "type", "submit");
		return submit.click();
	}


	private static List<Enterprise> extractEnterprises(HtmlPage searchResultPage) throws IOException {
		final List<Enterprise> enterprises = extractEnterprisesSinglePage(searchResultPage);
		//
		enterprises.addAll(walkThroughSearchPages(searchResultPage));
		return enterprises;
	}

	private static List<Enterprise> extractEnterprisesSinglePage(HtmlPage searchResultPage) {
		final List<Enterprise> enterprises = new ArrayList<>();
		try {
			final HtmlTable searchTable = searchResultPage.getHtmlElementById(HTML_ELEMENT_SEARCH_TABLE);
			final WebClient webClient = searchResultPage.getWebClient();
			List<HtmlTableRow> searchTableRows = searchTable.getRows();
			for (int i = 1; i < searchTableRows.size(); i++) {
				HtmlTableRow resultRow = searchTableRows.get(i);
				// Clicking on the fist cell to get an enterprise homePage
				if (!resultRow.getCells().isEmpty()) {
					List<HtmlAnchor> anchors = resultRow.getCells().get(0).getByXPath(HTML_ELEMENT_ANCHOR);
					if (!anchors.isEmpty()) {
						HtmlPage enterprisePage = anchors.get(0).click();
						Optional<Enterprise> foundEnterprise = parseEnterprisePage(enterprisePage);
						foundEnterprise.ifPresent(enterprises::add);
						webClient.getCurrentWindow().getHistory().back();
					}
				}
			}
		} catch (Exception e) {
			log.error(MSG_ERR_PARSING, e);
		}
		return enterprises;
	}

	/**
	 * Extract enterprises from search result pages, pagination element is on the last row of search table.
	 *
	 * <pre>
	 * {@code
	 * <tr class="Pager">
	 * <td colspan="4">
	 *         <table border="0">
	 *             <tr>
	 *                 <!-- ... -->
	 *             </tr>
	 *         </table>
	 *     </td>
	 * </tr>}
	 * </pre>
	 *
	 * @param searchPage a search result page
	 * @return a found list enterprises
	 * @throws IOException if pages cannot be processed
	 */
	private static List<Enterprise> walkThroughSearchPages(HtmlPage searchPage) throws IOException {
		final List<Enterprise> enterprises = new ArrayList<>();
		final List<HtmlTable> pager = searchPage.getByXPath(HTML_XPATH_PAGER);
		if (!pager.isEmpty() && !pager.get(0).getRows().isEmpty()) {
			final List<HtmlTableDataCell> pages = pager
					.get(0).getRows()
					.get(0).getByXPath(HTML_ELEMENT_TABLE_DATA_CELL);
			HtmlTableDataCell currentPage = null;
			for (var page : pages) {
				final List<HtmlAnchor> anchors = page.getByXPath(HTML_ELEMENT_ANCHOR);
				if (currentPage == null && anchors.isEmpty()) {
					currentPage = page;
				} else if (currentPage != null && !anchors.isEmpty()) {
					HtmlPage nextPage = anchors.get(0).click();
					enterprises.addAll(extractEnterprisesSinglePage(nextPage));
					walkThroughSearchPages(nextPage);
				}
			}
		}
		return enterprises;
	}

	private static Optional<Enterprise> parseEnterprisePage(HtmlPage page) {
		try {
			Enterprise enterprise = new Enterprise();
			enterprise.setTin(page.getHtmlElementById(HTML_ELEMENT_ENTERPRISE_TIN_FLD).asText());
			enterprise.setName(page.getHtmlElementById(HTML_ELEMENT_ENTERPRISE_NAME_FLD).asText());
			enterprise.setForeignName(page.getHtmlElementById(HTML_ELEMENT_ENTERPRISE_FOREIGN_NAME_FLD).asText());
			enterprise.setShortName(page.getHtmlElementById(HTML_ELEMENT_ENTERPRISE_SHORT_NAME_FLD).asText());
			enterprise.setStatus(page.getHtmlElementById(HTML_ELEMENT_ENTERPRISE_STATUS_FLD).asText());
			enterprise.setType(page.getHtmlElementById(HTML_ELEMENT_ENTERPRISE_TYPE_FLD).asText());
			// TODO: Representative
			enterprise.setLegalAddress(page.getHtmlElementById(HTML_ELEMENT_ENTERPRISE_ADDRESS_FLD).asText());
			enterprise.setSource(InformationSource.NATIONAL_BUSINESS_REGISTRATION_PORTAL.name());
			return Optional.of(enterprise);
		} catch (Exception e) {
			log.error(MSG_ERR_PARSING, e);
			return Optional.empty();
		}
	}

	// endregion

	public static void main(String[] args) throws IOException {
		for (String tin : args) {
			var enterprises = new MinistryPlanningInvestmentSiteParser(tin).parse();
			log.info(enterprises.toString());
		}

	}

}
