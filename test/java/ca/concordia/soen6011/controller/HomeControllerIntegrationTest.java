package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.configuration.WebSecurityConfig;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.context.annotation.Import;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Import(WebSecurityConfig.class)
@WebMvcTest(HomeController.class)
class HomeControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testIndexView() throws Exception {

		// 1. Verifying HTTP Request Matching
		MvcResult mvcResult = mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andReturn();

		// 2. Verifying Input Deserialization
		
		// 3. Verifying Input Validation
		
		// 4. Verifying Business Logic Calls
		
		// 5. Verifying Output Serialization
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains("WhateverJobYouChoose"));
	}

	@Test
	void testErrorView() throws Exception {

		// 1. Verifying HTTP Request Matching
		MvcResult mvcResult = mockMvc.perform(get("/error"))
				.andExpect(status().isOk())
				.andExpect(view().name("error"))
				.andReturn();

		// 2. Verifying Input Deserialization
		
		// 3. Verifying Input Validation
		
		// 4. Verifying Business Logic Calls
		
		// 5. Verifying Output Serialization
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains("Something happened. Please try again"));
	}
}
