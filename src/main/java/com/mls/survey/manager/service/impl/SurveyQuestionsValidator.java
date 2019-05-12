package com.mls.survey.manager.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mls.survey.manager.bean.AnswerOptionBean;
import com.mls.survey.manager.bean.SurveyQuestionBean;
import com.mls.survey.manager.dao.bean.AnswerOptionDO;
import com.mls.survey.manager.dao.bean.QuestionDO;
import com.mls.survey.manager.exceptions.DataNotFoundException;
import com.mls.survey.manager.exceptions.InvalidInputException;
import com.mls.survey.manager.service.SurveyQuestionsServices;

@Component
public class SurveyQuestionsValidator {

	public void validateSurveyQuestionSaveInput(SurveyQuestionBean question) {

		validateQuestionText(question.getQuestionText());

		if (question.getOptions() != null) {
			question.getOptions().forEach(option -> {
				validateOptionDescription(option.getDescription());
			});
		}
	}

	public void validateSurveyQuestionUpdateInput(SurveyQuestionBean question) {

		validateQuestionId(question.getQuestionId());
		validateQuestionText(question.getQuestionText());

		if (question.getOptions() != null) {
			question.getOptions().forEach(option -> {
				validateOptionId(option.getOptionId());
				validateOptionDescription(option.getDescription());
			});
		}
	}

	public void validateAnswerOptionSaveInput(AnswerOptionBean option) {
		validateQuestionId(option.getQuestion().getQuestionId());
		validateOptionDescription(option.getDescription());
	}

	public void validateAnswerOptionUpdateInput(AnswerOptionBean option) {
		validateQuestionId(option.getQuestion().getQuestionId());
		validateOptionId(option.getOptionId());
		validateOptionDescription(option.getDescription());
	}

	public void validateQuestionId(Integer questionId) {

		if (questionId == null || questionId <= 0) {
			throw new InvalidInputException("questionId is invalid.");
		}
	}

	public void validateOptionId(Integer optionId) {

		if (optionId == null || optionId <= 0) {
			throw new InvalidInputException("option.optionId is invalid.");
		}
	}

	private void validateQuestionText(String questionText) {

		if (StringUtils.isEmpty(questionText)) {

			throw new InvalidInputException("questionText should not be empty.");

		}

		if (questionText.length() > 250) {
			throw new InvalidInputException("questionText should not exceeds 250 characters.");
		}
	}

	private void validateOptionDescription(String optionDescr) {

		if (StringUtils.isEmpty(optionDescr)) {
			throw new InvalidInputException("option.description should not be empty. value=" + optionDescr);
		}

		if (optionDescr.length() > 250) {
			throw new InvalidInputException(
					"option.description should not exceeds 250 characters. value=" + optionDescr);
		}
	}

	public void validateAnswerOptionDO(AnswerOptionDO answerOptionDO, Integer questionId, Integer optionId) {
		if (answerOptionDO == null || !answerOptionDO.getQuestionId().equals(questionId)) {
			throw new DataNotFoundException("No answer option found for id: " + optionId);
		}
	}

	public void validateQuestionDO(QuestionDO questionDO, Integer givenId) {
		if (questionDO == null || !questionDO.getStatus().equals(SurveyQuestionsServices.QUESTION_STATUS_ACTIVE)) {
			throw new DataNotFoundException("No question found for id: " + givenId);
		}
	}
}
