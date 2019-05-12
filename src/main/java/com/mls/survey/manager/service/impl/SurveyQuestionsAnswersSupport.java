package com.mls.survey.manager.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mls.survey.manager.bean.AnswerOptionBean;
import com.mls.survey.manager.dao.AnswerOptionsDao;
import com.mls.survey.manager.dao.bean.AnswerOptionDO;
import com.mls.survey.manager.exceptions.DataNotFoundException;

@Component
public class SurveyQuestionsAnswersSupport {

	private static final Logger logger = LoggerFactory.getLogger(SurveyQuestionsAnswersSupport.class);

	@Autowired
	private AnswerOptionsDao optionsDao;

	public void saveAnswerOptions(List<AnswerOptionBean> options, Integer questionId) {
		List<AnswerOptionDO> answerDOList = new ArrayList<AnswerOptionDO>();
		for (AnswerOptionBean option : options) {

			AnswerOptionDO answerDO = new AnswerOptionDO();
			answerDO.setQuestionId(questionId);
			answerDO.setDescription(option.getDescription());
			answerDO.setVoteCount(0L);
			answerDOList.add(answerDO);
		}
		optionsDao.saveAnswerOptions(answerDOList);
	}

	public void updateAnswerOptions(List<AnswerOptionBean> options, Integer questionId) {

		Set<AnswerOptionBean> newAnswers = new LinkedHashSet<AnswerOptionBean>(options);

		List<AnswerOptionDO> savedAnswers = optionsDao.getAnswerOptions(questionId);
		List<AnswerOptionDO> updateList = new ArrayList<AnswerOptionDO>();

		for (AnswerOptionBean newAnswer : newAnswers) {
			Optional<AnswerOptionDO> optAnswer = savedAnswers.stream()
					.filter(sa -> sa.getOptionId().equals(newAnswer.getOptionId())).findFirst();
			if (optAnswer.isEmpty()) {
				throw new DataNotFoundException("No answer option found with id: " + newAnswer.getOptionId());

			}
			AnswerOptionDO answerToUpdate = optAnswer.get();
			answerToUpdate.setDescription(newAnswer.getDescription());
			updateList.add(answerToUpdate);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Updating answer options. count={} ", updateList.size());
		}
		optionsDao.updateAnswerOptions(updateList);
	}

	public void deleteOptionsOfQuestion(Integer questionId) {
		optionsDao.deleteOptionByQuestionId(questionId);
	}
}
