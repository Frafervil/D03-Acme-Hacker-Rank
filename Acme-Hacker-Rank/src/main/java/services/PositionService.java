
package services;

import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import domain.Company;
import domain.Position;

@Service
@Transactional
public class PositionService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private PositionRepository	positionRepository;


	// Supporting services ----------------------------------------------------

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProblemSer companyService;
	
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
	
	public Position save(final Position position, String saveMode) {
		Company principal;
		Position result;
		int numProblems;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);
		
		Assert.notNull(position);
		Assert.isTrue(position.getCompany() == principal);
		
		numProblems = this.p
		
		if(saveMode.equals("CANCELLED")){
			Assert.isTrue(position.getStatus().equals("FINAL"));
		}
		if(saveMode.equals("FINAL")){
			Assert.isTrue(position.getPRO);
		}
		
		position.setStatus(saveMode);
		
		result = this.positionRepository.save(position);
		Assert.notNull(result);

		return result;
	}
	

	private String generateTicker(Company company) {
		String result;
		String text;
		String numbers;
		text = company.getCommercialName().toUpperCase();
		Random random = new Random();

		if (text.length() < 4){
			while(text.length() < 4){
				text.concat("X");
			}
		}else{
			if(text.length() >4){
				text = text.substring(0,3);
			}
		}
		
		numbers = String.format("%04d", random.nextInt(10000));		
		result = text + "-" + numbers;
		if(this.repeatedTicker(company, result))
			generateTicker(company);
			
		return result;
	}

	public boolean repeatedTicker(Company company, String ticker){
		Boolean isRepeated = false;
		int repeats;
		
		repeats = this.positionRepository.findRepeatedTickers(company.getId(), ticker);
		
		if(repeats>0)
			isRepeated = true;
		
		return isRepeated;	
	}
	
	public void flush() {
		this.positionRepository.flush();
	}
}
