
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProblemRepository;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class ProblemService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ProblemRepository	problemRepository;
	// Supporting services ----------------------------------------------------
	@Autowired
	private CompanyService		companyService;

	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private PositionService positionService;
		
	@Autowired
	private Validator			validator;


	// Simple CRUD Methods

	public Problem create() {
		Problem result;
		final Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		result = new Problem();
		result.setIsDraft(true);
		result.setCompany(principal);
		return result;
	}

	public Problem save(final Problem problem, final Boolean draft) {
		Problem result;
		Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(problem);
		Assert.isTrue(problem.getCompany() == principal);
		problem.setIsDraft(draft);

		result = this.problemRepository.save(problem);
		Assert.notNull(result);
		return result;
	}

	public void delete(final Problem problem) {
		Collection<Position> positions;
		Collection<Problem> problems;
		Collection<Application> applications;
		
		positions = this.positionService.findByProblemId(problem.getId());
		applications = this.applicationService.findAllByProblemId(problem.getId());
		
		for (Position p : positions) {
			problems = p.getProblems();
			Assert.isTrue(problems.size()>2, "problem.position.error");
		}
		
		Assert.notNull(applications, "problem.application.error");
		
		this.problemRepository.delete(problem);

	}

	public Problem findOne(final int problemId) {
		Problem result;

		result = this.problemRepository.findOne(problemId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Problem> findAll() {
		Collection<Problem> result;

		result = this.problemRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	// Business Methods
	
	public Collection<Problem> findAllByCompanyId (final int companyId){
		Collection<Problem> result;

		result = this.problemRepository.findAllByCompanyId(companyId);

		return result;
	}
	
	public Collection<Problem> findAllFinalByCompanyId (final int companyId){
		Collection<Problem> result;
		
		result = this.problemRepository.findAllFinalByCompanyId(companyId);

		return result;
	}

	public Problem reconstruct(final Problem problem, final BindingResult binding) {
		Problem result;
		if (problem.getId() == 0)
			result = problem;
		else
			result = this.problemRepository.findOne(problem.getId());

		result.setTitle(problem.getTitle());
		result.setStatement(problem.getStatement());
		result.setHint(problem.getHint());
		result.setAttachments(problem.getAttachments());
		result.setIsDraft(problem.getIsDraft());
		result.setCompany(this.companyService.findByPrincipal());
		
		this.validator.validate(result, binding);
		return result;
	}
	public void deleteInBatch(Collection<Problem> problems){
		this.problemRepository.deleteInBatch(problems);
	}
}
