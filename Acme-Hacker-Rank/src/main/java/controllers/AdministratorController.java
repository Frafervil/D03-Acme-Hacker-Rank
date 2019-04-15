/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Administrator;
import forms.AdministratorForm;

import services.AdministratorService;
import services.CustomisationService;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	private AdministratorService administratorservice;

	@Autowired
	private CustomisationService customisationService;

	@RequestMapping("/viewProfile")
	public ModelAndView view() {
		ModelAndView result;

		result = new ModelAndView("administrator/viewProfile");
		result.addObject("actor", this.administratorservice.findByPrincipal());

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Administrator administrator;
		administrator = this.administratorservice.findByPrincipal();
		Assert.notNull(administrator);
		result = this.editModelAndView(administrator);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(
			@ModelAttribute("administrator") Administrator administrator,
			final BindingResult binding) {
		ModelAndView result;

		try {
			administrator = this.administratorservice.reconstructPruned(
					administrator, binding);
			if (binding.hasErrors()) {
				result = this.editModelAndView(administrator);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
			} else {
				administrator = this.administratorservice.save(administrator);
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		} catch (final Throwable oops) {
			result = this.editModelAndView(administrator,
					"administrator.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete() {
		ModelAndView result;

		try {
			this.administratorservice.delete();

			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/administrator/viewProfile.do");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(
			final Administrator administrator) {
		ModelAndView result;
		AdministratorForm administratorForm;
		administratorForm = this.administratorservice.construct(administrator);
		result = this.createEditModelAndView(administratorForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(
			final AdministratorForm administratorForm, final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("administrator/edit");
		result.addObject("administratorForm", administratorForm);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);

		return result;
	}

	private ModelAndView editModelAndView(final Administrator administrator) {
		ModelAndView result;
		result = this.editModelAndView(administrator, null);
		return result;
	}

	private ModelAndView editModelAndView(final Administrator administrator,
			final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("administrator/edit");
		result.addObject("administrator", administrator);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);

		return result;
	}

}
