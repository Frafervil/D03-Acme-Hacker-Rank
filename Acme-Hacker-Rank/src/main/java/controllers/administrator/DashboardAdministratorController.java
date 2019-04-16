package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CompanyService;
import services.HackerService;
import services.PositionService;

import controllers.AbstractController;
import domain.Company;
import domain.Hacker;
import domain.Position;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services

	@Autowired
	private HackerService hackerService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private ApplicationService applicationService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		final Double avgPositionsPerCompany, minPositionsPerCompany, maxPositionsPerCompany, stddevPositionsPerCompany;
		final Double avgApplicationsPerHacker, minApplicationsPerHacker, maxApplicationsPerHacker, stddevApplicationsPerHacker;

		final Collection<Company> companiesWithMorePositions;
		final Collection<Hacker> hackersWithMoreApplications;

		final Double avgSalariesOffered, minSalariesOffered, maxSalariesOffered, stddevSalariesOffered;

		final Position bestSalaryPosition;
		final Position worstSalaryPosition;

		// Stadistics

		// avg
		avgPositionsPerCompany = this.positionService.avgPositionsPerCompany();

		// min
		minPositionsPerCompany = this.positionService.minPositionsPerCompany();

		// max
		maxPositionsPerCompany = this.positionService.maxPositionsPerCompany();

		// standard Deviation
		stddevPositionsPerCompany = this.positionService
				.stddevPositionsPerCompany();

		// avg
		avgApplicationsPerHacker = this.applicationService
				.avgApplicationsPerHacker();

		// min
		minApplicationsPerHacker = this.applicationService
				.minApplicationsPerHacker();

		// max
		maxApplicationsPerHacker = this.applicationService
				.maxApplicationsPerHacker();

		// standard Deviation
		stddevApplicationsPerHacker = this.applicationService
				.stddevApplicationsPerHacker();

		companiesWithMorePositions = this.companyService
				.companiesWithMorePositions();

		hackersWithMoreApplications = this.hackerService
				.hackersWithMoreApplications();

		// avg
		avgSalariesOffered = this.positionService.avgSalariesOffered();

		// min
		minSalariesOffered = this.positionService.minSalariesOffered();

		// max
		maxSalariesOffered = this.positionService.maxSalariesOffered();

		// standard Deviation
		stddevSalariesOffered = this.positionService.stddevSalariesOffered();

		bestSalaryPosition = this.positionService.bestSalaryPosition();
		worstSalaryPosition = this.positionService.worstSalaryPosition();

		result = new ModelAndView("administrator/dashboard");
		result.addObject("avgPositionsPerCompany", avgPositionsPerCompany);
		result.addObject("minPositionsPerCompany", minPositionsPerCompany);
		result.addObject("maxPositionsPerCompany", maxPositionsPerCompany);
		result.addObject("stddevPositionsPerCompany", stddevPositionsPerCompany);

		result.addObject("avgApplicationsPerHacker", avgApplicationsPerHacker);
		result.addObject("minApplicationsPerHacker", minApplicationsPerHacker);
		result.addObject("maxApplicationsPerHacker", maxApplicationsPerHacker);
		result.addObject("stddevApplicationsPerHacker",
				stddevApplicationsPerHacker);

		result.addObject("companiesWithMorePositions",
				companiesWithMorePositions);

		result.addObject("hackersWithMoreApplications",
				hackersWithMoreApplications);

		result.addObject("avgSalariesOffered", avgSalariesOffered);
		result.addObject("minSalariesOffered", minSalariesOffered);
		result.addObject("maxSalariesOffered", maxSalariesOffered);
		result.addObject("stddevSalariesOffered", stddevSalariesOffered);

		result.addObject("bestSalaryPosition", bestSalaryPosition);
		result.addObject("worstSalaryPosition", worstSalaryPosition);

		return result;

	}

}
