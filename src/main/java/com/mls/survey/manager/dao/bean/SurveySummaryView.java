package com.mls.survey.manager.dao.bean;

import java.util.List;

public class SurveySummaryView {

	private QuestionDO question;

	private List<AnswerOptionDO> answers;

	private Long totalVotes = 0L;

	public SurveySummaryView(QuestionDO question, List<AnswerOptionDO> answers) {
		this.question = question;
		this.answers = answers;
	}

	public QuestionDO getQuestion() {
		return question;
	}

	public void setQuestion(QuestionDO question) {
		this.question = question;
	}

	public List<AnswerOptionDO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerOptionDO> answers) {
		this.answers = answers;
	}

	public Long getTotalVotes() {
		return totalVotes;
	}

	public void setTotalVotes(Long totalVotes) {
		this.totalVotes = totalVotes;
	}

}
