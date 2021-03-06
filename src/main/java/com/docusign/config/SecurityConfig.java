/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.docusign.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;

/**
 * @author Joe Grandja
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// @formatter:off
	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
				.antMatchers("/assets/**", "/webjars/**");

	}
	// @formatter:on

	// @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		var resolver = new DefaultBearerTokenResolver();
		resolver.setAllowUriQueryParameter(true);
		http
			.oauth2ResourceServer().bearerTokenResolver(resolver).jwt().and()
			.and()
			.authorizeRequests(authorizeRequests ->
				authorizeRequests
						.antMatchers("/render/**").authenticated()
					.anyRequest().permitAll())
			.oauth2Login(oauth2Login ->
				oauth2Login
					.loginPage("/oauth2/authorization/local-client")
					.failureUrl("/login?error")
					.permitAll())
			.logout(logout ->
				logout
					.logoutSuccessUrl("http://localhost:8090/uaa/logout.do?client_id=login-client&redirect=http://localhost:8080"))
			.oauth2Client();

	}
	// @formatter:on

}
