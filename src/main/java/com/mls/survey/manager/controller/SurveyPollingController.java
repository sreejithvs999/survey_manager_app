package com.mls.survey.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mls.survey.manager.bean.SurveyResponseForm;
import com.mls.survey.manager.service.SurveyPollingServices;

@RestController
@RequestMapping("/surveyQuestion")
public class SurveyPollingController {

	@Autowired
	private SurveyPollingServices pollingServices;

	@PostMapping("/poll")
	public ResponseEntity<?> voteSurveyQuestions(@RequestBody SurveyResponseForm surveyResponse) {

		pollingServices.saveSurveyResponse(surveyResponse);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/results")
	public ResponseEntity<SurveyResponseForm> getSurveyResults() {

		return new ResponseEntity<>(pollingServices.getSurveyResults(), HttpStatus.OK);
	}
}
