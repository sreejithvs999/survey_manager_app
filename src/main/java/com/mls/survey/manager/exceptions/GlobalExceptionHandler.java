package com.mls.survey.manager.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(SurveyAppException.class)
	public ResponseEntity<?> handleSurveyAppException(SurveyAppException exception) {

		if (exception.getCause() != null) {
			logger.error("Server error.", exception.getCause());
		}
		return new ResponseEntity<>(errorResponse(exception.getMessage()), exception.getHttpStatus());
	}

	@ExceptionHandler(ServletRequestBindingException.class)
	public ResponseEntity<?> handleServletRequestBindingException(ServletRequestBindingException exception) {
		return new ResponseEntity<>(errorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		if (exception.getCause() instanceof JsonProcessingException) {
			return handleJsonProcessingException((JsonProcessingException) exception.getCause());
		}
		return handleAllExceptions(exception);
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<?> handleJsonProcessingException(JsonProcessingException exception) {
		if (logger.isWarnEnabled()) {
			logger.warn("JSON Processing Error", exception.getMessage());
		}
		return new ResponseEntity<>(
				errorResponse("Request payload processing failed. Please check Survey Apps API Spec."),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAllExceptions(Exception exception) {
		logger.error("Unexpected Server error.", exception);
		return new ResponseEntity<>(errorResponse("Something wrong with Survey Apps services. Please try later."),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Map<String, String> errorResponse(String message) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("error", message);
		return map;
	}
}
