package org.dbadmin.repository.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.DQConfig;
import org.dbadmin.model.DQConfigProxy;
import org.dbadmin.model.DQParams;
import org.dbadmin.model.DQRule;
import org.dbadmin.repository.DqConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * JDBC-based implementation of the {@link DqConfigRepository} interface.
 * @author D.K. 8/31/2016
 */
@Repository
public class JdbcDqConfigRepositoryImpl implements DqConfigRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public JdbcDqConfigRepositoryImpl(DataSource systemDataSource) throws SQLException {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(systemDataSource);
	}


	@Override
	public DQConfig findById(int id) throws DataAccessException {
		DQConfigProxy configProxy = null;
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			configProxy = this.namedParameterJdbcTemplate.queryForObject(
					"SELECT "  
							+ "  id" 
							+ ", parameters_map_id" 
							+ ", dq_rule" 
							+ ", comments"
							+ " FROM dq_config" 
							+ " WHERE id= :id",
					params, BeanPropertyRowMapper.newInstance(DQConfigProxy.class));
		} catch (EmptyResultDataAccessException ex) {
			// NOP. Just no parameters map for this config was found.
		}
		
		// these parameters will be added on another stage of the object completion in other repository
		Connection connection = null;
		String tableName = null;
		String columnName = null;
		DQRule rule = null;
		Map<String, String> dqParamsMap = null;
		if (configProxy != null) {
			rule = this.getDQRule(configProxy.getDqRule());

			List<DQParams> dqParams = this.getDqParameters(configProxy.getParametersMapId());

			dqParamsMap = new HashMap<>();
			for (DQParams dqp : dqParams) {
				dqParamsMap.put(dqp.getMapKey(), dqp.getMapValue());
			}
		}
		
	
		DQConfig config = new DQConfig(connection, tableName, columnName, rule, dqParamsMap);		
		
		return config;
	}

	private List<DQParams> getDqParameters (int mapId) {
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("mapId", mapId);
		List<DQParams> dqParams = this.namedParameterJdbcTemplate.query(
				"SELECT " 
						+ "  id" 
						+ ", map_id" 						
						+ ", map_key" 
						+ ", map_value" 
						+ ", comments"
						+ " FROM parameters_map "
						+ " WHERE map_id = :mapId",
				namedParameters,	
				BeanPropertyRowMapper.newInstance(DQParams.class));
		return dqParams;
	}
	
	private DQRule getDQRule(String rule) {
		
		switch(rule.trim()) {
		
		case "DATE_CHECK": 				return DQRule.DATE_CHECK;
		case "LENGTH_CHECK": 			return DQRule.LENGTH_CHECK;
		case "NULL_CHECK": 				return DQRule.NULL_CHECK;
		case "OUTLIER": 				return DQRule.OUTLIER;		
		case "SET_CHECK": 				return DQRule.SET_CHECK;	
		case "PHONE_CHECK": 			return DQRule.PHONE_CHECK;	
		case "ADDRESS_CHECK": 			return DQRule.ADDRESS_CHECK;	
		case "DUP_CHECK": 				return DQRule.DUP_CHECK;	
		case "CANDIDATE_KEY_CHECK": 	return DQRule.CANDIDATE_KEY_CHECK;
		case "ORPHAN_DETECT":			return DQRule.ORPHAN_DETECT;
		case "INVALID_PK_DETECT":		return DQRule.INVALID_PK_DETECT;
		case "DATA_TYPE_DETECT":		return DQRule.DATA_TYPE_DETECT;
		case "CLUSTER_DETECT":			return DQRule.CLUSTER_DETECT;
		case "FITTING_DISTRIBUTION":	return DQRule.FITTING_DISTRIBUTION;
		case "ABNORMAL_DUP_CHECK":		return DQRule.ABNORMAL_DUP_CHECK;
		case "DATE_PARSABILITY":		return DQRule.DATE_PARSABILITY;		
		case "NUMERIC_CONVERTION_DETECT": return DQRule.NUMERIC_CONVERTION_DETECT;
		default : 						return DQRule.NA;
		}
	}

	@Override
	public void save(DQConfig config) throws DataAccessException {
		//TODO save to tables dq_config and parameters_map
	}


	@Override
	public void delete(DQConfig config) throws DataAccessException {
		//TODO delete from tables dq_config and parameters_map
	}
}
