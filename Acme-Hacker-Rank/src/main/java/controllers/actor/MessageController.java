package controllers.actor;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;

import controllers.AbstractController;
import domain.Actor;
import domain.Message;

@Controller
@RequestMapping("/message/actor")
public class MessageController extends AbstractController {

	// Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageService messageService;

	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Message> messages;

		try {
			final int actorId = this.actorService.findByPrincipal().getId();
			Assert.notNull(actorId);

			messages = this.messageService.findByActorId(actorId);

			result = new ModelAndView("message/list");
			result.addObject("messages", messages);
			result.addObject("requestURI", "message/actor/list.do");

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = new ModelAndView("message/list");
			result.addObject("message", "message.retrieve.error");
			result.addObject("messages", new ArrayList<Message>());
		}

		return result;
	}

	@RequestMapping(value = "/exportData")
	public ModelAndView exportData() {
		ModelAndView result;
		Message mensaje;
		Actor principal;

		principal = this.actorService.findByPrincipal();

		try {
			mensaje = this.messageService.create();
			mensaje.getRecipients().add(principal);
			mensaje.setBody("Name: " + principal.getName() + " || Surname: "
					+ principal.getSurname() + " || Photo: "
					+ principal.getPhoto() + " || Phone: "
					+ principal.getPhone() + " || Address: "
					+ principal.getAddress() + " || VAT number: "
					+ principal.getVatNumber() + " || Username: "
					+ principal.getUserAccount().getUsername());
			mensaje.setSubject("Your data");

			this.messageService.save(mensaje);

			result = new ModelAndView("redirect:/message/actor/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/message/actor/list.do");
		}

		return result;
	}

}
