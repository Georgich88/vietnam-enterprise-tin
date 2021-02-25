package com.georgeisaev.vietnam.enterprise.tin.storage.ui.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | Vietnam enterprise TIN")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private final LoginForm login;

	public LoginView() {
		// Instantiates a LoginForm component to capture username and password.
		login = new LoginForm();
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		// Sets the LoginForm action to "login" to post the login form to Spring Security.
		login.setAction("login");
		add(new H1("Vietnam enterprise TIN"), login);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		// Reads query parameters and shows an error if a login attempt fails.
		if (beforeEnterEvent.getLocation()
				.getQueryParameters()
				.getParameters()
				.containsKey("error")) {
			login.setError(true);
		}
	}

}
