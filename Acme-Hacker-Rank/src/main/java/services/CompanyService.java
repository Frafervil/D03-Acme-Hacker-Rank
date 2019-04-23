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

import repositories.CompanyRepository;
import repositories.CustomisationRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.CreditCard;
import domain.Message;
import domain.Position;
import domain.Problem;
import forms.CompanyForm;

@Service
@Transactional
public class CompanyService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserAccountRepository useraccountRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService actorService;

	@Autowired
	private CreditCardService creditCardService;

	 @Autowired
	 private ProblemService problemService;
	 
	 @Autowired
	 private PositionService positionService;

	 @Autowired
	 private ApplicationService applicationService;
	 
	 @Autowired
	 private AnswerService answerService;
	 
	 @Autowired
	 private MessageService messageService;
	 
	 @Autowired
	 private Validator validator;

	 @Autowired
	 private CustomisationRepository customisationRepository;

	 // Simple CRUD Methods

	 public boolean exists(final Integer arg0) {
		 return this.companyRepository.exists(arg0);
	 }

	public Company create() {
		Company result;
		UserAccount userAccount;
		Authority authority;
		CreditCard creditCard;

		result = new Company();
		userAccount = new UserAccount();
		authority = new Authority();
		creditCard = new CreditCard();

		authority.setAuthority("COMPANY");
		userAccount.addAuthority(authority);

		Assert.notNull(userAccount);
		Assert.notNull(creditCard);

		result.setUserAccount(userAccount);
		result.setCreditCard(creditCard);

		return result;
	}

	public Company save(final Company company) {
		Company saved;
		UserAccount logedUserAccount;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		logedUserAccount = this.actorService
				.createUserAccount(Authority.COMPANY);
		Assert.notNull(company, "company.not.null");

		if (this.exists(company.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "company.notLogged");
			Assert.isTrue(logedUserAccount.equals(company.getUserAccount()),
					"company.notEqual.userAccount");
			saved = this.companyRepository.findOne(company.getId());
			Assert.notNull(saved, "brotherhood.not.null");
			Assert.isTrue(
					saved.getUserAccount().getUsername()
							.equals(company.getUserAccount().getUsername()),
					"company.notEqual.username");
			Assert.isTrue(
					saved.getUserAccount().getPassword()
							.equals(company.getUserAccount().getPassword()),
					"company.notEqual.password");

			saved = this.companyRepository.save(company);

		} else {
			CreditCard creditCard;
			company.getUserAccount().setPassword(
					encoder.encodePassword(company.getUserAccount()
							.getPassword(), null));
			creditCard = this.creditCardService
					.saveNew(company.getCreditCard());
			company.setCreditCard(creditCard);
			saved = this.companyRepository.saveAndFlush(company);
		}
		return saved;
	}

	 public void delete() {
		 /*
		  * Orden de borrado:
		  * 	1 Answer de las applications a una position de la company
		  * 	2 Application a una position de la company
		  * 	3 Problemas
		  * 	4 Position
		  * 	5 Mensajes
		  * 	6 Company
		  * 	7 CC
		  */
		 Company principal;
		 Collection<Problem> problems;
		 Collection<Position> positions;
		 Collection<Application> applications;
		 Collection<Message> messages;

		 principal = this.findByPrincipal();
		 Assert.notNull(principal);
		 		 
		 applications = this.applicationService.findAllByCompanyId(principal.getId());
		 for (Application a : applications) {
				if(a.getAnswer()!= null)
					this.answerService.delete(a.getAnswer());
		 }
		 this.applicationService.deleteInBatch(applications);
		 
		 positions = this.positionService.findByCompany(principal.getId());
		 for (Position position : positions) {
			 position.getSkillsRequired().clear();
			 position.getTechnologiesRequired().clear();
			 position.getProblems().clear();
			 this.positionService.delete(position);
		 }
		 
		 problems = this.problemService.findAllByCompanyId(principal.getId());
		 for (Problem problem : problems) {
			 this.problemService.delete(problem);
		 }		 
		 
		 messages = this.messageService.findBySenderId(principal.getId());
		 this.messageService.deleteInBach(messages);

		 
		 this.companyRepository.delete(principal);
		 
		 this.creditCardService.delete(principal.getCreditCard());

	 }

	public Company findOne(final int companyId) {
		Company result;

		result = this.companyRepository.findOne(companyId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Company> findAll() {
		Collection<Company> result;

		result = this.companyRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	// Other business methods

	public Company findByPrincipal() {
		Company result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.companyRepository
				.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;

	}

	public CompanyForm construct(final Company company) {
		final CompanyForm companyForm = new CompanyForm();
		companyForm.setAddress(company.getAddress());
		companyForm.setEmail(company.getEmail());
		companyForm.setId(company.getId());
		companyForm.setName(company.getName());
		companyForm.setPhone(company.getPhone());
		companyForm.setPhoto(company.getPhoto());
		companyForm.setSurname(company.getSurname());
		companyForm.setVatNumber(company.getVatNumber());
		companyForm.setCommercialName(company.getCommercialName());
		companyForm.setBrandName(company.getCreditCard().getBrandName());
		companyForm.setCVV(company.getCreditCard().getCVV());
		companyForm.setExpirationMonth(company.getCreditCard()
				.getExpirationMonth());
		companyForm.setExpirationYear(company.getCreditCard()
				.getExpirationYear());
		companyForm.setHolderName(company.getCreditCard().getHolderName());
		companyForm.setNumber(company.getCreditCard().getNumber());
		companyForm.setCheckBox(companyForm.getCheckBox());
		companyForm.setUsername(company.getUserAccount().getUsername());
		// En los construct no coger la contraseña
		return companyForm;
	}

	public Company findByUserAccountId(final int userAccountId) {
		Assert.notNull(userAccountId);
		Company result;
		result = this.companyRepository.findByUserAccountId(userAccountId);
		return result;
	}

	public Company reconstruct(final CompanyForm companyForm,
			final BindingResult binding) {
		Company result;

		result = this.create();
		result.getUserAccount().setUsername(companyForm.getUsername());
		result.getUserAccount().setPassword(companyForm.getPassword());
		result.setAddress(companyForm.getAddress());
		result.setEmail(companyForm.getEmail());
		result.setName(companyForm.getName());
		result.setPhoto(companyForm.getPhoto());
		result.setSurname(companyForm.getSurname());
		result.setVatNumber(companyForm.getVatNumber());
		result.setCommercialName(companyForm.getCommercialName());
		result.getCreditCard().setBrandName(companyForm.getBrandName());
		result.getCreditCard().setCVV(companyForm.getCVV());
		result.getCreditCard().setExpirationMonth(
				companyForm.getExpirationMonth());
		result.getCreditCard().setExpirationYear(
				companyForm.getExpirationYear());
		result.getCreditCard().setHolderName(companyForm.getHolderName());
		result.getCreditCard().setNumber(companyForm.getNumber());

		if (!StringUtils.isEmpty(companyForm.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(companyForm.getPhone());
			if (matcher.matches())
				companyForm.setPhone(this.customisationRepository.findAll()
						.iterator().next().getCountryCode()
						+ companyForm.getPhone());
		}
		result.setPhone(companyForm.getPhone());

		if (!companyForm.getPassword().equals(companyForm.getPasswordChecker()))
			binding.rejectValue("passwordChecker",
					"company.validation.passwordsNotMatch",
					"Passwords doesnt match");
		if (!this.useraccountRepository.findUserAccountsByUsername(
				companyForm.getUsername()).isEmpty())
			binding.rejectValue("username",
					"company.validation.usernameExists",
					"This username already exists");
		if (companyForm.getCheckBox() == false)
			binding.rejectValue("checkBox", "company.validation.checkBox",
					"This checkbox must be checked");

		this.validator.validate(result, binding);
		this.companyRepository.flush();

		return result;

	}

	public Company reconstructPruned(final Company company,
			final BindingResult binding) {
		Company result;

		if (company.getId() == 0)
			result = company;
		else
			result = this.companyRepository.findOne(company.getId());
		result.setAddress(company.getAddress());
		result.setEmail(company.getEmail());
		result.setName(company.getName());
		result.setPhoto(company.getPhoto());
		result.setSurname(company.getSurname());
		result.setVatNumber(company.getVatNumber());
		result.setCommercialName(company.getCommercialName());

		if (!StringUtils.isEmpty(company.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(company.getPhone());
			if (matcher.matches())
				company.setPhone(this.customisationRepository.findAll()
						.iterator().next().getCountryCode()
						+ company.getPhone());
		}
		result.setPhone(company.getPhone());

		this.validator.validate(result, binding);
		this.companyRepository.flush();
		return result;
	}

	public void flush() {
		this.companyRepository.flush();
	}

	public Collection<Company> companiesWithMorePositions() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities()
				.contains(authority));
		Collection<Company> result;

		result = this.companyRepository.companiesWithMorePositions();

		return result;
	}

}
