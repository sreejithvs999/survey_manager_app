package com.mls.survey.manager.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mls.survey.manager.bean.AnswerOptionBean;
import com.mls.survey.manager.dao.AnswerOptionsDao;
import com.mls.survey.manager.dao.SurveyQuestionsDao;
import com.mls.survey.manager.dao.bean.AnswerOptionDO;
import com.mls.survey.manager.dao.bean.QuestionDO;
import com.mls.survey.manager.service.AnswerOptionServices;

@Service
public class DefaultAnswerOptionsServices implements AnswerOptionServices {

	private static final Logger logger = LoggerFactory.getLogger(DefaultAnswerOptionsServices.class);

	@Autowired
	private AnswerOptionsDao optionsDao;

	@Autowired
	private SurveyQuestionsDao questionsDao;

	@Autowired
	private SurveyQuestionsValidator validator;

	private static final int QUESTION_STATUS_ACTIVE = 1;

	@Override
	public List<AnswerOptionBean> getAnswerOptions(Integer questionId) {

		QuestionDO questionDo = questionsDao.getQuestion(questionId);
		validator.validateQuestionDO(questionDo, questionId);

		List<AnswerOptionDO> answerDOList = optionsDao.getAnswerOptions(questionId);
		List<AnswerOptionBean> options = new ArrayList<AnswerOptionBean>();
		for (AnswerOptionDO answerDO : answerDOList) {
			AnswerOptionBean option = new AnswerOptionBean();
			option.setOptionId(answerDO.getOptionId());
			option.setDescription(answerDO.getDescription());
			options.add(option);
		}
		return options;
	}

	@Override
	public AnswerOptionBean saveAnswerOption(AnswerOptionBean answerOption) {

		validator.validateAnswerOptionSaveInput(answerOption);
		AnswerOptionDO answerOptionDO = new AnswerOptionDO();
		QuestionDO questionDo = questionsDao.getQuestion(answerOption.getQuestion().getQuestionId());
		validator.validateQuestionDO(questionDo, answerOption.getQuestion().getQuestionId());
		answerOptionDO.setQuestionId(questionDo.getQuestionId());
		answerOptionDO.setDescription(answerOption.getDescription());
		answerOptionDO.setVoteCount(0L);
		optionsDao.saveAnswerOption(answerOptionDO);
		return AnswerOptionBean.ofId(answerOption.getOptionId());
	}

	@Override
	public void updateAnswerOption(AnswerOptionBean answerOption) {

		validator.validateAnswerOptionUpdateInput(answerOption);
		QuestionDO questionDo = questionsDao.getQuestion(answerOption.getQuestion().getQuestionId());
		validator.validateQuestionDO(questionDo, answerOption.getQuestion().getQuestionId());

		AnswerOptionDO answerOptionDO = optionsDao.getAnswerOption(answerOption.getOptionId());
		validator.validateAnswerOptionDO(answerOptionDO, questionDo.getQuestionId(), answerOption.getOptionId());

		answerOptionDO.setDescription(answerOption.getDescription());
		optionsDao.updateAnswerOption(answerOptionDO);
	}

	@Override
	public void deleteAnswerOption(AnswerOptionBean answerOption) {

		validator.validateOptionId(answerOption.getOptionId());
		validator.validateQuestionId(answerOption.getQuestion().getQuestionId());

		QuestionDO questionDo = questionsDao.getQuestion(answerOption.getQuestion().getQuestionId());
		validator.validateQuestionDO(questionDo, answerOption.getQuestion().getQuestionId());
		AnswerOptionDO answerOptionDO = optionsDao.getAnswerOption(answerOption.getOptionId());
		validator.validateAnswerOptionDO(answerOptionDO, questionDo.getQuestionId(), answerOption.getOptionId());

		optionsDao.deleteOption(answerOption.getOptionId());
	}

}
