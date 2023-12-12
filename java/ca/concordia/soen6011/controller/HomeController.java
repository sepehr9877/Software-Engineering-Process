package ca.concordia.soen6011.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller handles page requests on the root or 
 * index of the website.
 */
@Controller
@RequestMapping("/")
public class HomeController {

	/**
	 * Default constructor a(will add dependencies after)
	 */
	public HomeController() {
		//empty
	}

	/**
	 * Return the index html page
	 * @return the index page
	 */
	@GetMapping
	public String getIndexView() {
		
		return "index";
	}

	/**
	 * Return the error html page
	 * @return the error page
	 */
	@GetMapping("/error")
	public String getErrorView() {

		return "error";
	}
	@GetMapping("homepage")
	public String home_page() {
		return "homepage_link_nav";
	}

}
