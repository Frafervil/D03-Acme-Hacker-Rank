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

import repositories.AdministratorRepository;
import repositories.CustomisationRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Administrator;
import domain.CreditCard;
import domain.Message;
import forms.AdministratorForm;

@Service
@Transactional
public class AdministratorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AdministratorRepository administratorRepository;

	@Autowired
	private UserAccountRepository useraccountRepository;

	@Autowired
	private CustomisationRepository customisationRepository;

	// Supporting services-------------------------------------------
	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private Validator validator;

	public Administrator findByPrincipal() {
		Administrator res;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		res = this.administratorRepository.findByUserAccountId(userAccount
				.getId());
		Assert.notNull(res);
		return res;
	}

	public boolean exists(final Integer arg0) {
		return this.administratorRepository.exists(arg0);
	}

	public Administrator create() {
		Administrator principal;
		principal = this.findByPrincipal();
		Assert.notNull(principal);

		Administrator result;
		UserAccount userAccount;
		Authority authority;
		CreditCard creditCard;

		result = new Administrator();
		userAccount = new UserAccount();
		authority = new Authority();
		creditCard = this.creditCardService.create();

		authority.setAuthority("ADMIN");
		userAccount.addAuthority(authority);
		result.setUserAccount(userAccount);
		result.setCreditCard(creditCard);

		return result;

	}

	public Administrator save(final Administrator administrator) {
		Administrator saved;
		UserAccount logedUserAccount;

		final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		logedUserAccount = this.actorService.createUserAccount(Authority.ADMIN);
		Assert.notNull(administrator, "administrator.not.null");

		if (administrator.getId() == 0) {
			CreditCard creditCard;
			administrator.getUserAccount().setPassword(
					passwordEncoder.encodePassword(administrator
							.getUserAccount().getPassword(), null));
			creditCard = this.creditCardService.saveNew(administrator
					.getCreditCard());
			administrator.setCreditCard(creditCard);
			saved = this.administratorRepository.saveAndFlush(administrator);

		} else {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "administrator.notLogged");
			Assert.isTrue(
					logedUserAccount.equals(administrator.getUserAccount()),
					"administrator.notEqual.userAccount");
			saved = this.administratorRepository.findOne(administrator.getId());
			Assert.notNull(saved, "administrator.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername()
					.equals(administrator.getUserAccount().getUsername()));
			Assert.isTrue(saved.getUserAccount().getPassword()
					.equals(administrator.getUserAccount().getPassword()));
			saved = this.administratorRepository.saveAndFlush(administrator);
		}

		return saved;
	}

	public void delete() {
		Administrator principal;
		Collection<Message> messages;

		principal = this.findByPrincipal();
		Assert.notNull(principal);
		// Debería quedar al menos un administrador
		Assert.isTrue(this.findAll().size() > 1);
		
		messages = this.messageService.findBySenderId(principal.getId());
		this.messageService.deleteInBach(messages);

		this.administratorRepository.delete(principal);
		 
		this.creditCardService.delete(principal.getCreditCard());
	}

	public Administrator findOne(final int adminId) {
		Administrator result;

		result = this.administratorRepository.findOne(adminId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Administrator> findAll() {
		Collection<Administrator> result;

		result = this.administratorRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public AdministratorForm construct(final Administrator administrator) {
		final AdministratorForm administratorForm = new AdministratorForm();
		administratorForm.setAddress(administrator.getAddress());
		administratorForm.setEmail(administrator.getEmail());
		administratorForm.setIdAdministrator(administrator.getId());
		administratorForm.setName(administrator.getName());
		administratorForm.setPhone(administrator.getPhone());
		administratorForm.setPhoto(administrator.getPhoto());
		administratorForm.setSurname(administrator.getSurname());
		administratorForm.setVatNumber(administrator.getVatNumber());
		administratorForm.setBrandName(administrator.getCreditCard()
				.getBrandName());
		administratorForm.setCVV(administrator.getCreditCard().getCVV());
		administratorForm.setExpirationMonth(administrator.getCreditCard()
				.getExpirationMonth());
		administratorForm.setExpirationYear(administrator.getCreditCard()
				.getExpirationYear());
		administratorForm.setHolderName(administrator.getCreditCard()
				.getHolderName());
		administratorForm.setNumber(administrator.getCreditCard().getNumber());
		administratorForm.setCheckBox(administratorForm.getCheckBox());
		administratorForm.setUsername(administrator.getUserAccount()
				.getUsername());
		return administratorForm;
	}

	public Administrator reconstruct(final AdministratorForm administratorForm,
			final BindingResult binding) {
		Administrator result;

		result = this.create();
		result.getUserAccount().setUsername(administratorForm.getUsername());
		result.getUserAccount().setPassword(administratorForm.getPassword());
		result.setAddress(administratorForm.getAddress());
		result.setEmail(administratorForm.getEmail());
		result.setName(administratorForm.getName());
		result.setPhoto(administratorForm.getPhoto());
		result.setSurname(administratorForm.getSurname());
		result.setVatNumber(administratorForm.getVatNumber());
		result.getCreditCard().setBrandName(administratorForm.getBrandName());
		result.getCreditCard().setCVV(administratorForm.getCVV());
		result.getCreditCard().setExpirationMonth(
				administratorForm.getExpirationMonth());
		result.getCreditCard().setExpirationYear(
				administratorForm.getExpirationYear());
		result.getCreditCard().setHolderName(administratorForm.getHolderName());
		result.getCreditCard().setNumber(administratorForm.getNumber());

		if (!StringUtils.isEmpty(administratorForm.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(administratorForm
					.getPhone());
			if (matcher.matches())
				administratorForm.setPhone(this.customisationRepository
						.findAll().iterator().next().getCountryCode()
						+ administratorForm.getPhone());
		}
		result.setPhone(administratorForm.getPhone());

		if (!administratorForm.getPassword().equals(
				administratorForm.getPasswordChecker()))
			binding.rejectValue("passwordChecker",
					"administrator.validation.passwordsNotMatch",
					"Passwords doesnt match");
		if (!this.useraccountRepository.findUserAccountsByUsername(
				administratorForm.getUsername()).isEmpty()
				|| administratorForm.getUsername().equals(
						LoginService.getPrincipal().getUsername()))
			binding.rejectValue("username",
					"administrator.validation.usernameExists",
					"This username already exists");
		if (administratorForm.getCheckBox() == false)
			binding.rejectValue("checkBox",
					"administrator.validation.checkBox",
					"This checkbox must be checked");

		this.validator.validate(result, binding);
		this.administratorRepository.flush();
		return result;
	}

	public Administrator reconstructPruned(final Administrator administrator,
			final BindingResult binding) {
		Administrator result;

		if (administrator.getId() == 0)
			result = administrator;
		else
			result = this.administratorRepository
					.findOne(administrator.getId());
		result.setAddress(administrator.getAddress());
		result.setEmail(administrator.getEmail());
		result.setName(administrator.getName());
		result.setPhoto(administrator.getPhoto());
		result.setSurname(administrator.getSurname());
		result.setVatNumber(administrator.getVatNumber());

		if (!StringUtils.isEmpty(administrator.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(administrator.getPhone());
			if (matcher.matches())
				administrator.setPhone(this.customisationRepository.findAll()
						.iterator().next().getCountryCode()
						+ administrator.getPhone());
		}
		result.setPhone(administrator.getPhone());

		this.validator.validate(result, binding);
		this.administratorRepository.flush();
		return result;
	}

	public void flush() {
		this.administratorRepository.flush();
	}
}
