package com.manydesigns.portofino.database;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

public class Type {
	public final static Logger logger = LoggerFactory.getLogger(Type.class);

	protected final String typeName;
	protected final int jdbcType;
	protected final boolean autoincrement;
	protected final Integer maximumPrecision;
	protected final String literalPrefix;
	protected final String literalSuffix;
	protected final boolean nullable;
	protected final boolean caseSensitive;
	protected final boolean searchable;
	protected final int minimumScale;
	protected final int maximumScale;
	protected final Boolean precisionRequired;
	protected final Boolean scaleRequired;

	public Type(String typeName, int jdbcType, Integer maximumPrecision, String literalPrefix, String literalSuffix, boolean nullable, boolean caseSensitive, boolean searchable, boolean autoincrement, int minimumScale, int maximumScale) {
		this.typeName = typeName;
		this.jdbcType = jdbcType;
		this.maximumPrecision = maximumPrecision;
		this.literalPrefix = literalPrefix;
		this.literalSuffix = literalSuffix;
		this.nullable = nullable;
		this.caseSensitive = caseSensitive;
		this.searchable = searchable;
		this.autoincrement = autoincrement;
		this.minimumScale = minimumScale;
		this.maximumScale = maximumScale;

		this.precisionRequired = null;
		this.scaleRequired = null;

	}

	public Type(String typeName, int jdbcType, Integer maximumPrecision, String literalPrefix, String literalSuffix, boolean nullable, boolean caseSensitive, boolean searchable, boolean autoincrement, int minimumScale, int maximumScale, boolean precisionRequired, boolean scaleRequired) {
		this.typeName = typeName;
		this.jdbcType = jdbcType;
		this.maximumPrecision = maximumPrecision;
		this.literalPrefix = literalPrefix;
		this.literalSuffix = literalSuffix;
		this.nullable = nullable;
		this.caseSensitive = caseSensitive;
		this.searchable = searchable;
		this.autoincrement = autoincrement;
		this.minimumScale = minimumScale;
		this.maximumScale = maximumScale;

		this.precisionRequired = precisionRequired;
		this.scaleRequired = scaleRequired;

	}

	public String getTypeName() {
		return typeName;
	}

	public int getJdbcType() {
		return jdbcType;
	}

	public Class getDefaultJavaType() {
		return getDefaultJavaType(jdbcType, maximumPrecision, maximumScale);
	}

	public static @Nullable Class getDefaultJavaType(int jdbcType, Integer precision, Integer scale) {
		switch (jdbcType) {
		case Types.BIGINT:
			return Long.class;
		case Types.BIT:
			return Boolean.class;
		case Types.BOOLEAN:
			return Boolean.class;
		case Types.CHAR:
		case Types.VARCHAR:
		case -15: // Types.NCHAR is on JDBC4/Java6 - we try to stay compatible
					// with JDBC3/Java5
		case -9: // Types.NVARCHAR (same)
			return String.class;
		case Types.DATE:
			return java.sql.Date.class;
		case Types.TIME:
			return Time.class;
		case Types.TIMESTAMP:
			return Timestamp.class;
		case Types.DECIMAL:
		case Types.NUMERIC:
			if (scale != null && scale > 0) {
				return BigDecimal.class;
			} else {
				return getDefaultIntegerType(precision);
			}
		case Types.DOUBLE:
			return Double.class;
		case Types.REAL:
			return Double.class;
		case Types.FLOAT:
			return Float.class;
		case Types.INTEGER:
			return getDefaultIntegerType(precision);
		case Types.SMALLINT:
			return Short.class;
		case Types.TINYINT:
			return Byte.class;
		case Types.BINARY:
			return byte[].class;
		case Types.BLOB:
			return java.sql.Blob.class;
		case Types.CLOB:
			return String.class;
		case Types.LONGVARBINARY:
			return byte[].class;
		case Types.LONGVARCHAR:
			return String.class;
		case Types.VARBINARY:
			return byte[].class;
		case Types.ARRAY:
			return java.sql.Array.class;
		case Types.DATALINK:
			return java.net.URL.class;
		case Types.DISTINCT:
		case Types.JAVA_OBJECT:
			return Object.class;
		case Types.NULL:
		case Types.OTHER:
		case Types.REF:
			return java.sql.Ref.class;
		case Types.STRUCT:
			return java.sql.Struct.class;
		default:
			logger.warn("Unsupported jdbc type: {}", jdbcType);
			return null;
		}
	}

	public static Class<? extends Number> getDefaultIntegerType(Integer precision) {
		if (precision == null) {
			return BigInteger.class;
		}
		if (precision < Math.log10(Integer.MAX_VALUE)) {
			return Integer.class;
		} else if (precision < Math.log10(Long.MAX_VALUE)) {
			return Long.class;
		} else {
			if (precision == 131089) {
				return BigDecimal.class; // Postgres bug - #925
			} else {
				return BigInteger.class;
			}
		}
	}

	public boolean isAutoincrement() {
		return autoincrement;
	}

	public Integer getMaximumPrecision() {
		return maximumPrecision;
	}

	public String getLiteralPrefix() {
		return literalPrefix;
	}

	public String getLiteralSuffix() {
		return literalSuffix;
	}

	public boolean isNullable() {
		return nullable;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public Integer getMinimumScale() {
		return minimumScale;
	}

	public Integer getMaximumScale() {
		return maximumScale;
	}

	@Override
	public String toString() {
		return "Type{" + "typeName='" + typeName + '\'' + ", jdbcType=" + jdbcType + ", autoincrement=" + autoincrement + ", maximumPrecision=" + maximumPrecision + ", literalPrefix='" + literalPrefix + '\'' + ", literalSuffix='" + literalSuffix + '\'' + ", nullable=" + nullable + ", caseSensitive=" + caseSensitive + ", searchable=" + searchable + ", minimumScale=" + minimumScale + ", maximumScale=" + maximumScale + '}';
	}

	public boolean isPrecisionRequired() {
		if (precisionRequired != null) {
			return precisionRequired;
		}
		switch (jdbcType) {
		case Types.BIGINT:
			return false;
		case Types.BIT:
			return false;
		case Types.BOOLEAN:
			return false;
		case Types.CHAR:
			return true;
		case Types.VARCHAR:
			return true;
		case Types.DATE:
			return false;
		case Types.TIME:
			return false;
		case Types.TIMESTAMP:
			return false;
		case Types.DECIMAL:
			return true;
		case Types.NUMERIC:
			return true;
		case Types.DOUBLE:
			return false;
		case Types.REAL:
			return false;
		case Types.FLOAT:
			return false;
		case Types.INTEGER:
			return false;
		case Types.SMALLINT:
			return false;
		case Types.TINYINT:
			return false;
		case Types.BINARY:
			return false;
		case Types.BLOB:
			return false;
		case Types.CLOB:
			return false;
		case Types.LONGVARBINARY:
			return false;
		case Types.LONGVARCHAR:
			return false;
		case Types.VARBINARY:
			return false;
		case Types.ARRAY:
			return false;
		case Types.DATALINK:
			return false;
		case Types.DISTINCT:
		case Types.JAVA_OBJECT:
			return false;
		case Types.NULL:
		case Types.OTHER:
		case Types.REF:
			return false;
		case Types.STRUCT:
			return false;
		default:
			return false;
		}

	}

	public boolean isScaleRequired() {
		if (scaleRequired != null) {
			return scaleRequired;
		}
		switch (jdbcType) {
		case Types.BIGINT:
			return false;
		case Types.BIT:
			return false;
		case Types.BOOLEAN:
			return false;
		case Types.CHAR:
			return false;
		case Types.VARCHAR:
			return false;
		case Types.DATE:
			return false;
		case Types.TIME:
			return false;
		case Types.TIMESTAMP:
			return false;
		case Types.DECIMAL:
			return true;
		case Types.NUMERIC:
			return true;
		case Types.DOUBLE:
			return false;
		case Types.REAL:
			return false;
		case Types.FLOAT:
			return false;
		case Types.INTEGER:
			return false;
		case Types.SMALLINT:
			return false;
		case Types.TINYINT:
			return false;
		case Types.BINARY:
			return false;
		case Types.BLOB:
			return false;
		case Types.CLOB:
			return false;
		case Types.LONGVARBINARY:
			return false;
		case Types.LONGVARCHAR:
			return false;
		case Types.VARBINARY:
			return false;
		case Types.ARRAY:
			return false;
		case Types.DATALINK:
			return false;
		case Types.DISTINCT:
		case Types.JAVA_OBJECT:
			return false;
		case Types.NULL:
		case Types.OTHER:
		case Types.REF:
			return false;
		case Types.STRUCT:
			return false;
		default:
			return false;
		}
	}

	public boolean isNumeric() {
		switch (jdbcType) {
		case Types.NUMERIC:
		case Types.DECIMAL:
		case Types.INTEGER:
		case Types.FLOAT:
		case Types.DOUBLE:
		case Types.BIGINT:
		case Types.SMALLINT:
		case Types.TINYINT:
		case Types.REAL:
			return true;
		default:
			return false;
		}
	}

}