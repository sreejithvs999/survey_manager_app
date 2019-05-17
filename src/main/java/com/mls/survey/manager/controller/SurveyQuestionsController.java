package com.mls.survey.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mls.survey.manager.bean.SurveyQuestionBean;
import com.mls.survey.manager.service.SurveyQuestionsServices;

@RestController
@RequestMapping(value = "/surveyQuestion")
public class SurveyQuestionsController {

	@Autowired
	private SurveyQuestionsServices questionsServices;

	@PostMapping
	public ResponseEntity<SurveyQuestionBean> createQuestion(@RequestBody SurveyQuestionBean questionBean) {

		questionBean = questionsServices.createSurveyQuestion(questionBean);
		return new ResponseEntity<>(questionBean, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{questionId}")
	public ResponseEntity<?> editQuestion(@PathVariable("questionId") Integer questionId,
			@RequestBody SurveyQuestionBean questionBean) {

		questionBean.setQuestionId(questionId);
		questionsServices.updateSurveyQuestion(questionBean);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping
	public ResponseEntity<List<SurveyQuestionBean>> getQuestions() {

		return new ResponseEntity<>(questionsServices.getQuestions(), HttpStatus.OK);
	}

	@DeleteMapping("/{questionId}")
	public ResponseEntity<?> deleteQuestion(@PathVariable("questionId") Integer questionId) {

		questionsServices.deleteSurveyQuestion(questionId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
