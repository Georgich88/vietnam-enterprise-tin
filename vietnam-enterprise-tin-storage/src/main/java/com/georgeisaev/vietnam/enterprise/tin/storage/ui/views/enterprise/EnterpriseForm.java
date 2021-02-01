package com.georgeisaev.vietnam.enterprise.tin.storage.ui.views.enterprise;

import com.georgeisaev.vietnam.enterprise.tin.storage.domain.enterprise.Enterprise;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnterpriseForm extends FormLayout {

	// region Fields

	// Fields

	final TextField tin = new TextField("TIN");
	final TextField name = new TextField("Name");
	final TextField foreignName = new TextField("Foreign name");
	final TextField shortName = new TextField("Short name");
	final TextField type = new TextField("Type");
	final DatePicker foundationDate = new DatePicker("Foundation date");
	final TextField representative = new TextField("Representative");
	final TextField legalAddress = new TextField("Address");
	final ComboBox<String> status = new ComboBox<>("Status");

	// Buttons

	final Button save = new Button("Save");
	final Button delete = new Button("Delete");
	final Button close = new Button("Cancel");

	// Bean

	final Binder<Enterprise> binder = new BeanValidationBinder<>(Enterprise.class);
	private Enterprise enterprise;

	// endregion

	public EnterpriseForm() {
		addClassName("enterprise-form");
		binder.bindInstanceFields(this);
		status.setItems("active");
		add(
				tin,
				name,
				foreignName,
				shortName,
				type,
				foundationDate,
				representative,
				legalAddress,
				status,
				createButtonsLayout()
		);
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
		binder.readBean(enterprise);
	}

	private Component createButtonsLayout() {
		// Buttons
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		// Buttons shortcuts
		save.addClickShortcut(Key.ENTER);
		close.addClickShortcut(Key.ESCAPE);
		// Buttons events
		save.addClickListener(click -> validateAndSave());
		delete.addClickListener(click -> fireEvent(new DeleteEvent(this, enterprise)));
		close.addClickListener(click -> fireEvent(new CloseEvent(this)));
		// Bean listener
		binder.addStatusChangeListener(evt -> save.setEnabled(binder.isValid()));
		// Layout
		return new HorizontalLayout(save, delete, close);
	}

	// region Validation

	private void validateAndSave() {
		try {
			binder.writeBean(enterprise);
			fireEvent(new SaveEvent(this, enterprise));
		} catch (ValidationException e) {
			log.error("", e);
		}
	}

	// endregion

	// region Events

	public abstract static class EnterpriseFormEvent extends ComponentEvent<EnterpriseForm> {

		private final Enterprise enterprise;

		protected EnterpriseFormEvent(EnterpriseForm source, Enterprise enterprise) {
			super(source, false);
			this.enterprise = enterprise;
		}

		public Enterprise getEnterprise() {
			return enterprise;
		}

	}

	public static class SaveEvent extends EnterpriseFormEvent {

		SaveEvent(EnterpriseForm source, Enterprise enterprise) {
			super(source, enterprise);
		}

	}

	public static class DeleteEvent extends EnterpriseFormEvent {

		DeleteEvent(EnterpriseForm source, Enterprise enterprise) {
			super(source, enterprise);
		}

	}

	public static class CloseEvent extends EnterpriseFormEvent {

		CloseEvent(EnterpriseForm source) {
			super(source, null);
		}

	}

	@Override
	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
	                                                              ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

	// endregion
}
