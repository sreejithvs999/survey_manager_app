package com.mls.survey.manager.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mls.survey.manager.bean.AnswerOptionBean;
import com.mls.survey.manager.bean.SurveyQuestionBean;
import com.mls.survey.manager.bean.SurveyResponseEntry;
import com.mls.survey.manager.bean.SurveyResponseForm;
import com.mls.survey.manager.dao.AnswerOptionsDao;
import com.mls.survey.manager.dao.SurveyQuestionsDao;
import com.mls.survey.manager.dao.bean.SurveySummaryView;
import com.mls.survey.manager.exceptions.InvalidInputException;
import com.mls.survey.manager.service.SurveyPollingServices;
import com.mls.survey.manager.service.SurveyQuestionsServices;

public class DefaultSurveyPollingService implements SurveyPollingServices {

	private static final Logger logger = LoggerFactory.getLogger(DefaultSurveyPollingService.class);

	@Autowired
	private AnswerOptionsDao optionsDao;

	@Autowired
	private SurveyQuestionsDao questionsDao;

	@Autowired
	private SurveyQuestionsValidator validator;

	@Override
	public void saveSurveyResponse(SurveyResponseForm form) {

		if (form.getSurveyResponse() == null || form.getSurveyResponse().isEmpty()) {
			throw new InvalidInputException("surveyResponse is missing or empty.");
		}
		// filter out duplicates
		Set<SurveyResponseEntry> responseEntries = new LinkedHashSet<>(form.getSurveyResponse());

		Integer[] questionIds = new Integer[responseEntries.size()];
		Integer[] answerIds = new Integer[responseEntries.size()];

		int i = 0;
		for (SurveyResponseEntry entry : responseEntries) {
			validator.validateQuestionId(entry.getQuestionId());
			validator.validateOptionId(entry.getOptionId());
			questionIds[i] = entry.getQuestionId();
			answerIds[i++] = entry.getOptionId();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Total responded answers : {}", i);
		}

		optionsDao.voteForAnswerOptions(questionIds, answerIds);
	}

	@Override
	public SurveyResponseForm getSurveyResults() {

		List<SurveySummaryView> resultList = questionsDao
				.getSurveyResult(SurveyQuestionsServices.QUESTION_STATUS_ACTIVE);
		SurveyResponseForm form = new SurveyResponseForm();

		for (SurveySummaryView view : resultList) {

			SurveyQuestionBean question = SurveyQuestionBean.ofId(view.getQuestion().getQuestionId());
			question.setQuestionText(view.getQuestion().getQuestionText());
			question.setTotalResponse(view.getTotalVotes());

			view.getAnswers().forEach(ans -> {
				AnswerOptionBean answer = AnswerOptionBean.ofId(ans.getOptionId());
				answer.setDescription(ans.getDescription());
				answer.setVoteCount(ans.getVoteCount());
				if (!question.getTotalResponse().equals(0L)) {
					answer.setPercent(answer.getVoteCount() * 100.0 / question.getTotalResponse());
				}
				question.getOptions().add(answer);
			});
			form.getSurveyResult().add(question);
		}

		return form;
	}

}
