
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

import services.CompanyService;
import services.CustomisationService;
import services.PositionService;
import controllers.AbstractController;
import domain.Company;
import domain.Position;
import forms.CompanyForm;

@Controller
@RequestMapping("/company")
public class CompanyController extends AbstractController {

	// Services

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private CustomisationService	customisationService;


	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Company> companies;

		companies = this.companyService.findAll();

		result = new ModelAndView("company/list");
		result.addObject("companies", companies);
		result.addObject("requestURI", "company/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam(required = false) final Integer companyId) {
		final ModelAndView result;
		Company company = new Company();

		if (companyId == null)
			company = this.companyService.findByPrincipal();
		else
			company = this.companyService.findOne(companyId);

		Collection<Position> positions;
		
		try{
			Company principal;
			principal = this.companyService.findByPrincipal();
			positions = this.positionService.findByCompany(principal.getId());
		}catch(final Throwable oops){
			positions = this.positionService.findAvailableByCompanyId(company.getId());
		}

		result = new ModelAndView("company/display");
		result.addObject("company", company);
		result.addObject("positions", positions);

		return result;

	}

	//Create
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Company company;
		CompanyForm companyForm;

		company = this.companyService.create();
		companyForm = this.companyService.construct(company);
		result = this.createEditModelAndView(companyForm);

		return result;
	}

	// Save de Edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("company") Company company, final BindingResult binding) {
		ModelAndView result;

		try {
			company = this.companyService.reconstructPruned(company, binding);
			if (binding.hasErrors()) {
				result = this.editModelAndView(company);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else
				company = this.companyService.save(company);
			result = new ModelAndView("welcome/index");
		} catch (final Throwable oops) {
			result = this.editModelAndView(company, "company.commit.error");

		}

		return result;
	}

	//Save de Register
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView register(@ModelAttribute("companyForm") @Valid final CompanyForm companyForm, final BindingResult binding) {
		ModelAndView result;
		Company company;

		try {
			company = this.companyService.reconstruct(companyForm, binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				result = this.createEditModelAndView(companyForm);
			} else {
				company = this.companyService.save(company);
				result = new ModelAndView("welcome/index");
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(companyForm, "company.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Company company;

		company = this.companyService.findByPrincipal();
		Assert.notNull(company);
		result = this.editModelAndView(company);

		return result;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete() {
		ModelAndView result;

		try {
			this.companyService.delete();

			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/company/display.do");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------

	private ModelAndView editModelAndView(final Company company) {
		ModelAndView result;
		result = this.editModelAndView(company, null);
		return result;
	}

	private ModelAndView editModelAndView(final Company company, final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("company/edit");
		result.addObject("company", company);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);
		return result;
	}

	protected ModelAndView createEditModelAndView(final CompanyForm companyForm) {
		ModelAndView result;
		result = this.createEditModelAndView(companyForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final CompanyForm companyForm, final String message) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();
		if (companyForm.getId() != 0)
			result = new ModelAndView("company/edit");
		else
			result = new ModelAndView("company/register");

		result.addObject("companyForm", companyForm);
		result.addObject("actionURI", "company/edit.do");
		result.addObject("redirectURI", "welcome/index.do");
		result.addObject("countryCode", countryCode);

		result.addObject("message", message);

		return result;
	}
}
