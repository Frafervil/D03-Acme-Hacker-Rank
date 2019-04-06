package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Application;
import domain.Company;
import domain.Position;
import domain.Problem;

import repositories.ProblemRepository;

@Service
@Transactional
public class ProblemService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ProblemRepository	problemRepository;
	// Supporting services ----------------------------------------------------
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ApplicationService applicationService;
	
	
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
	
	public Problem save(Problem problem){
		Problem result;
		Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);
		
		Assert.notNull(problem);
		Assert.isTrue(problem.getCompany() == principal);
		
		result = this.problemRepository.save(problem);
		Assert.notNull(result);
		return result;
	}
	
	public void delete(Problem problem){
		Collection<Position> positions;
		Collection<Problem> problems;
		Collection<Application> applications;
		
		positions = problem.getPositions();
		applications = this.applicationService.findAllByProblemId(problem.getId());
		
		for (Position p : positions) {
			problems = this.findAllByPositionId(p.getId());
			Assert.isTrue(problems.size()>2,"This position has 2 problems only");
		}
		
		Assert.isNull(applications, "This problem is associated to an application");
		
		this.problemRepository.delete(problem);
		
	}
	
	// Business Methods

	public Collection<Problem> findAllByPositionId (final int positionId){
		Collection<Problem> result;
		
		result = this.problemRepository.findAllByPositionId(positionId);
		Assert.notNull(result);
		return result;
	}
	
	public Collection<Problem> findAllByCompanyId (final int companyId){
		Collection<Problem> result;
		
		result = this.problemRepository.findAllByCompanyId(companyId);
		Assert.notNull(result);
		return result;
	}
	
	public int countByPositionId (final int positionId){
		int result;
		
		result = this.problemRepository.countByPositionId(positionId);

		return result;
	}
}
