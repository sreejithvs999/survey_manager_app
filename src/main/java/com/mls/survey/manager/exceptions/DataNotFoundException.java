package com.mls.survey.manager.exceptions;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends SurveyAppException {

	private static final long serialVersionUID = 1L;

	public DataNotFoundException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.NOT_FOUND;
	}
}
