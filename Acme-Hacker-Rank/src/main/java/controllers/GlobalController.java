
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import services.CustomisationService;

@ControllerAdvice
public class GlobalController {

	@Autowired
	private CustomisationService	customisationService;


	@ModelAttribute("bannerWelcome")
	public String populateUser() {
		final String banner = this.customisationService.find().getWelcomeBanner();
		return banner;
	}
}
