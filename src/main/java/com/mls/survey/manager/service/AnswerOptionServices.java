package com.mls.survey.manager.service;

import java.util.List;

import com.mls.survey.manager.bean.AnswerOptionBean;

public interface AnswerOptionServices {

	List<AnswerOptionBean> getAnswerOptions(Integer questionId);

	AnswerOptionBean saveAnswerOption(AnswerOptionBean answerOption);

	void updateAnswerOption(AnswerOptionBean answerOption);

	void deleteAnswerOption(AnswerOptionBean answerOption);

}
