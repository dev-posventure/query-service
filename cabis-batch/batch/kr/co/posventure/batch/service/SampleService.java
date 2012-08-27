package kr.co.posventure.batch.service;

import org.apache.log4j.Logger;
import kr.co.daewoobrenic.domain.CtSqlInfo;
import kr.co.daewoobrenic.service.AbstractSqlQueryService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hdlee
 */
@Service
public class SampleService extends AbstractSqlQueryService {
	
	private static final Logger logger = Logger.getLogger(SampleService.class);

	/**
	 * 서비스 실행 모든 비지니스 로직은 여기다 넣는다.
	 * @return
	 */
	@Transactional
	public boolean excute(String date) {
		if (logger.isDebugEnabled()) {
			logger.debug("excute() - start");
		}
		try {
			create();
			read();
			update();
			delete();
			getSqlQuery();
			getSqlQueryForNamedQuery();
		} catch (Exception e) {
			logger.warn("excute() - exception ignored", e);
			return false;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("excute() - end");
		}
		return true;
	}

	private void getSqlQueryForNamedQuery() {
		CtSqlInfo ctSqlInfo = new CtSqlInfo("AAMFA_ASETDEPRBC",4);
	    String sql = getSqlQueryService().getSqlQueryForNamedParameter(ctSqlInfo);
	    SqlParameterSource namedParameters = new MapSqlParameterSource("condition", "");
	    System.out.println(getNamedParameterJdbcTemplate().queryForList(sql, namedParameters));
    }

	private void getSqlQuery() {
		CtSqlInfo ctSqlInfo = new CtSqlInfo("AAMFA_ASETDEPRBC",4);
	    String sql = getSqlQueryService().getSqlQuery(ctSqlInfo);
	    System.out.println(getJdbcTemplate().queryForList(sql, new Object[] {"", ""}));
    }

	public void delete() {
		getJdbcTemplate().update("delete into t_actor (first_name, surname) values (?, ?)", new Object[] { "Leonor", "Watling" });
	}

	public void update() {
		getJdbcTemplate().update("update into t_actor (first_name, surname) values (?, ?)", new Object[] { "Leonor", "Watling" });
	}

	public void read() {
		getJdbcTemplate().queryForList("select * from temp");
	}

	public void create() {
		getJdbcTemplate().update("insert into t_actor (first_name, surname) values (?, ?)", new Object[] { "Leonor", "Watling" });
	}
}
