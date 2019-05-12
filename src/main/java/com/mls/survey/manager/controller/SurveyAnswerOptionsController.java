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

import com.mls.survey.manager.bean.AnswerOptionBean;
import com.mls.survey.manager.bean.SurveyQuestionBean;
import com.mls.survey.manager.service.AnswerOptionServices;

@RestController
@RequestMapping("/surveyQuestion/{questionId}/answerOption")
public class SurveyAnswerOptionsController {

	@Autowired
	private AnswerOptionServices optionServices;

	@PostMapping
	public ResponseEntity<AnswerOptionBean> saveAnswerOption(@PathVariable("questionId") Integer questionId,
			@RequestBody AnswerOptionBean option) {

		option.setQuestion(SurveyQuestionBean.ofId(questionId));
		return new ResponseEntity<AnswerOptionBean>(optionServices.saveAnswerOption(option), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<AnswerOptionBean>> getAnswerOptions(@PathVariable("questionId") Integer questionId) {

		return new ResponseEntity<List<AnswerOptionBean>>(optionServices.getAnswerOptions(questionId), HttpStatus.OK);
	}

	@PutMapping("/{optionId}")
	public ResponseEntity<?> editAnswerOption(@PathVariable("questionId") Integer questionId,
			@PathVariable("optionId") Integer optionId, @RequestBody AnswerOptionBean option) {

		option.setQuestion(SurveyQuestionBean.ofId(questionId));
		option.setOptionId(optionId);
		optionServices.updateAnswerOption(option);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{optionId}")
	public ResponseEntity<?> deleteAnswerOption(@PathVariable("questionId") Integer questionId,
			@PathVariable("optionId") Integer optionId) {

		AnswerOptionBean answer = AnswerOptionBean.ofId(optionId);
		answer.setQuestion(SurveyQuestionBean.ofId(questionId));
		optionServices.deleteAnswerOption(answer);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
