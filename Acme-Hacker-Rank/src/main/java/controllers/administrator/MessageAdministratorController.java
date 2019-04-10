package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;

import controllers.AbstractController;
import domain.Actor;
import domain.Message;

@Controller
@RequestMapping("/message/administrator")
public class MessageAdministratorController extends AbstractController {

	// Services

	@Autowired
	private MessageService messageService;

	@Autowired
	private ActorService actorService;

	// Broadcast
	@RequestMapping(value = "/broadcast", method = RequestMethod.POST, params = "save")
	public ModelAndView broadcast(@Valid final Message mensaje,
			final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(mensaje);
		else
			try {
				this.messageService.broadcast(mensaje);
				result = new ModelAndView("redirect:/message/actor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(mensaje,
						"message.commit.error");
			}

		return result;
	}

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

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Message mensaje) {
		ModelAndView result;

		result = this.createEditModelAndView(mensaje, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message mensaje,
			final String messageCode) {
		final ModelAndView result;
		Actor sender;

		sender = mensaje.getSender();

		result = new ModelAndView("message/edit");
		result.addObject("sender", sender);
		result.addObject("mensaje", mensaje);
		// En la siguiente línea estamos indicando que el receptor del mensaje
		// es el mismo administrador que lo está enviando,
		// esto es incorrecto, pero hará que el pase el @valid del método
		// broadcast,
		// el servicio correspondiente se encargará de entregar el mensaje a
		// todos los actores del sistema
		result.addObject("recipients", this.actorService.findByPrincipal());
		result.addObject("broadcast", true);
		result.addObject("permission", true);
		result.addObject("requestURI", "message/administrator/broadcast.do");

		result.addObject("message", messageCode);

		return result;

	}

}
