package com.mls.survey.manager.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mls.survey.manager.bean.SurveyQuestionBean;
import com.mls.survey.manager.dao.SurveyQuestionsDao;
import com.mls.survey.manager.dao.bean.QuestionDO;
import com.mls.survey.manager.service.SurveyQuestionsServices;

@Service
public class DefaultSurveyQuestionsServices implements SurveyQuestionsServices {

	private static final Logger logger = LoggerFactory.getLogger(DefaultSurveyQuestionsServices.class);

	@Autowired
	private SurveyQuestionsValidator validator;

	@Autowired
	private SurveyQuestionsDao questionsDao;

	@Autowired
	private SurveyQuestionsAnswersSupport answerOptionsSupport;

	@Override
	@Transactional(readOnly = true)
	public List<SurveyQuestionBean> getQuestions() {
		List<QuestionDO> questionDOList = questionsDao.getQuestions(QUESTION_STATUS_ACTIVE);
		List<SurveyQuestionBean> questions = new ArrayList<SurveyQuestionBean>();
		for (QuestionDO questionDO : questionDOList) {
			SurveyQuestionBean question = new SurveyQuestionBean();
			question.setQuestionId(questionDO.getQuestionId());
			question.setQuestionText(question.getQuestionText());
			question.setModifiedTime(questionDO.getModifiedTime());
			questions.add(question);
		}

		return questions;
	}

	@Transactional
	@Override
	public SurveyQuestionBean createSurveyQuestion(SurveyQuestionBean question) {

		validator.validateSurveyQuestionSaveInput(question);

		QuestionDO questionDO = new QuestionDO();
		questionDO.setModifiedTime(Instant.now());
		questionDO.setQuestionText(question.getQuestionText());
		questionDO.setStatus(QUESTION_STATUS_ACTIVE);
		questionDO = questionsDao.saveSurveyQuestion(questionDO);

		if (question.getOptions() != null) {
			answerOptionsSupport.saveAnswerOptions(question.getOptions(), questionDO.getQuestionId());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Saved survey quetion. id={}", questionDO.getQuestionId());
		}
		return SurveyQuestionBean.ofId(questionDO.getQuestionId());
	}

	@Override
	@Transactional
	public void updateSurveyQuestion(SurveyQuestionBean question) {

		validator.validateSurveyQuestionUpdateInput(question);

		QuestionDO questionDO = questionsDao.getQuestion(question.getQuestionId());

		validator.validateQuestionDO(questionDO, question.getQuestionId());

		questionDO.setQuestionText(question.getQuestionText());
		questionDO.setModifiedTime(Instant.now());

		if (question.getOptions() != null) {
			answerOptionsSupport.updateAnswerOptions(question.getOptions(), question.getQuestionId());
		}
		questionsDao.updateSurveyQuestion(questionDO);
	}

	@Override
	@Transactional
	public void deleteSurveyQuestion(Integer questionId) {

		QuestionDO questionDO = questionsDao.getQuestion(questionId);
		validator.validateQuestionDO(questionDO, questionId);
		questionDO.setStatus(QUESTION_STATUS_INACTIVE);
		questionsDao.updateStatus(questionDO);
		answerOptionsSupport.deleteOptionsOfQuestion(questionId);
	}

}
