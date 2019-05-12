package com.mls.survey.manager.service;

import com.mls.survey.manager.bean.SurveyResponseForm;

public interface SurveyPollingServices {

	void saveSurveyResponse(SurveyResponseForm surveyResponse);

	SurveyResponseForm getSurveyResults();
}
