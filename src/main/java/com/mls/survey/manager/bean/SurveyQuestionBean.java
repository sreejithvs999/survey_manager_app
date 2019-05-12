package com.mls.survey.manager.bean;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class SurveyQuestionBean {

	private Integer questionId;

	private String questionText;

	private Instant modifiedTime;

	private Integer status;

	private List<AnswerOptionBean> options = new ArrayList<AnswerOptionBean>();

	private Long totalResponse;

	public SurveyQuestionBean() {
	}

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

	public List<AnswerOptionBean> getOptions() {
		return options;
	}

	public void setOptions(List<AnswerOptionBean> options) {
		this.options = options;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTotalResponse() {
		return totalResponse;
	}

	public void setTotalResponse(Long totalResponse) {
		this.totalResponse = totalResponse;
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
		if (obj == null || !(obj instanceof SurveyQuestionBean)) {
			return false;
		}
		SurveyQuestionBean other = (SurveyQuestionBean) obj;
		if (questionId != null && other.questionId != null) {
			return questionId.equals(other.questionId);
		}
		return false;
	}

	public static SurveyQuestionBean ofId(Integer id) {
		SurveyQuestionBean bean = new SurveyQuestionBean();
		bean.setQuestionId(id);
		return bean;
	}
}
