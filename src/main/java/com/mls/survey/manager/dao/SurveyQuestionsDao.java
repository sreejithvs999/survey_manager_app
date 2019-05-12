package com.mls.survey.manager.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.mls.survey.manager.dao.bean.AnswerOptionDO;
import com.mls.survey.manager.dao.bean.QuestionDO;
import com.mls.survey.manager.dao.bean.SurveySummaryView;

@Repository
public class SurveyQuestionsDao {

	private static final Logger logger = LoggerFactory.getLogger(SurveyQuestionsDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public QuestionDO saveSurveyQuestion(QuestionDO question) {

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(SQL_INSERT_QUESTION, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, question.getQuestionText());
			ps.setTimestamp(2, new Timestamp(question.getModifiedTime().toEpochMilli()));
			return ps;
		}, keyHolder);

		question.setQuestionId(keyHolder.getKey().intValue());
		if (logger.isDebugEnabled()) {
			logger.debug("Inserted Question data. id={} ", question.getQuestionId());
		}
		return question;
	}

	public void updateSurveyQuestion(QuestionDO question) {

		jdbcTemplate.update(SQL_UPDATE_QUESTION, ps -> {
			ps.setString(1, question.getQuestionText());
			ps.setTimestamp(2, new Timestamp(question.getModifiedTime().toEpochMilli()));
			ps.setInt(3, question.getQuestionId());
		});
	}

	public void updateStatus(QuestionDO question) {
		jdbcTemplate.update(SQL_UPDATE_QUESTION_STATUS, ps -> {
			ps.setInt(1, question.getStatus());
			ps.setInt(2, question.getQuestionId());
		});
	}

	public QuestionDO getQuestion(Integer questionId) {

		return jdbcTemplate.query(SQL_GET_QUESTION, (rs) -> {
			if (!rs.next()) {
				return null;
			}
			QuestionDO question = new QuestionDO();
			question.setQuestionId(rs.getInt(""));
			question.setQuestionText(rs.getString(""));
			question.setModifiedTime(Instant.ofEpochMilli(rs.getTimestamp("").getTime()));
			question.setStatus(rs.getInt(""));
			return question;
		});
	}

	public List<QuestionDO> getQuestions(Integer status) {

		List<QuestionDO> list = new ArrayList<QuestionDO>();
		jdbcTemplate.query(con -> {
			PreparedStatement ps = con.prepareStatement(SQL_GET_QUESTIONS_OF_STATUS);
			ps.setInt(1, status);
			return ps;
		}, (rs, i) -> {
			QuestionDO question = new QuestionDO();
			question.setQuestionId(rs.getInt(""));
			question.setQuestionText(rs.getString(""));
			question.setModifiedTime(Instant.ofEpochMilli(rs.getTimestamp("").getTime()));
			return question;
		});
		return list;
	}

	public List<SurveySummaryView> getSurveyResult(Integer status) {

		return jdbcTemplate.query(con -> {
			PreparedStatement ps = con.prepareStatement(SQL_GET_QUESTIONS_ANSWERS_RESULT);
			ps.setInt(1, status);
			return ps;
		}, rs -> {
			List<SurveySummaryView> results = new ArrayList<SurveySummaryView>();
			Map<QuestionDO, List<AnswerOptionDO>> answerTab = new LinkedHashMap<QuestionDO, List<AnswerOptionDO>>();

			while (rs.next()) {

				QuestionDO question = new QuestionDO();
				question.setQuestionId(rs.getInt(""));

				AnswerOptionDO answer = new AnswerOptionDO();
				answer.setOptionId(rs.getInt(""));
				answer.setDescription(rs.getString(""));
				answer.setVoteCount(rs.getLong(""));

				List<AnswerOptionDO> answerList;

				if (!answerTab.containsKey(question)) {
					question.setQuestionText(rs.getString(""));
					question.setQuestionId(rs.getInt(""));
					answerList = answerTab.compute(question, (k, v) -> new ArrayList<>());
				} else {
					answerList = answerTab.get(question);
				}
				answerList.add(answer);

			}
			answerTab.forEach((k, v) -> {
				SurveySummaryView view = new SurveySummaryView(k, v);
				view.setTotalVotes(v.stream().mapToLong(AnswerOptionDO::getVoteCount).sum());
				results.add(view);
			});

			return results;
		});
	}

	private static final String SQL_INSERT_QUESTION = "";
	private static final String SQL_UPDATE_QUESTION = "";
	private static final String SQL_UPDATE_QUESTION_STATUS = "";
	private static final String SQL_GET_QUESTIONS_OF_STATUS = "";
	private static final String SQL_GET_QUESTION = "";
	private static final String SQL_GET_QUESTIONS_ANSWERS_RESULT = "";

}
