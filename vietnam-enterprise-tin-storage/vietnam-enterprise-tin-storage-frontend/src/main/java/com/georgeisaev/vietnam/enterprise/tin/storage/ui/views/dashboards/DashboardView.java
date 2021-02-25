package com.georgeisaev.vietnam.enterprise.tin.storage.ui.views.dashboards;

import com.georgeisaev.vietnam.enterprise.tin.storage.services.enterprise.EnterpriseService;
import com.georgeisaev.vietnam.enterprise.tin.storage.ui.MainLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Dashboard | Vaadin CRM")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

	private final EnterpriseService enterpriseService;

	public DashboardView(EnterpriseService enterpriseService) {
		this.enterpriseService = enterpriseService;
		addClassName("dashboard-view");
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		add(getEnterpriseStats());
	}

	private Span getEnterpriseStats() {
		Span stats = new Span(enterpriseService.count() + " enterprises");
		stats.addClassName("enterprise-stats");

		return stats;
	}


}
