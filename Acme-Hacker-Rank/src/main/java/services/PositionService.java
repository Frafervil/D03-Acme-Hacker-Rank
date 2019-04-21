
package services;

import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionRepository;
import domain.Application;
import domain.Company;
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class PositionService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private PositionRepository	positionRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private ProblemService		problemService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private Validator			validator;


	// Simple CRUD Methods

	public boolean exist(final Integer positionId) {
		return this.positionRepository.exists(positionId);
	}

	public Position findOne(final int positionId) {
		Position result;

		result = this.positionRepository.findOne(positionId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findAll() {
		Collection<Position> result;

		result = this.positionRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findAllFinal() {
		Collection<Position> result;

		result = this.positionRepository.findAllFinal();
		Assert.notNull(result);

		return result;
	}

	public Position create() {
		Position result;
		final Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		result = new Position();
		result.setTicker(this.generateTicker(principal));
		result.setStatus("DRAFT");
		result.setCompany(principal);
		return result;
	}

	public Position save(final Position position, final String saveMode) {
		Company principal;
		Position result;
		int numProblems;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(position);
		Assert.isTrue(position.getCompany() == principal);

		numProblems = this.problemService.countByPositionId(position.getId());

		if (saveMode.equals("CANCELLED"))
			Assert.isTrue(position.getStatus().equals("FINAL"));
		if (saveMode.equals("FINAL"))
			Assert.isTrue(numProblems >= 2);

		position.setStatus(saveMode);

		result = this.positionRepository.save(position);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Position position) {
		Company principal;
		Collection<Problem> problems;
		Collection<Application> applications;

		Assert.notNull(position);

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		applications = this.applicationService.findAllByPositionId(position.getId());

		for (final Application a : applications)
			this.applicationService.delete(a);

		problems = this.problemService.findAllByPositionId(position.getId());
		for (final Problem p : problems)
			p.setPositions(null);

		this.positionRepository.delete(position);
	}

	// Business Methods

	public Collection<Position> findByCompany(final int companyId) {
		Collection<Position> result;

		result = this.positionRepository.findByCompanyId(companyId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findAvailableByCompanyId(final int companyId) {
		Collection<Position> result;

		result = this.positionRepository.findAvailableByCompanyId(companyId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findByKeyword(final String keyword) {
		final Collection<Position> result = this.positionRepository.findByKeyword(keyword);

		return result;
	}

	private String generateTicker(final Company company) {
		String result;
		String text;
		String numbers;
		text = company.getCommercialName().toUpperCase();
		final Random random = new Random();

		if (text.length() < 4)
			while (text.length() < 4)
				text.concat("X");
		else if (text.length() > 4)
			text = text.substring(0, 3);

		numbers = String.format("%04d", random.nextInt(10000));
		result = text + "-" + numbers;
		if (this.repeatedTicker(company, result))
			this.generateTicker(company);

		return result;
	}

	public boolean repeatedTicker(final Company company, final String ticker) {
		Boolean isRepeated = false;
		int repeats;

		repeats = this.positionRepository.findRepeatedTickers(company.getId(), ticker);

		if (repeats > 0)
			isRepeated = true;

		return isRepeated;
	}

	public Position reconstruct(final Position position, final BindingResult binding) {
		Position result;
		if (position.getId() == 0) {
			result = position;
			result.setTicker(this.generateTicker(position.getCompany()));

		} else
			result = this.positionRepository.findOne(position.getId());

		result.setDescription(position.getDescription());
		result.setDeadline(position.getDeadline());
		result.setTitle(position.getTitle());
		result.setProfileRequired(position.getProfileRequired());
		result.setSalaryOffered(position.getSalaryOffered());
		result.setSkillsRequired(position.getSkillsRequired());
		result.setCompany(this.companyService.findByPrincipal());
		result.setTechnologiesRequired(position.getTechnologiesRequired());
		result.setStatus(position.getStatus());

		this.validator.validate(result, binding);
		return result;
	}

	public void flush() {
		this.positionRepository.flush();
	}

}
