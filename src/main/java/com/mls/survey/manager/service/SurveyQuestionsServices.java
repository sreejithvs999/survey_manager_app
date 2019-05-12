package com.mls.survey.manager.service;

import java.util.List;

import com.mls.survey.manager.bean.SurveyQuestionBean;

public interface SurveyQuestionsServices {

	public static final int QUESTION_STATUS_ACTIVE = 1;
	public static final int QUESTION_STATUS_INACTIVE = 0;

	List<SurveyQuestionBean> getQuestions();

	SurveyQuestionBean createSurveyQuestion(SurveyQuestionBean question);

	void updateSurveyQuestion(SurveyQuestionBean question);

	void deleteSurveyQuestion(Integer questionId);

}
