package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import security.UserAccount;
import security.UserAccountRepository;
import forms.AdministratorForm;

@Service
@Transactional
public class UserAccountService {

	@Autowired
	private UserAccountRepository useraccountRepository;

	@Autowired
	private Validator validator;

	public UserAccount create() {
		UserAccount result;
		result = new UserAccount();
		return result;
	}

	public UserAccount reconstruct(final AdministratorForm administratorForm,
			final BindingResult binding) {
		final UserAccount result;
		result = this.useraccountRepository.findOne(administratorForm
				.getIdAdministrator());
		Assert.isTrue(
				administratorForm.getPasswordChecker().equals(
						administratorForm.getPassword()),
				"administrator.validation.passwordsNotMatch");

		result.setPassword(administratorForm.getPassword());
		result.setUsername(administratorForm.getUsername());
		this.validator.validate(result, binding);
		this.useraccountRepository.flush();

		return result;
	}

}
