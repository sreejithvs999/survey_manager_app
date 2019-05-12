package com.mls.survey.manager.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class SurveyResponseForm {

	private List<SurveyResponseEntry> surveyResponse = new ArrayList<SurveyResponseEntry>();

	private List<SurveyQuestionBean> surveyResult = new ArrayList<SurveyQuestionBean>();

	public List<SurveyResponseEntry> getSurveyResponse() {
		return surveyResponse;
	}

	public void setSurveyResponse(List<SurveyResponseEntry> surveyResponse) {
		this.surveyResponse = surveyResponse;
	}

	public List<SurveyQuestionBean> getSurveyResult() {
		return surveyResult;
	}

	public void setSurveyResult(List<SurveyQuestionBean> surveyResult) {
		this.surveyResult = surveyResult;
	}

}
