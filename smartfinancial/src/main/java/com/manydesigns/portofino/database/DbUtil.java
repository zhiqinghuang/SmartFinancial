package com.manydesigns.portofino.database;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.Statement;

public class DbUtil {
	public final static Logger logger = LoggerFactory.getLogger(DbUtil.class);

	public static void closeResultSetAndStatement(ResultSet rs) {
		try {
			Statement st = rs.getStatement();
			DbUtils.closeQuietly(st);
		} catch (Throwable e) {
			logger.debug("Could not close statement", e);
		}
		try {
			DbUtils.closeQuietly(rs);
		} catch (Throwable e) {
			logger.debug("Could not close result set", e);
		}
	}
}