
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.Actor;
import domain.CreditCard;

@Service
@Transactional
public class CreditCardService {

	// Managed Repository
	@Autowired
	private CreditCardRepository	creditCardRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService			actorService;


	public CreditCard findOne(final int creditcardId) {
		CreditCard result;

		result = this.creditCardRepository.findOne(creditcardId);

		Assert.notNull(result);

		return result;
	}

	public CreditCard create() {
		CreditCard result;

		result = new CreditCard();
		Assert.notNull(result);

		return result;
	}

	public CreditCard save(final CreditCard creditCard) {
		CreditCard result;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getCreditCard().getId() == creditCard.getId());

		result = this.creditCardRepository.save(creditCard);
		Assert.notNull(result);

		return result;
	}

	public CreditCard saveNew(final CreditCard creditCard) {
		CreditCard result;

		result = this.creditCardRepository.save(creditCard);
		Assert.notNull(result);

		return result;
	}
	
	public void delete(CreditCard card){
		this.creditCardRepository.delete(card);
	}
}
