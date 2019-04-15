package services;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CustomisationRepository;
import repositories.HackerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Company;
import domain.CreditCard;
import domain.Hacker;
import forms.HackerForm;

@Service
@Transactional
public class HackerService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private HackerRepository hackerRepository;

	@Autowired
	private UserAccountRepository useraccountRepository;

	@Autowired
	private CustomisationRepository customisationRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private Validator validator;

	// Additional functions

	// Simple CRUD Methods

	public Hacker create() {
		Hacker result;
		CreditCard creditCard;

		result = new Hacker();
		creditCard = new CreditCard();

		// Nuevo userAccount con Member en la lista de authorities
		final UserAccount userAccount = this.actorService
				.createUserAccount(Authority.HACKER);

		result.setUserAccount(userAccount);
		result.setCreditCard(creditCard);

		return result;
	}

	public Hacker save(final Hacker hacker) {
		Hacker saved;
		UserAccount logedUserAccount;

		final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		logedUserAccount = this.actorService
				.createUserAccount(Authority.HACKER);
		Assert.notNull(hacker, "hacker.not.null");

		if (hacker.getId() == 0) {
			CreditCard creditCard;
			hacker.getUserAccount().setPassword(
					passwordEncoder.encodePassword(hacker.getUserAccount()
							.getPassword(), null));
			creditCard = this.creditCardService.saveNew(hacker.getCreditCard());
			hacker.setCreditCard(creditCard);
			saved = this.hackerRepository.saveAndFlush(hacker);

		} else {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "hacker.notLogged");
			Assert.isTrue(logedUserAccount.equals(hacker.getUserAccount()),
					"hacker.notEqual.userAccount");
			saved = this.hackerRepository.findOne(hacker.getId());
			Assert.notNull(saved, "hacker.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername()
					.equals(hacker.getUserAccount().getUsername()));
			Assert.isTrue(saved.getUserAccount().getPassword()
					.equals(hacker.getUserAccount().getPassword()));
			saved = this.hackerRepository.saveAndFlush(hacker);
		}

		return saved;
	}

	// public void delete() {
	// Hacker principal;
	// final Collection<Application> applications;
	//
	// principal = this.findByPrincipal();
	// Assert.notNull(principal);
	//
	// applications = this.applicationService.findAllByHacker(principal
	// .getId());
	// for (final Application a : applications)
	// this.applicationService.deleteApplicationDeletingProfile(a);
	//
	// this.hackerRepository.delete(principal);
	// }

	public Hacker findOne(final int hackerId) {
		Hacker result;

		result = this.hackerRepository.findOne(hackerId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Hacker> findAll() {
		Collection<Hacker> result;

		result = this.hackerRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Hacker findByPrincipal() {
		Hacker result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.hackerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;

	}

	public Hacker findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Hacker result;
		result = this.hackerRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public boolean exists(final Integer arg0) {
		return this.hackerRepository.exists(arg0);
	}

	public HackerForm construct(final Hacker hacker) {
		final HackerForm hackerForm = new HackerForm();
		hackerForm.setAddress(hacker.getAddress());
		hackerForm.setEmail(hacker.getEmail());
		hackerForm.setIdHacker(hacker.getId());
		hackerForm.setName(hacker.getName());
		hackerForm.setPhone(hacker.getPhone());
		hackerForm.setPhoto(hacker.getPhoto());
		hackerForm.setSurname(hacker.getSurname());
		hackerForm.setVatNumber(hacker.getVatNumber());
		hackerForm.setBrandName(hacker.getCreditCard().getBrandName());
		hackerForm.setCVV(hacker.getCreditCard().getCVV());
		hackerForm.setExpirationMonth(hacker.getCreditCard()
				.getExpirationMonth());
		hackerForm
				.setExpirationYear(hacker.getCreditCard().getExpirationYear());
		hackerForm.setHolderName(hacker.getCreditCard().getHolderName());
		hackerForm.setNumber(hacker.getCreditCard().getNumber());
		hackerForm.setCheckBox(hackerForm.getCheckBox());
		hackerForm.setUsername(hacker.getUserAccount().getUsername());
		return hackerForm;
	}

	public Hacker reconstruct(final HackerForm hackerForm,
			final BindingResult binding) {
		Hacker result;

		result = this.create();
		result.getUserAccount().setUsername(hackerForm.getUsername());
		result.getUserAccount().setPassword(hackerForm.getPassword());
		result.setAddress(hackerForm.getAddress());
		result.setEmail(hackerForm.getEmail());
		result.setName(hackerForm.getName());
		result.setPhoto(hackerForm.getPhoto());
		result.setSurname(hackerForm.getSurname());
		result.setVatNumber(hackerForm.getVatNumber());
		result.getCreditCard().setBrandName(hackerForm.getBrandName());
		result.getCreditCard().setCVV(hackerForm.getCVV());
		result.getCreditCard().setExpirationMonth(
				hackerForm.getExpirationMonth());
		result.getCreditCard()
				.setExpirationYear(hackerForm.getExpirationYear());
		result.getCreditCard().setHolderName(hackerForm.getHolderName());
		result.getCreditCard().setNumber(hackerForm.getNumber());

		if (!StringUtils.isEmpty(hackerForm.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(hackerForm.getPhone());
			if (matcher.matches())
				hackerForm.setPhone(this.customisationRepository.findAll()
						.iterator().next().getCountryCode()
						+ hackerForm.getPhone());
		}
		result.setPhone(hackerForm.getPhone());

		if (!hackerForm.getPassword().equals(hackerForm.getPasswordChecker()))
			binding.rejectValue("passwordChecker",
					"member.validation.passwordsNotMatch",
					"Passwords doesnt match");
		if (!this.useraccountRepository.findUserAccountsByUsername(
				hackerForm.getUsername()).isEmpty())
			binding.rejectValue("username", "member.validation.usernameExists",
					"This username already exists");
		if (hackerForm.getCheckBox() == false)
			binding.rejectValue("checkBox", "member.validation.checkBox",
					"This checkbox must be checked");

		this.validator.validate(result, binding);
		this.hackerRepository.flush();

		return result;
	}

	public Hacker reconstructPruned(final Hacker hacker,
			final BindingResult binding) {
		Hacker result;

		if (hacker.getId() == 0)
			result = hacker;
		else
			result = this.hackerRepository.findOne(hacker.getId());
		result.setAddress(hacker.getAddress());
		result.setEmail(hacker.getEmail());
		result.setName(hacker.getName());
		result.setPhoto(hacker.getPhoto());
		result.setSurname(hacker.getSurname());
		result.setVatNumber(hacker.getVatNumber());

		if (!StringUtils.isEmpty(hacker.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(hacker.getPhone());
			if (matcher.matches())
				hacker.setPhone(this.customisationRepository.findAll()
						.iterator().next().getCountryCode()
						+ hacker.getPhone());
		}
		result.setPhone(hacker.getPhone());

		this.validator.validate(result, binding);
		this.hackerRepository.flush();
		return result;
	}

	public void flush() {
		this.hackerRepository.flush();
	}

	public Collection<Hacker> hackersWithMoreApplications() {
		Collection<Hacker> result;

		result = this.hackerRepository.hackersWithMoreApplications();

		return result;
	}

}
