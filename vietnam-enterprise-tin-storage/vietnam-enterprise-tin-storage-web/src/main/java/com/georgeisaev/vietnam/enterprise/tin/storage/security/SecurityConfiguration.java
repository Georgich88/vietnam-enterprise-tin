package com.georgeisaev.vietnam.enterprise.tin.storage.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login?error";
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_SUCCESS_URL = "/login";

	private final UserDetailsService userDetailsService;

	@Autowired
	public SecurityConfiguration(@Qualifier("userCredentialsService") UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	/**
	 * Require login to access internal pages and configure login form.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http.
			csrf()
				.disable()
			.requestCache()
				.requestCache(new CustomRequestCache())
			.and()
				.authorizeRequests()
				.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
			.anyRequest()
				.authenticated()
			.and()
				.formLogin()
			.loginPage(LOGIN_URL)
				.permitAll()
				.loginProcessingUrl(LOGIN_PROCESSING_URL)
				.failureUrl(LOGIN_FAILURE_URL)
			.and()
				.logout()
				.logoutSuccessUrl(LOGOUT_SUCCESS_URL)
			.and()
				.headers()
					.frameOptions()
					.sameOrigin();
		//@formatter:on
	}

	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				"/VAADIN/**",
				"/favicon.ico",
				"/robots.txt",
				"/manifest.webmanifest",
				"/sw.js",
				"/offline.html",
				"/icons/**",
				"/images/**",
				"/styles/**",
				"/h2-console/**");
	}

}
