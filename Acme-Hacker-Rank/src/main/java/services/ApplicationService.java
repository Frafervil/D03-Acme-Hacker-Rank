package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Answer;
import domain.Application;

import repositories.ApplicationRepository;

@Service
@Transactional
public class ApplicationService {
	// Managed repository -----------------------------------------------------
	@Autowired
	private ApplicationRepository applicationRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private AnswerService answerService;
	
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
	
	// Additional functions

	public Collection<Application> findAllByPositionId(int positionId){
		Collection<Application> result;
		
		result = this.applicationRepository.findAllByPositionId(positionId);
		Assert.notNull(result);
		
		return result;
	}
	
	public Collection<Application> findAllByProblemId(int problemId){
		Collection<Application> result;
		
		result = this.applicationRepository.findAllByProblemId(problemId);
		
		return result;
	}
}
