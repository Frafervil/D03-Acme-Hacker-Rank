
package controllers.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;


import services.ApplicationService;
import services.CompanyService;
import controllers.AbstractController;

import domain.Application;
import domain.Company;
import domain.Position;

@Controller
@RequestMapping("/application/company")
public class ApplicationCompanyController extends AbstractController {

	// Services

	@Autowired
	private ApplicationService		applicationService;

	@Autowired
	private CompanyService	companyService;


	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Application> applications;
		Company principal;

		principal = this.companyService.findByPrincipal();

		applications = this.applicationService.findAllByCompanyId(principal.getId());

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);
		result.addObject("requestURI", "application/company/list.do");

		return result;

	}
	@RequestMapping(value = "/list", method = RequestMethod.GET, params = {
		"applicationStatus"
	})
	public ModelAndView listByStatus(@RequestParam final int applicationStatus) {
		final ModelAndView result;
		Map<String, List<Application>> groupedApplication;
		Collection<Application> applications;
		Company principal;
		principal = this.companyService.findByPrincipal();

		applications = this.applicationService.findAllByCompanyId(principal.getId());
		
		groupedApplication = this.applicationService.groupByStatus(applications);

		if (applicationStatus == 0)
			applications = this.applicationService.findAllByCompanyId(principal.getId());
		else if (applicationStatus == 1)
			applications = new ArrayList<Application>(groupedApplication.get("ACCEPTED"));
		else if (applicationStatus == 2)
			applications = new ArrayList<Application>(groupedApplication.get("PENDING"));
		else if (applicationStatus == 3)
			applications = new ArrayList<Application>(groupedApplication.get("REJECTED"));
		else if (applicationStatus == 4)
			applications = new ArrayList<Application>(groupedApplication.get("SUBMITTED"));
		else
			applications = null;

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);
		result.addObject("applicationURI", "application/company/list.do");

		return result;

	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int applicationId) {
		final ModelAndView result;
		Application application;

		application = this.applicationService.findOne(applicationId);

		result = new ModelAndView("application/display");
		result.addObject("application", application);

		return result;
	}

	// REJECT
	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;
		Company principal;
		principal = this.companyService.findByPrincipal();

		application = this.applicationService.findOne(applicationId);
		Assert.notNull(application);
		Assert.isTrue(application.getPosition().getCompany().getId() == principal.getId() && application.getStatus().equals("SUBMITTED"));
		
		try{
			this.applicationService.reject(application);
			result = new ModelAndView("redirect:list.do");
		}catch(final Throwable oops) {
			result = this.createEditModelAndView(application, "application.commit.error");
		}
		return result;
		
	}

	// ACCEPT
	@RequestMapping(value = "/approve", method = RequestMethod.GET)
	public ModelAndView approve(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;
		Company principal;
		principal = this.companyService.findByPrincipal();

		application = this.applicationService.findOne(applicationId);
		Assert.notNull(application);
		Assert.isTrue(application.getPosition().getCompany().getId() == principal.getId() && application.getStatus().equals("SUBMITTED"));
		
		try{
			this.applicationService.approve(application);
			result = new ModelAndView("redirect:list.do");
		}catch(final Throwable oops) {
			result = this.createEditModelAndView(application, "application.commit.error");
		}
		return result;
	}


	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Application application, final String messageCode) {
		ModelAndView result;
		Position position;
		position = application.getPosition();

		result = new ModelAndView("application/display");
		result.addObject("application", application);
		result.addObject("position", position);
		result.addObject("message", messageCode);
		return result;
	}

}
