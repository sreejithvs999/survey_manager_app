package com.mls.survey.manager.bean;

public class SurveyResponseEntry {

	private Integer questionId;

	private Integer optionId;

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getOptionId() {
		return optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		if (questionId != null) {
			hash = 31 * hash + questionId.hashCode();
		}
		if (optionId != null) {
			hash = 31 * hash + optionId.hashCode();
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || !(obj instanceof SurveyResponseEntry)) {
			return false;
		}
		SurveyResponseEntry other = (SurveyResponseEntry) obj;
		if (questionId == null || other.questionId == null || !questionId.equals(other.questionId)) {
			return false;
		}
		if (optionId == null || other.optionId == null || !optionId.equals(other.optionId)) {
			return false;
		}
		return true;
	}
}
