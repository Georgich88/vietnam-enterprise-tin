package com.georgeisaev.vietnam.enterprise.tin.storage.ui.views.enterprise;

import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import com.georgeisaev.vietnam.enterprise.tin.storage.services.enterprise.EnterpriseService;
import com.georgeisaev.vietnam.enterprise.tin.storage.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.klaudeta.PaginatedGrid;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Enterprises | Vietnam TIN")
public class EnterpriseList extends VerticalLayout {

	private final EnterpriseForm form;
	private final PaginatedGrid<Enterprise> grid = new PaginatedGrid<>(Enterprise.class);
	private final TextField filterText = new TextField();

	private final transient EnterpriseService enterpriseService;

	public EnterpriseList(EnterpriseService enterpriseService) {

		this.enterpriseService = enterpriseService;
		addClassName("list-view");
		setSizeFull();
		configureGrid();

		form = new EnterpriseForm();
		form.addListener(EnterpriseForm.SaveEvent.class, this::saveEnterprise);
		form.addListener(EnterpriseForm.DeleteEvent.class, this::deleteEnterprise);
		form.addListener(EnterpriseForm.CloseEvent.class, e -> closeEditor());

		Div content = new Div(grid, form);
		content.addClassName("content");
		content.setSizeFull();

		add(getToolBar(), content);
		updateList();
		closeEditor();
	}

	private void deleteEnterprise(EnterpriseForm.DeleteEvent evt) {
		enterpriseService.delete(evt.getEnterprise());
		updateList();
		closeEditor();
	}

	private void saveEnterprise(EnterpriseForm.SaveEvent evt) {
		enterpriseService.save(evt.getEnterprise());
		updateList();
		closeEditor();
	}

	private HorizontalLayout getToolBar() {

		filterText.setPlaceholder("Filter by TIN...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList());

		Button addEnterpriseButton = new Button("Add enterprise", click -> addEnterprise());
		HorizontalLayout toolbar = new HorizontalLayout(filterText, addEnterpriseButton);
		toolbar.addClassName("toolbar");

		return toolbar;

	}

	private void addEnterprise() {
		grid.asSingleSelect().clear();
		editEnterprise(new Enterprise());
	}

	private void configureGrid() {
		grid.addClassName("enterprise-grid");
		grid.setSizeFull();
		grid.setColumns("tin", "name", "foreignName", "status");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(evt -> editEnterprise(evt.getValue()));
	}

	private void editEnterprise(Enterprise enterprise) {
		if (enterprise == null) {
			closeEditor();
		} else {
			form.setEnterprise(enterprise);
			form.setVisible(true);
			addClassName("editing");
		}
	}

	private void closeEditor() {
		form.setEnterprise(null);
		form.setVisible(false);
		removeClassName("editing");
	}

	private void updateList() {
		grid.setItems(enterpriseService.findAll(filterText.getValue()));
	}

}
