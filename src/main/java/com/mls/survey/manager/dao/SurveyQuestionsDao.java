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
			ps.setInt(2, question.getStatus());
			ps.setTimestamp(3, new Timestamp(question.getModifiedTime().toEpochMilli()));
			return ps;
		}, keyHolder);

		logger.debug("keys =={} ", keyHolder.getKeys());
		question.setQuestionId(((Number) keyHolder.getKeys().get("question_id")).intValue());
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

		return jdbcTemplate.query(SQL_GET_QUESTION, pss -> pss.setInt(1, questionId), (rs) -> {
			if (!rs.next()) {
				return null;
			}
			QuestionDO question = new QuestionDO();
			question.setQuestionId(rs.getInt("question_id"));
			question.setQuestionText(rs.getString("question_text"));
			question.setModifiedTime(Instant.ofEpochMilli(rs.getTimestamp("modified_time").getTime()));
			question.setStatus(rs.getInt("status"));
			return question;
		});
	}

	public List<QuestionDO> getQuestions(Integer status) {

		return jdbcTemplate.query(con -> {
			PreparedStatement ps = con.prepareStatement(SQL_GET_QUESTIONS_OF_STATUS);
			ps.setInt(1, status);
			return ps;
		}, (rs, i) -> {
			QuestionDO question = new QuestionDO();
			question.setQuestionId(rs.getInt("question_id"));
			question.setQuestionText(rs.getString("question_text"));
			question.setModifiedTime(Instant.ofEpochMilli(rs.getTimestamp("modified_time").getTime()));
			return question;
		});
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
				question.setQuestionId(rs.getInt("question_id"));

				AnswerOptionDO answer = new AnswerOptionDO();
				answer.setOptionId(rs.getInt("option_id"));
				answer.setDescription(rs.getString("description"));
				answer.setVoteCount(rs.getLong("vote_count"));

				List<AnswerOptionDO> answerList;

				if (!answerTab.containsKey(question)) {
					question.setQuestionText(rs.getString("question_text"));
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

	private static final String SQL_INSERT_QUESTION = "insert into survey_question(question_text, status, modified_time) values(?, ?, ?)";
	private static final String SQL_UPDATE_QUESTION = "update survey_question set question_text=?, modified_time=? where question_id=?";
	private static final String SQL_UPDATE_QUESTION_STATUS = "update survey_question set status=? where question_id=?";
	private static final String SQL_GET_QUESTIONS_OF_STATUS = "select * from survey_question where status = ?";
	private static final String SQL_GET_QUESTION = "select * from survey_question where question_id=?";
	private static final String SQL_GET_QUESTIONS_ANSWERS_RESULT = "select q.question_id, q.question_text, o.option_id, o.description, o.vote_count "
			+ "from survey_question q left outer join answer_option o on o.question_id = q.question_id where q.status = ?";

}
