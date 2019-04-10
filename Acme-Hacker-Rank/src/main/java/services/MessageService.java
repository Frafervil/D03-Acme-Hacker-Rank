package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Message;

import repositories.MessageRepository;

@Service
@Transactional
public class MessageService {

	// Managed Repository

	@Autowired
	private MessageRepository messageRepository;

	// Supporting services

	@Autowired
	private ActorService actorService;

	// Simple CRUD methods

	public Message create() {
		Message result;
		Actor principal;
		Collection<Actor> recipients;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		result = new Message();
		result.setSender(principal);
		recipients = new ArrayList<Actor>();
		result.setRecipients(recipients);

		return result;
	}

	public Message save(final Message message) {
		Actor principal;
		Message result;

		Assert.notNull(message);

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(message.getSender().equals(principal));

		result = this.messageRepository.save(message);
		Assert.notNull(result);

		return result;
	}

	public Collection<Message> findByActorId(final int actorId) {
		Collection<Message> result;

		result = this.messageRepository.findByRecipientId(actorId);
		Assert.notNull(result);
		return result;
	}

	public Message broadcast(final Message message) {
		Message result;
		Message saved;
		Actor principal;

		Assert.notNull(message);

		principal = this.actorService.findByPrincipal();

		Assert.notNull(principal);

		message.setSender(principal);

		Assert.isTrue(!message.getRecipients().contains(principal));

		saved = this.messageRepository.save(message);
		Assert.notNull(saved);

		result = this.messageRepository.save(saved);
		Assert.notNull(result);

		return result;
	}

}
