package com.mls.survey.manager.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.mls.survey.manager.dao.bean.AnswerOptionDO;

@Repository
public class AnswerOptionsDao {

	private static final Logger logger = LoggerFactory.getLogger(AnswerOptionsDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public AnswerOptionDO saveAnswerOption(AnswerOptionDO option) {

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {

			PreparedStatement ps = con.prepareStatement(SQL_INSERT_OPTION);
			ps.setInt(1, option.getQuestionId());
			ps.setString(1, option.getDescription());
			return ps;
		}, keyHolder);

		option.setOptionId(keyHolder.getKey().intValue());
		return option;
	}

	public void saveAnswerOptions(List<AnswerOptionDO> options) {

		jdbcTemplate.batchUpdate(SQL_INSERT_OPTION, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {

				ps.setInt(1, options.get(i).getQuestionId());
				ps.setString(2, options.get(i).getDescription());
			}

			@Override
			public int getBatchSize() {
				return options.size();
			}
		});
	}

	public void updateAnswerOption(AnswerOptionDO option) {

		jdbcTemplate.update(SQL_UPDATE_OPTION, ps -> {
			ps.setString(1, option.getDescription());
			ps.setInt(2, option.getQuestionId());
		});
	}

	public void updateAnswerOptions(List<AnswerOptionDO> options) {

		jdbcTemplate.batchUpdate(SQL_UPDATE_OPTION, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, options.get(i).getDescription());
				ps.setInt(2, options.get(i).getOptionId());
			}

			@Override
			public int getBatchSize() {
				return options.size();
			}
		});
	}

	public void deleteOption(Integer optionId) {
		jdbcTemplate.update(SQL_DELETE_OPTION, ps -> {
			ps.setInt(1, optionId);
		});
	}

	public void deleteOptionByQuestionId(Integer questionId) {
		jdbcTemplate.update(SQL_DELETE_OPTIONS_BY_QUESTION_ID, ps -> {
			ps.setInt(1, questionId);
		});
	}

	public List<AnswerOptionDO> getAnswerOptions(Integer questionId) {
		return jdbcTemplate.query(con -> {
			PreparedStatement ps = con.prepareStatement(SQL_GET_OPTIONS_OF_QUESTION);
			ps.setInt(1, questionId);
			return ps;
		}, (rs, i) -> {
			AnswerOptionDO option = new AnswerOptionDO();
			mapOptionBean(option, rs);
			return option;
		});
	}

	public AnswerOptionDO getAnswerOption(Integer optionId) {
		return jdbcTemplate.query(con -> {
			PreparedStatement ps = con.prepareStatement(SQL_GET_OPTION);
			ps.setInt(1, optionId);
			return ps;
		}, (rs) -> {
			AnswerOptionDO option = new AnswerOptionDO();
			mapOptionBean(option, rs);
			return option;
		});
	}

	public void voteForAnswerOptions(Integer[] questionIds, Integer[] optionIds) {

		jdbcTemplate.batchUpdate(SQL_UPDATE_OPTIONS_VOTES, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, questionIds[i]);
				ps.setInt(2, optionIds[i]);
			}

			@Override
			public int getBatchSize() {
				return questionIds.length;
			}
		});
	}

	private void mapOptionBean(AnswerOptionDO option, ResultSet rs) throws SQLException {
		option.setOptionId(rs.getInt(""));
		option.setDescription(rs.getString(""));
		option.setQuestionId(rs.getInt(""));
		option.setVoteCount(rs.getLong(""));
	}

	private static final String SQL_INSERT_OPTION = "";
	private static final String SQL_UPDATE_OPTION = "";
	private static final String SQL_DELETE_OPTION = "";
	private static final String SQL_DELETE_OPTIONS_BY_QUESTION_ID = "";
	private static final String SQL_GET_OPTIONS_OF_QUESTION = "";
	private static final String SQL_GET_OPTION = "";
	private static final String SQL_UPDATE_OPTIONS_VOTES = "";

}
