
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ApplicationRepository;
import domain.Answer;
import domain.Application;
import domain.Company;
import domain.Hacker;
import domain.Problem;
import forms.ApplicationForm;

@Service
@Transactional
public class ApplicationService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ApplicationRepository	applicationRepository;

	@Autowired
	private Validator				validator;

	// Supporting services ----------------------------------------------------
	@Autowired
	private AnswerService			answerService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private HackerService			hackerService;

	@Autowired
	private ProblemService			problemService;


	// Simple CRUD Methods
	public void delete(final Application application) {
		final Answer answer;

		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);

		answer = application.getAnswer();

		if (answer != null)
			this.answerService.delete(answer);

		this.applicationRepository.delete(application);

	}

	public Application findOne(final int applicationId) {
		Application result;

		result = this.applicationRepository.findOne(applicationId);
		Assert.notNull(result);
		return result;

	}

	public Application save(final Application application) {
		Application result;
		result = this.applicationRepository.save(application);
		Assert.notNull(result);
		return result;
	}

	// Additional functions

	public Collection<Application> findAllByPositionId(final int positionId) {
		Collection<Application> result;

		result = this.applicationRepository.findAllByPositionId(positionId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Application> findAllByProblemId(final int problemId) {
		Collection<Application> result;
		result = new ArrayList<Application>();
		result = this.applicationRepository.findAllByProblemId(problemId);

		return result;
	}

	public Collection<Application> findAllByCompanyId(final int companyId) {
		Collection<Application> result;
		result = new ArrayList<Application>();
		result = this.applicationRepository.findAllByCompany(companyId);

		return result;
	}

	public Collection<Application> findAllApplicationsByHackerId(final int hackerId) {
		Collection<Application> result;
		result = new ArrayList<Application>();
		result = this.applicationRepository.findAllApplicationsByHackerId(hackerId);

		return result;
	}

	public Map<String, List<Application>> groupByStatus(final Collection<Application> applications) {
		final Map<String, List<Application>> result = new HashMap<String, List<Application>>();

		Assert.notNull(applications);
		for (final Application r : applications)
			if (result.containsKey(r.getStatus()))
				result.get(r.getStatus()).add(r);
			else {
				final List<Application> l = new ArrayList<Application>();
				l.add(r);
				result.put(r.getStatus(), l);
			}

		if (!result.containsKey("ACCEPTED"))
			result.put("ACCEPTED", new ArrayList<Application>());
		if (!result.containsKey("PENDING"))
			result.put("PENDING", new ArrayList<Application>());
		if (!result.containsKey("REJECTED"))
			result.put("REJECTED", new ArrayList<Application>());
		if (!result.containsKey("SUBMITTED"))
			result.put("SUBMITTED", new ArrayList<Application>());

		return result;
	}

	public void reject(final Application a) {
		Company principal;

		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(a.getStatus().equals("PENDING"));

		a.setStatus("REJECTED");

		this.applicationRepository.save(a);
	}

	public void accept(final Application a) {
		Company principal;

		Assert.notNull(a);
		Assert.isTrue(a.getId() != 0);

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(a.getStatus().equals("PENDING"));

		a.setStatus("ACCEPTED");

		this.applicationRepository.save(a);
	}

	public Application reconstruct(final ApplicationForm applicationForm, final BindingResult binding) {
		Application result;
		List<Problem> problems = new ArrayList<>();
		if (applicationForm.getId() == 0) {
			result = this.create();
			result.setMoment(new Date(System.currentTimeMillis() - 1));
			result.setHacker(this.hackerService.findByPrincipal());
			result.setStatus("PENDING");
			result.setPosition(applicationForm.getPosition());
			problems = (List<Problem>) this.problemService.findAllByPositionId(applicationForm.getPosition().getId());
			final Random random = new Random();
			final Problem problem = problems.get(random.nextInt(problems.size() - 1));
			result.setProblem(problem);
			//result.setProblem(this.problemService.findProblemByPositionId(applicationForm.getPosition().getId()));
		} else {
			result = this.applicationRepository.findOne(applicationForm.getId());
			final Answer answer = new Answer();
			result.setAnswer(answer);
			result.getAnswer().setAnswerText(applicationForm.getAnswerText());
			result.getAnswer().setCodeLink(applicationForm.getCodeLink());
			result.getAnswer().setMoment(new Date(System.currentTimeMillis() - 1));

			if (applicationForm.getAnswerText().isEmpty())
				binding.rejectValue("answerText", "application.validation.answerText", "Must not be blank");
			if (applicationForm.getCodeLink().isEmpty())
				binding.rejectValue("codeLink", "application.validation.codeLink", "Must not be blank");
		}

		this.validator.validate(result, binding);
		this.applicationRepository.flush();
		return result;
	}

	public Application reconstructCompany(final Application application, final BindingResult binding) {
		Application result;
		result = this.applicationRepository.findOne(application.getId());
		result.getAnswer().setMoment(application.getAnswer().getMoment());
		result.getAnswer().setAnswerText(application.getAnswer().getAnswerText());
		result.getAnswer().setCodeLink(application.getAnswer().getCodeLink());

		this.validator.validate(result, binding);
		this.applicationRepository.flush();
		return result;
	}

	public Application create() {
		Application result;
		final Hacker principal;

		principal = this.hackerService.findByPrincipal();
		Assert.notNull(principal);

		result = new Application();
		result.setStatus("PENDING");
		result.setHacker(principal);
		return result;
	}

	public ApplicationForm construct(final Application application) {
		final ApplicationForm applicationForm = new ApplicationForm();
		applicationForm.setId(application.getId());
		applicationForm.setPosition(application.getPosition());
		applicationForm.setStatus(application.getStatus());
		return applicationForm;
	}

}
