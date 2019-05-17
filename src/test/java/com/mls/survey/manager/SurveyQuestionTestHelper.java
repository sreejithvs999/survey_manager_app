package com.mls.survey.manager;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mls.survey.manager.bean.AnswerOptionBean;
import com.mls.survey.manager.bean.SurveyQuestionBean;
import com.mls.survey.manager.bean.SurveyResponseForm;

public class SurveyQuestionTestHelper {

	private MockMvc mockMvc;

	MappingJackson2HttpMessageConverter converter;
	private JdbcTemplate jdbcTemplate;
	
	public SurveyQuestionTestHelper(MockMvc mockMvc, MappingJackson2HttpMessageConverter converter,
			JdbcTemplate jdbcTemplate) {
		this.mockMvc = mockMvc;
		this.converter = converter;
		this.jdbcTemplate = jdbcTemplate;
	}

	public MvcResult callCreateQuestion(String payload) throws Exception {
		return mockMvc
				.perform(post("/surveyQuestion")
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(payload))
				.andDo(print())
				.andExpect(status().is(HttpStatus.CREATED.value()))
				.andReturn();
	}

	public MvcResult callGetAllQuestion() throws Exception {
		return mockMvc.perform(get("/surveyQuestion"))
				.andDo(print())
				.andExpect(status().is(200))
				.andReturn();
	}

	public MvcResult callUpdateSurveyQuestion(String payload, Integer questionId) throws Exception {
		return mockMvc.perform(put("/surveyQuestion/{questionId}", questionId)
						.contentType(MediaType.APPLICATION_JSON_UTF8).content(payload))
				.andDo(print())
				.andExpect(status().is(204))
				.andReturn();
	}

	public MvcResult callDeleteSurveyQuestion(Integer questionId) throws Exception {
		return mockMvc.perform(delete("/surveyQuestion/{questionId}", questionId))
				.andDo(print())
				.andExpect(status().is(204))
				.andReturn();
	}
	
	public MvcResult callCreateAnswerOption(Integer questionId, String content) throws Exception {
		return mockMvc.perform(post("/surveyQuestion/{questionId}/answerOption", questionId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content))
				.andDo(print())
				.andExpect(status().is(201))
				.andReturn();
	}
	
	public MvcResult callGetAnswerOptions(Integer questionId) throws Exception {
		return mockMvc.perform(get("/surveyQuestion/{questionId}/answerOption", questionId))
				.andDo(print())
				.andExpect(status().is(200))
				.andReturn();
	}
	
	public MvcResult callEditAnswerOption(Integer questionId, Integer optionId, String content) throws Exception {
		return mockMvc.perform(put("/surveyQuestion/{questionId}/answerOption/{optionId}", questionId, optionId)
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(content))
				.andDo(print())
				.andExpect(status().is(204))
				.andReturn();
	}
	
	public MvcResult callDeleteAnswerOption(Integer questionId, Integer optionId) throws Exception {
		return mockMvc.perform(delete("/surveyQuestion/{questionId}/answerOption/{optionId}", questionId, optionId))
			.andDo(print())
			.andExpect(status().is(204))
			.andReturn();		
	}
	
	public MvcResult callSurveyPoll(String content) throws Exception {
		return mockMvc.perform(post("/surveyQuestion/poll")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content))
				.andDo(print())
				.andExpect(status().is(204))
				.andReturn();
	}
	
	public MvcResult callSurveyResult() throws Exception {
		return mockMvc.perform(get("/surveyQuestion/results"))
				.andDo(print())
				.andExpect(status().is(200))
				.andReturn();
	}
	
	public SurveyQuestionBean getSurveyQuestionBean(MvcResult result) throws Exception {
		return converter.getObjectMapper().readValue(result.getResponse().getContentAsString(),
				new TypeReference<SurveyQuestionBean>() {});
	}

	public List<SurveyQuestionBean> getSurveyQuestionBeans(MvcResult result) throws Exception {
		return converter.getObjectMapper().readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<SurveyQuestionBean>>() {});
	}

	public List<AnswerOptionBean> getAnswerOptions(MvcResult result) throws Exception {
		return converter.getObjectMapper().readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<AnswerOptionBean>>() {});
	}
	
	public AnswerOptionBean getAnswerOption(MvcResult result) throws Exception {
		return converter.getObjectMapper().readValue(result.getResponse().getContentAsString(),
				new TypeReference<AnswerOptionBean>() {});
	}
	
	public SurveyResponseForm getSurveyResponseForm(MvcResult result) throws Exception {
		return converter.getObjectMapper().readValue(result.getResponse().getContentAsString(), 
				new TypeReference<SurveyResponseForm>() {});
	}
	
	public void cleanUpTestData() {
		
		jdbcTemplate.update("truncate table answer_option");
		jdbcTemplate.update("truncate table survey_question cascade");
		jdbcTemplate.query("select setval('answer_option_option_id_seq', 1, false)", rs -> {});
		jdbcTemplate.query("select setval('survey_question_question_id_seq', 1, false)", rs -> {});
	}
}
