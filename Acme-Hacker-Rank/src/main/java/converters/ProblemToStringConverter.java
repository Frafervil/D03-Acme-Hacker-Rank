/*
 * ActorToStringConverter.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Problem;

@Component
@Transactional
public class ProblemToStringConverter implements Converter<Problem, String> {

	@Override
	public String convert(final Problem problem) {
		String result;

		if (problem == null)
			result = null;
		else
			result = String.valueOf(problem.getId());

		return result;
	}
}
