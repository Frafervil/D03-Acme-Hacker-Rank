package controllers.company;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.Problem;

import services.ActorService;
import services.ProblemService;

@Controller
@RequestMapping("/problem/company")
public class ProblemCompanyController {
	@Autowired
	private ActorService	actorService;
	
	@Autowired
	private ProblemService	problemService;
	
	//List
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Problem> problems;
		problems = new ArrayList<Problem>();
		
		final Actor principal = this.actorService.findByPrincipal();
		
		problems = this.problemService.findAllByCompanyId(principal.getId());

		result = new ModelAndView("problem/list");
		result.addObject("problems", problems);
		result.addObject("requestURI", "problem/company/list.do");

		return result;
	}
	
}
