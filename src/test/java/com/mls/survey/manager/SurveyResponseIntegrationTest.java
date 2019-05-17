package com.mls.survey.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.mls.survey.manager.bean.AnswerOptionBean;
import com.mls.survey.manager.bean.SurveyQuestionBean;
import com.mls.survey.manager.bean.SurveyResponseForm;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc 
public class SurveyResponseIntegrationTest {

private static final Logger logger = LoggerFactory.getLogger(SurveyQuestionIntegrationTest.class);
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	MappingJackson2HttpMessageConverter converter;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SurveyQuestionTestHelper helper;
	
	@Before
	public void init() {
		helper = new SurveyQuestionTestHelper(mockMvc, converter, jdbcTemplate);
	}
	
	@Test
	public void testSurveyResponsePoll() throws Exception {
		
		String payload = "{\"questionText\" : \"How often you buy Nutrition or Healthcare supplements through online stores?\","
				+ "\"options\" : ["
				+ "{\"description\" : \"Very Often\"},"
				+ "{\"description\" : \"Once in a while\"},"
				+ "{\"description\" : \"Never\"}"
				+ "]}";
		helper.callCreateQuestion(payload);	
		
		payload = "{\"questionText\" : \"Do you consumes Vitamins supplements?\","
				+ "\"options\" : ["
				+ "{\"description\" : \"Yes\"},"
				+ "{\"description\" : \"No\"}"
				+ "]}";
		helper.callCreateQuestion(payload);	
		
		payload = "{\"questionText\" : \"Where do you prefer buying Nutrition supplements from?\","
				+ "\"options\" : ["
				+ "{\"description\" : \"Medical stores\"},"
				+ "{\"description\" : \"Brands own stores\"},"
				+ "{\"description\" : \"Online stores\"},"
				+ "{\"description\" : \"Other sources\"}"
				+ "]}";
		helper.callCreateQuestion(payload);	
		
		payload = "{\"surveyResponse\" : ["
				+ "{\"questionId\" : \"1\", \"optionId\" : \"1\"},"
				+ "{\"questionId\" : \"2\", \"optionId\" : \"5\"},"
				+ "{\"questionId\" : \"3\", \"optionId\" : \"8\"}"
				+ "]}";
		helper.callSurveyPoll(payload);
		
		payload = "{\"surveyResponse\" : ["
				+ "{\"questionId\" : \"1\", \"optionId\" : \"2\"},"
				+ "{\"questionId\" : \"2\", \"optionId\" : \"4\"},"
				+ "{\"questionId\" : \"3\", \"optionId\" : \"6\"}"
				+ "]}";
		helper.callSurveyPoll(payload);
		
		payload = "{\"surveyResponse\" : ["
				+ "{\"questionId\" : \"1\", \"optionId\" : \"1\"},"
				+ "{\"questionId\" : \"2\", \"optionId\" : \"4\"},"
				+ "{\"questionId\" : \"3\", \"optionId\" : \"6\"}"
				+ "]}";
		helper.callSurveyPoll(payload);
		
		MvcResult result = helper.callSurveyResult();
		SurveyResponseForm form = helper.getSurveyResponseForm(result);
		List<SurveyQuestionBean> list = form.getSurveyResult();
		assertTrue(list.size() == 3);
			    
		SurveyQuestionBean question = list.stream().filter(q -> q.getQuestionId().equals(1)).findFirst().get();
		assertEquals(question.getOptions().size(), 3);
		AnswerOptionBean option = question.getOptions().stream().filter(o -> o.getOptionId().equals(1)).findFirst().get();
		assertEquals(option.getDescription(), "Very Often");
		assertEquals(option.getVoteCount().intValue(), 2);
		option = question.getOptions().stream().filter(o -> o.getOptionId().equals(2)).findFirst().get();
		assertEquals(option.getDescription(), "Once in a while");
		assertEquals(option.getVoteCount().intValue(), 1);
		
		question = list.stream().filter(q -> q.getQuestionId().equals(2)).findFirst().get();
		assertEquals(question.getOptions().size(), 2);
		option = question.getOptions().stream().filter(o -> o.getOptionId().equals(4)).findFirst().get();
		assertEquals(option.getDescription(), "Yes");
		assertEquals(option.getVoteCount().intValue(), 2);
		option = question.getOptions().stream().filter(o -> o.getOptionId().equals(5)).findFirst().get();
		assertEquals(option.getDescription(), "No");
		assertEquals(option.getVoteCount().intValue(), 1);
		
		question = list.stream().filter(q -> q.getQuestionId().equals(3)).findFirst().get();
		assertEquals(question.getOptions().size(), 4);
		option = question.getOptions().stream().filter(o -> o.getOptionId().equals(6)).findFirst().get();
		assertEquals(option.getDescription(), "Medical stores");
		assertEquals(option.getVoteCount().intValue(), 2);
		option = question.getOptions().stream().filter(o -> o.getOptionId().equals(8)).findFirst().get();
		assertEquals(option.getDescription(), "Online stores");
		assertEquals(option.getVoteCount().intValue(), 1);
		
	}
	
	@After
	public void cleanUp() throws Exception {
		logger.info("Clearing test data from tables.");
		helper.cleanUpTestData();
	}
}
