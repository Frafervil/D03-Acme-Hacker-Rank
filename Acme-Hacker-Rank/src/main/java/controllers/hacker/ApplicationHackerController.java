
package controllers.hacker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ApplicationService;
import services.HackerService;
import services.PositionService;
import domain.Application;
import domain.Hacker;
import forms.ApplicationForm;

@Controller
@RequestMapping("/application/hacker")
public class ApplicationHackerController extends AbstractController {

	//Services
	@Autowired
	private HackerService		hackerService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private PositionService		positionService;


	//Repositories

	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Application> applications;
		Hacker principal;
		principal = this.hackerService.findByPrincipal();

		applications = this.applicationService.findAllApplicationsByHackerId(principal.getId());

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);

		return result;
	}

	//List By Status
	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "applicationStatus")
	public ModelAndView listByStatus(@RequestParam final int applicationStatus) {
		final ModelAndView result;
		Map<String, List<Application>> groupedApplication;
		Collection<Application> applications;
		Hacker principal;
		principal = this.hackerService.findByPrincipal();

		applications = this.applicationService.findAllApplicationsByHackerId(principal.getId());

		groupedApplication = this.applicationService.groupByStatus(applications);

		if (applicationStatus == 0)
			applications = this.applicationService.findAllApplicationsByHackerId(principal.getId());
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

		return result;

	}

	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int applicationId) {
		final ModelAndView result;
		Application application;

		application = this.applicationService.findOne(applicationId);

		result = new ModelAndView("application/display");
		result.addObject("application", application);
		return result;
	}

	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Application application;
		ApplicationForm applicationForm;

		application = this.applicationService.create();
		applicationForm = this.applicationService.construct(application);
		result = this.createEditModelAndView(applicationForm);

		return result;
	}

	//Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;
		ApplicationForm applicationForm;

		application = this.applicationService.findOne(applicationId);
		applicationForm = this.applicationService.construct(application);
		Assert.notNull(application);
		result = this.createEditModelAndView(applicationForm);

		return result;
	}

	//Save de create
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("applicationForm") @Valid final ApplicationForm applicationForm, final BindingResult binding) {
		ModelAndView result;
		Application application;

		try {
			application = this.applicationService.reconstruct(applicationForm, binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				result = this.createEditModelAndView(applicationForm);
			} else {
				application = this.applicationService.save(application);
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(applicationForm, "application.commit.error");
		}
		return result;
	}

	//Save de edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "edit")
	public ModelAndView edit(@ModelAttribute("applicationForm") @Valid final ApplicationForm applicationForm, final BindingResult binding) {
		ModelAndView result;
		Application application;

		try {
			application = this.applicationService.reconstruct(applicationForm, binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				result = this.createEditModelAndView(applicationForm);
			} else {
				this.applicationService.submit(application);
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(applicationForm, "application.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final ApplicationForm applicationForm) {
		ModelAndView result;
		result = this.createEditModelAndView(applicationForm, null);
		return result;
	}

	private ModelAndView createEditModelAndView(final ApplicationForm applicationForm, final String messageCode) {
		ModelAndView result;

		if (applicationForm.getId() != 0)
			result = new ModelAndView("application/edit");
		else
			result = new ModelAndView("application/create");

		result.addObject("applicationForm", applicationForm);
		result.addObject("positions", this.positionService.findAllFinal());
		result.addObject("message", messageCode);
		return result;
	}
}
