
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Administrator;

@Service
@Transactional
public class ActorService {

	// Managed repository ------------------------------------------
	@Autowired
	private ActorRepository			actorRepository;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	// Supporting services -----------------------------------------
	@Autowired
	private HackerService			hackerService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private AdministratorService	administratorService;


	// Simple CRUD methods -----------------------------------------

	public Actor findOne(final int actorId) {
		Actor result;
		result = this.actorRepository.findOne(actorId);
		if (result == null) {
			result = this.hackerService.findOne(actorId);
			if (result == null) {
				result = this.companyService.findOne(actorId);
				if (result == null)
					result = this.administratorService.findOne(actorId);
			}
		}
		return result;
	}

	public Collection<Actor> findAll() {
		Collection<Actor> result;
		result = this.actorRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public String getAuthorityAsString(final Actor actor) {
		String result;
		Administrator principal;
		Collection<Authority> authorities;

		Assert.notNull(actor);
		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);
		result = null;
		authorities = actor.getUserAccount().getAuthorities();
		for (final Authority a : authorities) {
			result = a.getAuthority();
			break;
		}
		Assert.notNull(result);
		return result;
	}

	public Collection<Actor> findAllMinusPrincipal() {
		Collection<Actor> result;
		Actor principal;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		principal = this.findByPrincipal();
		Assert.notNull(principal);

		result.remove(principal);
		return result;

	}

	// Other business methods --------------------------------------

	public Actor findByUserAccount(final UserAccount userAccount) {
		final Actor result = this.actorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public Actor findByPrincipal() {
		Actor result;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	public Actor findByUserAccountId(final int userAccountId) {
		Assert.notNull(userAccountId);
		Actor result;
		result = this.actorRepository.findByUserAccountId(userAccountId);
		return result;
	}

	public void save(final Actor principal) {
		this.actorRepository.save(principal);
	}

	public Actor findByUsername(final String username) {
		Actor result;

		try {
			result = this.findByUserAccount(this.userAccountRepository.findByUsername(username));
		} catch (final NullPointerException e) {
			result = null;
		}
		return result;
	}

	public UserAccount createUserAccount(final String authorityName) {

		final UserAccount result = new UserAccount();

		//Creamos el objetoAutoridad
		final Authority authority = new Authority();
		authority.setAuthority(authorityName);

		//Sacamos la colecci�n de autoridades y a�adimos la que necesitamos
		final Collection<Authority> authorities = result.getAuthorities();
		authorities.add(authority);

		//�Hace falta?
		result.setAuthorities(authorities);

		return result;
	}

	public String hashPassword(final String password) {
		String result;

		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		result = encoder.encodePassword(password, null);

		return result;
	}
}
