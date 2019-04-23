package controllers.any;

import java.util.Arrays;
import java.util.Collection;

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

import services.CustomisationService;
import services.HackerService;
import controllers.AbstractController;
import domain.Hacker;
import forms.HackerForm;

@Controller
@RequestMapping("/hacker")
public class HackerController extends AbstractController {

	// Services

	@Autowired
	private HackerService hackerService;

	@Autowired
	private CustomisationService customisationService;

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Hacker> hackers;

		hackers = this.hackerService.findAll();

		result = new ModelAndView("hacker/list");
		result.addObject("hackers", hackers);
		result.addObject("requestURI", "hacker/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(
			@RequestParam(required = false) final Integer hackerId) {
		final ModelAndView result;
		Hacker hacker = new Hacker();

		if (hackerId == null)
			hacker = this.hackerService.findByPrincipal();
		else
			hacker = this.hackerService.findOne(hackerId);

		result = new ModelAndView("hacker/display");
		result.addObject("hacker", hacker);

		return result;

	}

	// Create
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Hacker hacker;
		HackerForm hackerForm;

		hacker = this.hackerService.create();
		hackerForm = this.hackerService.construct(hacker);
		result = this.createEditModelAndView(hackerForm);

		return result;
	}

	// Save de Edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("hacker") Hacker hacker,
			final BindingResult binding) {
		ModelAndView result;

		try {
			hacker = this.hackerService.reconstructPruned(hacker, binding);
			if (binding.hasErrors()) {
				result = this.editModelAndView(hacker);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
			} else
				hacker = this.hackerService.save(hacker);
			result = new ModelAndView("welcome/index");
		} catch (final Throwable oops) {
			result = this.editModelAndView(hacker, "hacker.commit.error");

		}

		return result;
	}

	// Save de Register
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView register(
			@ModelAttribute("hackerForm") @Valid final HackerForm hackerForm,
			final BindingResult binding) {
		ModelAndView result;
		Hacker hacker;

		try {
			hacker = this.hackerService.reconstruct(hackerForm, binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
				result = this.createEditModelAndView(hackerForm);
			} else {
				hacker = this.hackerService.save(hacker);
				result = new ModelAndView("welcome/index");
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(hackerForm,
					"hacker.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Hacker hacker;

		hacker = this.hackerService.findByPrincipal();
		Assert.notNull(hacker);
		result = this.editModelAndView(hacker);

		return result;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete() {
		ModelAndView result;

		try {
			this.hackerService.delete();

			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/company/display.do");
		}

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	private ModelAndView editModelAndView(final Hacker hacker) {
		ModelAndView result;
		result = this.editModelAndView(hacker, null);
		return result;
	}

	private ModelAndView editModelAndView(final Hacker hacker,
			final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("hacker/edit");
		result.addObject("hacker", hacker);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);
		return result;
	}

	protected ModelAndView createEditModelAndView(final HackerForm hackerForm) {
		ModelAndView result;
		result = this.createEditModelAndView(hackerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final HackerForm hackerForm,
			final String message) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();
		if (hackerForm.getIdHacker() != 0)
			result = new ModelAndView("hacker/edit");
		else
			result = new ModelAndView("hacker/register");

		result.addObject("hackerForm", hackerForm);
		result.addObject("actionURI", "hacker/edit.do");
		result.addObject("redirectURI", "welcome/index.do");
		result.addObject("countryCode", countryCode);

		result.addObject("message", message);

		return result;
	}
}
