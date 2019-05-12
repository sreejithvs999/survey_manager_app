package com.mls.survey.manager.exceptions;

import org.springframework.http.HttpStatus;

public abstract class SurveyAppException extends RuntimeException {

	public SurveyAppException(String message) {
		super(message);
	}

	public SurveyAppException(String message, Throwable cause) {
		super(message, cause);
	}

	public abstract HttpStatus getHttpStatus();
}
