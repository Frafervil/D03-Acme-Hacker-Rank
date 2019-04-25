package controllers.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
	
	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int problemId) {
		// Inicializa resultado
		ModelAndView result;
		Problem problem;

		// Busca en el repositorio
		problem = this.problemService.findOne(problemId);
		Assert.notNull(problem);

		// Crea y añade objetos a la vista
		result = new ModelAndView("problem/display");
		result.addObject("requestURI", "problem/display.do");
		result.addObject("problem", problem);

		// Envía la vista
		return result;
	}
	
	//Create
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Problem problem;

		problem = this.problemService.create();
		result = this.createModelAndView(problem);

		return result;
	}
	
	
	// Delete
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int problemId) {
		Problem problem;
		ModelAndView result;
		
		problem = this.problemService.findOne(problemId);
		try {
			this.problemService.delete(problem);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(problem, "problem.commit.error");
		}
		return result;
	}
	
	//Edit
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int problemId) {
		ModelAndView result;
		Problem problem;

		problem = this.problemService.findOne(problemId);
		Assert.notNull(problem);
		result = this.createEditModelAndView(problem);

		return result;
	}

	//Save draft --- CREATE
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "saveDraft")
	public ModelAndView createDraft(@ModelAttribute("problem") Problem problem, final BindingResult binding) {
		ModelAndView result;

		try {
			problem = this.problemService.reconstruct(problem, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(problem);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				problem = this.problemService.save(problem, true);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createModelAndView(problem, "problem.commit.error");
		}
		return result;
	}
	
	//Save Final --- CREATE
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView createFinal(@ModelAttribute("problem") Problem problem, final BindingResult binding) {
		ModelAndView result;

		try {
			problem = this.problemService.reconstruct(problem, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(problem);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				problem = this.problemService.save(problem, false);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createModelAndView(problem, "problem.commit.error");
		}
		return result;
	}
	// Save Draft --- Edit
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveDraft")
	public ModelAndView saveDraft(@ModelAttribute("problem") Problem problem, final BindingResult binding) {
		ModelAndView result;

		try {
			problem = this.problemService.reconstruct(problem, binding);
			if (binding.hasErrors()) {
				System.out.println(binding.getAllErrors());
				result = this.editModelAndView(problem);
			} else {
				problem = this.problemService.save(problem, true);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.editModelAndView(problem, "problem.commit.error");
		}
		return result;
	}
	
	// Save Draft --- Edit
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(@ModelAttribute("problem") Problem problem, final BindingResult binding) {
		ModelAndView result;

		try {
			problem = this.problemService.reconstruct(problem, binding);
			if (binding.hasErrors()) {
				System.out.println(binding.getAllErrors());
				result = this.editModelAndView(problem);
			} else {
				problem = this.problemService.save(problem, false);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.editModelAndView(problem, "problem.commit.error");
		}
		return result;
	}
	
	
	// ------------------- Ancillary Methods
			protected ModelAndView createEditModelAndView(final Problem problem) {
				ModelAndView result;

				result = this.createEditModelAndView(problem, null);

				return result;
			}

			protected ModelAndView createEditModelAndView(final Problem problem, final String messageCode) {
				ModelAndView result;

				result = new ModelAndView("problem/edit");
				result.addObject("problem", problem);

				result.addObject("message", messageCode);

				return result;
			}
			
			private ModelAndView createModelAndView(final Problem problem) {
				ModelAndView result;

				result = this.createModelAndView(problem, null);
				return result;
			}

			private ModelAndView createModelAndView(final Problem problem, final String messageCode) {
				ModelAndView result;
				result = new ModelAndView("problem/create");
				result.addObject("problem", problem);
				result.addObject("message", messageCode);
				return result;
			}
			

			private ModelAndView editModelAndView(final Problem problem) {
				ModelAndView result;

				result = this.editModelAndView(problem, null);
				return result;
			}

			private ModelAndView editModelAndView(final Problem problem, final String messageCode) {
				ModelAndView result;

				result = new ModelAndView("float/edit");
				result.addObject("problem", problem);
				result.addObject("message", messageCode);
				return result;
			}


}
