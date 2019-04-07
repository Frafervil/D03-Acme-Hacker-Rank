
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

import services.ActorService;
import services.PositionService;
import controllers.AbstractController;
import domain.Actor;
import domain.Position;

@Controller
@RequestMapping("/position/company")
public class PositionCompanyController extends AbstractController {

	@Autowired
	private PositionService	positionService;
	
	@Autowired
	private ActorService	actorService;
	

	//List
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Position> positions;
		positions = new ArrayList<Position>();
		
		final Actor principal = this.actorService.findByPrincipal();
		
		positions = this.positionService.findByCompany(principal.getId());

		result = new ModelAndView("position/list");
		result.addObject("positions", positions);
		result.addObject("requestURI", "position/company/list.do");

		return result;
	}
	
	// Display

		@RequestMapping(value = "/display", method = RequestMethod.GET)
		public ModelAndView display(@RequestParam final int positionId) {
			// Inicializa resultado
			ModelAndView result;
			Position position;

			// Busca en el repositorio
			position = this.positionService.findOne(positionId);
			Assert.notNull(position);

			// Crea y añade objetos a la vista
			result = new ModelAndView("position/display");
			result.addObject("requestURI", "position/display.do");
			result.addObject("position", position);

			// Envía la vista
			return result;
		}
		
		//Create
		
		@RequestMapping(value = "/create", method = RequestMethod.GET)
		public ModelAndView create() {
			ModelAndView result;
			Position position;

			position = this.positionService.create();
			result = this.createModelAndView(position);

			return result;
		}
		
		//Edit
		
		@RequestMapping(value = "/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam final int positionId) {
			ModelAndView result;
			Position position;

			position = this.positionService.findOne(positionId);
			Assert.notNull(position);
			result = this.createEditModelAndView(position);

			return result;
		}

		//Save
		
		@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
		public ModelAndView save(@ModelAttribute("position") Position position, final BindingResult binding) {
			ModelAndView result;

			try {
				position = this.positionService.reconstruct(position, binding);
				if (binding.hasErrors()) {
					result = this.createModelAndView(position);
					for (final ObjectError e : binding.getAllErrors())
						System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				} else {
					position = this.positionService.save(position, "DRAFT");
					result = new ModelAndView("redirect:/welcome/index.do");
				}

			} catch (final Throwable oops) {
				result = this.createModelAndView(position, "position.commit.error");
			}
			return result;
		}
		
		//Save Cancelled
		
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveCancelled")
		public ModelAndView saveDraft(@ModelAttribute("parade") Position position, final BindingResult binding) {
			ModelAndView result;

			try {
				position = this.positionService.reconstruct(position, binding);
				if (binding.hasErrors()) {
					result = this.createEditModelAndView(position);
					for (final ObjectError e : binding.getAllErrors())
						System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				} else {
					position = this.positionService.save(position,"CANCELLED");
					result = new ModelAndView("redirect:/welcome/index.do");
				}

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(position, "position.commit.error");
			}
			return result;
		}
		
		//Save Final

		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
		public ModelAndView saveFinal(@ModelAttribute("parade") Position position, final BindingResult binding) {
			ModelAndView result;

			try {
				position = this.positionService.reconstruct(position, binding);
				if (binding.hasErrors()) {
					System.out.println(binding.getAllErrors());
					result = this.createEditModelAndView(position);
				} else {
					this.positionService.save(position, "FINAL");
					result = new ModelAndView("redirect:/welcome/index.do");
				}

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(position, "position.commit.error");
			}

			return result;
		}
		
		//Delete
		
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
		public ModelAndView delete(final Position position, final BindingResult binding) {
			ModelAndView result;
			try {
				this.positionService.delete(position);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(position, "position.commit.error");
			}
			return result;
		}
		
		// -------------------
		protected ModelAndView createEditModelAndView(final Position position) {
			ModelAndView result;

			result = this.createEditModelAndView(position, null);

			return result;
		}

		protected ModelAndView createEditModelAndView(final Position position, final String messageCode) {
			ModelAndView result;

			result = new ModelAndView("position/edit");
			result.addObject("position", position);

			result.addObject("message", messageCode);

			return result;
		}
		
		private ModelAndView createModelAndView(final Position position) {
			ModelAndView result;

			result = this.createModelAndView(position, null);
			return result;
		}

		private ModelAndView createModelAndView(final Position position, final String messageCode) {
			ModelAndView result;
			result = new ModelAndView("position/create");
			result.addObject("position", position);
			result.addObject("message", messageCode);
			return result;
		}

}
