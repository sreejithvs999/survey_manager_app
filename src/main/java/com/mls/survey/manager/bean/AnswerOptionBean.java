package com.mls.survey.manager.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class AnswerOptionBean {

	private Integer optionId;

	private SurveyQuestionBean question;

	private String description;

	private Long voteCount;

	private Double percent;

	public Integer getOptionId() {
		return optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	public SurveyQuestionBean getQuestion() {
		return question;
	}

	public void setQuestion(SurveyQuestionBean question) {
		this.question = question;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Long voteCount) {
		this.voteCount = voteCount;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	@Override
	public int hashCode() {
		return optionId != null ? optionId.hashCode() : 1;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof AnswerOptionBean)) {
			return false;
		}
		AnswerOptionBean other = (AnswerOptionBean) obj;
		if (optionId != null && other.optionId != null) {
			return optionId.equals(other.optionId);
		}
		return false;
	}

	public static AnswerOptionBean ofId(Integer id) {
		AnswerOptionBean bean = new AnswerOptionBean();
		bean.setOptionId(id);
		return bean;
	}
}
