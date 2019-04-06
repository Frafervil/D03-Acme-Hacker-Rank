package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Problem;

import repositories.ProblemRepository;

@Service
@Transactional
public class ProblemService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ProblemRepository	problemRepository;
	// Supporting services ----------------------------------------------------
	// Simple CRUD Methods
	// Business Methods

	public Collection<Problem> findAllByPositionId (final int positionId){
		Collection<Problem> result;
		
		result = this.problemRepository.findAllByPositionId(positionId);
		Assert.notNull(result);
		return result;
	}
	
	public int countByPositionId (final int positionId){
		int result;
		
		result = this.problemRepository.countByPositionId(positionId);

		return result;
	}
}
