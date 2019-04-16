package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.MessageService;

import controllers.AbstractController;
import domain.Message;

@Controller
@RequestMapping("/message/administrator")
public class MessageAdministratorController extends AbstractController {

	// Services

	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "/warning")
	public ModelAndView warning() {
		ModelAndView result;
		Message mensaje;

		try {
			mensaje = this.messageService.create();
			mensaje.setBody("Passwords have been leaked!"
					+ "¡Las contraseñas han sido filtradas!");
			mensaje.setSubject("WARNING!" + "¡ALERTA!");

			this.messageService.broadcast(mensaje);

			result = new ModelAndView("redirect:/message/actor/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/message/actor/list.do");
		}

		return result;
	}

}
