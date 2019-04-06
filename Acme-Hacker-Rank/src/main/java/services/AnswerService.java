package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Answer;

import repositories.AnswerRepository;

@Service
@Transactional
public class AnswerService {
	// Managed repository -----------------------------------------------------
	@Autowired
	private AnswerRepository answerRepository;
	
	// Simple CRUD Methods
	public void delete(Answer answer){
		this.answerRepository.delete(answer);
	}
}
