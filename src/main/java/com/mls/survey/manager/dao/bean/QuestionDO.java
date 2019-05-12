package com.mls.survey.manager.dao.bean;

import java.time.Instant;

public class QuestionDO {

	private Integer questionId;

	private String questionText;

	private Instant modifiedTime;

	private Integer status;

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public Instant getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Instant modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return questionId != null ? questionId.hashCode() : 1;

	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof QuestionDO)) {
			return false;
		}
		QuestionDO other = (QuestionDO) obj;
		if (questionId != null && other.questionId != null && questionId.equals(other.questionId)) {
			return true;
		}
		return false;

	}
}
