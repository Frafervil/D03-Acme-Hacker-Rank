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

import domain.Application;

@Component
@Transactional
public class ApplicationToStringConverter implements Converter<Application, String> {

	@Override
	public String convert(final Application application) {
		String result;

		if (application == null)
			result = null;
		else
			result = String.valueOf(application.getId());

		return result;
	}
}
