package com.mls.survey.manager.dao.bean;

public class AnswerOptionDO {

	private Integer optionId;

	private Integer questionId;

	private String description;

	private Long voteCount;

	public Integer getOptionId() {
		return optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
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

	@Override
	public int hashCode() {
		return this.optionId != null ? this.optionId.hashCode() : 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof AnswerOptionDO)) {
			return false;
		}
		AnswerOptionDO other = (AnswerOptionDO) obj;
		if (this.optionId != null && other.optionId != null && this.optionId.equals(other.optionId)) {
			return true;
		}
		return false;
	}
}
