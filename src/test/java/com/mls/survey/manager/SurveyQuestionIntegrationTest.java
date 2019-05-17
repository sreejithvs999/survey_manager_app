package com.mls.survey.manager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Optional;

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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc 
public class SurveyQuestionIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(SurveyQuestionIntegrationTest.class);
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	MappingJackson2HttpMessageConverter converter;
	
	private SurveyQuestionTestHelper helper;
	
	@Before
	public void init() {
		helper = new SurveyQuestionTestHelper(mockMvc, converter, jdbcTemplate);
	}
	
	@Test
	public void testSurveyQuestionCreateUpdateDelete() throws Exception {
		
		String payload = "{\"questionText\" : \"What do you consider most while buying new Handy?\","
				+ "\"options\" : ["
				+ "{\"description\" : \"Well known Brand Name\"},"
				+ "{\"description\" : \"Higher Discounts\"},"
				+ "{\"description\" : \"Advanced Features\"},"
				+ "{\"description\" : \"Lower Price\"}"
				+ "]}";
		
		MvcResult result = helper.callCreateQuestion(payload);		
		SurveyQuestionBean question = helper.getSurveyQuestionBean(result);
		assertTrue(question.getQuestionId() > 0);
		
		result = helper.callGetAllQuestion();
		List<SurveyQuestionBean> list = helper.getSurveyQuestionBeans(result);
		assertTrue(list.size() > 0);
		question = list.get(0);
		assertTrue(question.getQuestionText().equals("What do you consider most while buying new Handy?"));

		result = helper.callGetAnswerOptions(question.getQuestionId());		
		List<AnswerOptionBean> optionList = helper.getAnswerOptions(result);

		
		payload = "{\"questionText\" : \"What do you consider most while buying new Mobile Phone?\","
				+ "\"options\" : " + converter.getObjectMapper().writeValueAsString(optionList) 
				+ "}";		
		result = helper.callUpdateSurveyQuestion(payload, question.getQuestionId());
		
		result = helper.callGetAllQuestion();		
		list = helper.getSurveyQuestionBeans(result);		
		assertTrue(list.size() > 0);
		question = list.get(0);
		assertTrue(question.getQuestionText().equals("What do you consider most while buying new Mobile Phone?"));
		
		helper.callDeleteSurveyQuestion(question.getQuestionId());
		result = helper.callGetAllQuestion();		
		list = helper.getSurveyQuestionBeans(result);		
		assertTrue("There must not be any active survey questions.", list.size() == 0);
	}
	

	@Test
	public void testAnswerOptionCreateUpdateDelete() throws Exception {
		
		String payload = "{\"questionText\" : \"How do you prefer to buy grocery items?\"}";
		
		MvcResult result = helper.callCreateQuestion(payload);		
		SurveyQuestionBean question = helper.getSurveyQuestionBean(result);
		
		payload = "{\"description\" : \"Buy online from trusted stores.\"}";
		result=helper.callCreateAnswerOption(question.getQuestionId(), payload);
		AnswerOptionBean option = helper.getAnswerOption(result);
		assertNotNull(option);
		
		result = helper.callGetAnswerOptions(question.getQuestionId());
		List<AnswerOptionBean> list = helper.getAnswerOptions(result);
		assertTrue(list.size() > 0);
		option = list.get(0);
		assertTrue(option.getDescription().equals("Buy online from trusted stores."));
		
		payload =  "{\"description\" : \"Online Stores\"}";
		helper.callEditAnswerOption(question.getQuestionId(), option.getOptionId(), payload);
		
		result = helper.callGetAnswerOptions(question.getQuestionId());
		list = helper.getAnswerOptions(result);
		assertTrue(list.size() > 0);
		option = list.get(0);
		assertTrue(option.getDescription().equals("Online Stores"));
		
		helper.callDeleteAnswerOption(question.getQuestionId(), option.getOptionId());
		result = helper.callGetAnswerOptions(question.getQuestionId());
		list = helper.getAnswerOptions(result);
		assertTrue("There must be no answer options", list.size() == 0);
	}
	
	@Test
	public void testMultipleAnswerOptionsUpdate() throws Exception {
		
		String payload = "{\"questionText\" : \"What are your motivations to buy Clothing items online?\","
				+ "\"options\" : ["
				+ "{\"description\" : \"All branded items are available online.\"},"
				+ "{\"description\" : \"Large number of collections.\"}"
				+ "]}";
		MvcResult result = helper.callCreateQuestion(payload);		
		SurveyQuestionBean question = helper.getSurveyQuestionBean(result);
		
		result = helper.callGetAnswerOptions(question.getQuestionId());
		List<AnswerOptionBean> list = helper.getAnswerOptions(result);
		
		Optional<AnswerOptionBean> option = list.stream()
				.filter(opt -> opt.getDescription().equals("All branded items are available online.")).findFirst();
		assertTrue(option.isPresent());
		Integer brandOptionId  = option.get().getOptionId();
		payload = "{\"questionText\" : \"What are your motivations to buy Clothing items online?\","
				+ "\"options\" : ["
				+ "{\"description\" : \"Latest items from top brands.\", \"optionId\" : \"" + brandOptionId + "\"}"
				+ "]}";
		helper.callUpdateSurveyQuestion(payload, question.getQuestionId());
		
		result = helper.callGetAnswerOptions(question.getQuestionId());
		list = helper.getAnswerOptions(result);
		assertTrue("There must be 3 answer options.", list.size() == 2);
		option = list.stream().filter(opt -> opt.getDescription().equals("Latest items from top brands.")).findFirst();
		assertTrue(option.isPresent());
		assertTrue(option.get().getOptionId().equals(brandOptionId));		
	}
		
	@After
	public void cleanUpTestData() {
		
		logger.info("Clearing test data from tables.");
		helper.cleanUpTestData();
	}
}
