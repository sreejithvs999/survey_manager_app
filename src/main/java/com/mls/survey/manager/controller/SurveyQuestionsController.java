package com.mls.survey.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mls.survey.manager.bean.AnswerOptionBean;
import com.mls.survey.manager.service.SurveyQuestionsServices;

@RestController
@RequestMapping(value = "/surveyQuestion")
public class SurveyQuestionsController {

	@Autowired
	private SurveyQuestionsServices questionsServices;
	
	@PostMapping
	public ResponseEntity<?> createQuestion(@RequestBody AnswerOptionBean questionBean) {
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
