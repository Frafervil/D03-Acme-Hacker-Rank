
package controllers.company;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CompanyService;
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
		
		@RequestMapping(value = "/create", method = RequestMethod.GET)
		public ModelAndView create() {
			ModelAndView result;
			Position position;

			position = this.positionService.create();
			result = this.createModelAndView(position);

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
