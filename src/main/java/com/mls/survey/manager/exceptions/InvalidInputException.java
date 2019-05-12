package com.mls.survey.manager.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends SurveyAppException {

	private static final long serialVersionUID = 1L;

	public InvalidInputException(String message) {
		super(message);
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HttpStatus.BAD_REQUEST;
	}
}
