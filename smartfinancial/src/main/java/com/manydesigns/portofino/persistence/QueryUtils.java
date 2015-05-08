package com.manydesigns.portofino.persistence;

import com.manydesigns.elements.fields.search.Criterion;
import com.manydesigns.elements.fields.search.TextMatchMode;
import com.manydesigns.elements.reflection.ClassAccessor;
import com.manydesigns.elements.reflection.PropertyAccessor;
import com.manydesigns.elements.text.OgnlSqlFormat;
import com.manydesigns.elements.text.QueryStringWithParameters;
import com.manydesigns.portofino.database.TableCriteria;
import com.manydesigns.portofino.model.Model;
import com.manydesigns.portofino.model.database.*;
import com.manydesigns.portofino.reflection.TableAccessor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryUtils {
	protected static final String WHERE_STRING = " WHERE ";
	protected static final Pattern FROM_PATTERN = Pattern.compile("(SELECT\\s+.*\\s+)?FROM\\s+([a-z_$\\u0080-\\ufffe]{1}[a-z_$0-9\\u0080-\\ufffe]*).*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	protected static final Logger logger = LoggerFactory.getLogger(QueryUtils.class);

	public static List<Object[]> runSql(Session session, String sql) {
		OgnlSqlFormat sqlFormat = OgnlSqlFormat.create(sql);
		String formatString = sqlFormat.getFormatString();
		Object[] parameters = sqlFormat.evaluateOgnlExpressions(null);
		return runSql(session, formatString, parameters);
	}

	public static List<Object[]> runSql(Session session, final String queryString, final Object[] parameters) {
		final List<Object[]> result = new ArrayList<Object[]>();

		try {
			session.doWork(new Work() {
				public void execute(Connection connection) throws SQLException {
					PreparedStatement stmt = connection.prepareStatement(queryString);
					try {
						for (int i = 0; i < parameters.length; i++) {
							stmt.setObject(i + 1, parameters[i]);
						}
						ResultSet rs = stmt.executeQuery();
						ResultSetMetaData md = rs.getMetaData();
						int cc = md.getColumnCount();
						while (rs.next()) {
							Object[] current = new Object[cc];
							for (int i = 0; i < cc; i++) {
								current[i] = rs.getObject(i + 1);
							}
							result.add(current);
						}
					} finally {
						stmt.close(); // Chiude anche il result set
					}
				}
			});
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			session.beginTransaction();
			throw e;
		}

		return result;
	}

	public static List<Object> getObjects(Session session, TableCriteria criteria, @Nullable Integer firstResult, @Nullable Integer maxResults) {
		QueryStringWithParameters queryStringWithParameters = getQueryStringWithParametersForCriteria(criteria);

		return runHqlQuery(session, queryStringWithParameters.getQueryString(), queryStringWithParameters.getParameters(), firstResult, maxResults);
	}

	public static List<Object> getObjects(Session session, String queryString, Object rootObject, @Nullable Integer firstResult, @Nullable Integer maxResults) {
		OgnlSqlFormat sqlFormat = OgnlSqlFormat.create(queryString);
		String formatString = sqlFormat.getFormatString();
		Object[] parameters = sqlFormat.evaluateOgnlExpressions(rootObject);

		return runHqlQuery(session, formatString, parameters, firstResult, maxResults);
	}

	public static List<Object> getObjects(Session session, String queryString, @Nullable Integer firstResult, @Nullable Integer maxResults) {
		return getObjects(session, queryString, (TableCriteria) null, null, firstResult, maxResults);
	}

	public static QueryStringWithParameters getQueryStringWithParametersForCriteria(TableCriteria criteria) {
		return getQueryStringWithParametersForCriteria(criteria, null);
	}

	public static QueryStringWithParameters getQueryStringWithParametersForCriteria(@Nullable TableCriteria criteria, @Nullable String alias) {
		if (criteria == null) {
			return new QueryStringWithParameters("", new Object[0]);
		}
		Table table = criteria.getTable();

		ArrayList<Object> parametersList = new ArrayList<Object>();
		StringBuilder whereBuilder = new StringBuilder();
		for (Criterion criterion : criteria) {
			PropertyAccessor accessor = criterion.getPropertyAccessor();
			String hqlFormat;
			if (criterion instanceof TableCriteria.EqCriterion) {
				TableCriteria.EqCriterion eqCriterion = (TableCriteria.EqCriterion) criterion;
				Object value = eqCriterion.getValue();
				hqlFormat = "{0} = ?";
				parametersList.add(value);
			} else if (criterion instanceof TableCriteria.InCriterion) {
				TableCriteria.InCriterion inCriterion = (TableCriteria.InCriterion) criterion;
				Object[] values = inCriterion.getValues();
				StringBuilder params = new StringBuilder();
				if (values != null) {
					boolean first = true;
					for (Object value : values) {
						if (!first) {
							params.append(", ?");
						} else {
							params.append("?");
							first = false;
						}
						parametersList.add(value);
					}
					hqlFormat = "{0} in (" + params.toString() + ")";
				} else {
					hqlFormat = null;
				}
			} else if (criterion instanceof TableCriteria.NeCriterion) {
				TableCriteria.NeCriterion neCriterion = (TableCriteria.NeCriterion) criterion;
				Object value = neCriterion.getValue();
				hqlFormat = "{0} <> ?";
				parametersList.add(value);
			} else if (criterion instanceof TableCriteria.BetweenCriterion) {
				TableCriteria.BetweenCriterion betweenCriterion = (TableCriteria.BetweenCriterion) criterion;
				Object min = betweenCriterion.getMin();
				Object max = betweenCriterion.getMax();
				hqlFormat = "{0} >= ? AND {0} <= ?";
				parametersList.add(min);
				parametersList.add(max);
			} else if (criterion instanceof TableCriteria.GtCriterion) {
				TableCriteria.GtCriterion gtCriterion = (TableCriteria.GtCriterion) criterion;
				Object value = gtCriterion.getValue();
				hqlFormat = "{0} > ?";
				parametersList.add(value);
			} else if (criterion instanceof TableCriteria.GeCriterion) {
				TableCriteria.GeCriterion gtCriterion = (TableCriteria.GeCriterion) criterion;
				Object value = gtCriterion.getValue();
				hqlFormat = "{0} >= ?";
				parametersList.add(value);
			} else if (criterion instanceof TableCriteria.LtCriterion) {
				TableCriteria.LtCriterion ltCriterion = (TableCriteria.LtCriterion) criterion;
				Object value = ltCriterion.getValue();
				hqlFormat = "{0} < ?";
				parametersList.add(value);
			} else if (criterion instanceof TableCriteria.LeCriterion) {
				TableCriteria.LeCriterion leCriterion = (TableCriteria.LeCriterion) criterion;
				Object value = leCriterion.getValue();
				hqlFormat = "{0} <= ?";
				parametersList.add(value);
			} else if (criterion instanceof TableCriteria.LikeCriterion) {
				TableCriteria.LikeCriterion likeCriterion = (TableCriteria.LikeCriterion) criterion;
				String value = (String) likeCriterion.getValue();
				String pattern = processTextMatchMode(likeCriterion.getTextMatchMode(), value);
				hqlFormat = "{0} like ?";
				parametersList.add(pattern);
			} else if (criterion instanceof TableCriteria.IlikeCriterion) {
				TableCriteria.IlikeCriterion ilikeCriterion = (TableCriteria.IlikeCriterion) criterion;
				String value = (String) ilikeCriterion.getValue();
				String pattern = processTextMatchMode(ilikeCriterion.getTextMatchMode(), value);
				hqlFormat = "lower({0}) like lower(?)";
				parametersList.add(pattern);
			} else if (criterion instanceof TableCriteria.IsNullCriterion) {
				hqlFormat = "{0} is null";
			} else if (criterion instanceof TableCriteria.IsNotNullCriterion) {
				hqlFormat = "{0} is not null";
			} else {
				logger.error("Unrecognized criterion: {}", criterion);
				throw new InternalError("Unrecognied criterion");
			}

			if (hqlFormat == null) {
				continue;
			}

			String accessorName = accessor.getName();
			if (alias != null) {
				accessorName = alias + "." + accessorName;
			}
			String hql = MessageFormat.format(hqlFormat, accessorName);

			if (whereBuilder.length() > 0) {
				whereBuilder.append(" AND ");
			}
			whereBuilder.append(hql);
		}
		String whereClause = whereBuilder.toString();
		String queryString;
		String actualEntityName = table.getActualEntityName();
		if (alias != null) {
			actualEntityName += " " + alias;
		}
		if (whereClause.length() > 0) {
			queryString = MessageFormat.format("FROM {0}" + WHERE_STRING + "{1}", actualEntityName, whereClause);
		} else {
			queryString = MessageFormat.format("FROM {0}", actualEntityName);
		}

		Object[] parameters = new Object[parametersList.size()];
		parametersList.toArray(parameters);

		return new QueryStringWithParameters(queryString, parameters);
	}

	protected static String processTextMatchMode(TextMatchMode textMatchMode, String value) {
		String pattern;
		switch (textMatchMode) {
		case EQUALS:
			pattern = value;
			break;
		case CONTAINS:
			pattern = "%" + value + "%";
			break;
		case STARTS_WITH:
			pattern = value + "%";
			break;
		case ENDS_WITH:
			pattern = "%" + value;
			break;
		default:
			String msg = MessageFormat.format("Unrecognized text match mode: {0}", textMatchMode);
			logger.error(msg);
			throw new InternalError(msg);
		}
		return pattern;
	}

	public static Table getTableFromQueryString(Database database, String queryString) {
		Matcher matcher = FROM_PATTERN.matcher(queryString);
		String entityName;
		if (matcher.matches()) {
			entityName = matcher.group(2);
		} else {
			return null;
		}

		Table table = DatabaseLogic.findTableByEntityName(database, entityName);
		return table;
	}

	public static List<Object> getObjects(Session session, String queryString, TableCriteria criteria, @Nullable Object rootObject, @Nullable Integer firstResult, @Nullable Integer maxResults) {
		QueryStringWithParameters result = mergeQuery(queryString, criteria, rootObject);

		return runHqlQuery(session, result.getQueryString(), result.getParameters(), firstResult, maxResults);
	}

	public static QueryStringWithParameters mergeQuery(String queryString, @Nullable TableCriteria criteria, Object rootObject) {
		OgnlSqlFormat sqlFormat = OgnlSqlFormat.create(queryString);
		String formatString = sqlFormat.getFormatString();
		Object[] parameters = sqlFormat.evaluateOgnlExpressions(rootObject);

		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		PlainSelect parsedQueryString;
		PlainSelect parsedCriteriaQuery;
		try {
			parsedQueryString = parseQuery(parserManager, formatString);
		} catch (JSQLParserException e) {
			throw new RuntimeException("Couldn't merge query", e);
		}

		Alias mainEntityAlias = null;
		if (criteria != null) {
			mainEntityAlias = getEntityAlias(criteria.getTable().getActualEntityName(), parsedQueryString);
		}

		QueryStringWithParameters criteriaQuery = getQueryStringWithParametersForCriteria(criteria, mainEntityAlias != null ? mainEntityAlias.getName() : null);
		String criteriaQueryString = criteriaQuery.getQueryString();
		Object[] criteriaParameters = criteriaQuery.getParameters();

		try {
			if (StringUtils.isEmpty(criteriaQueryString)) {
				parsedCriteriaQuery = new PlainSelect();
			} else {
				parsedCriteriaQuery = parseQuery(parserManager, criteriaQueryString);
			}
		} catch (JSQLParserException e) {
			throw new RuntimeException("Couldn't merge query", e);
		}

		Expression whereExpression;
		if (parsedQueryString.getWhere() != null) {
			if (parsedCriteriaQuery.getWhere() != null) {
				whereExpression = parsedQueryString.getWhere();
				if (!(whereExpression instanceof Parenthesis)) {
					whereExpression = new Parenthesis(whereExpression);
				}
				whereExpression = new AndExpression(whereExpression, parsedCriteriaQuery.getWhere());
			} else {
				whereExpression = parsedQueryString.getWhere();
			}
		} else {
			whereExpression = parsedCriteriaQuery.getWhere();
		}
		parsedQueryString.setWhere(whereExpression);
		if (criteria != null && criteria.getOrderBy() != null) {
			List orderByElements = new ArrayList();
			OrderByElement orderByElement = new OrderByElement();
			orderByElement.setAsc(criteria.getOrderBy().isAsc());
			String propertyName = criteria.getOrderBy().getPropertyAccessor().getName();
			if (mainEntityAlias != null) {
				propertyName = mainEntityAlias + "." + propertyName;
			}
			orderByElement.setExpression(new net.sf.jsqlparser.schema.Column(new net.sf.jsqlparser.schema.Table(), propertyName));
			orderByElements.add(orderByElement);
			if (parsedQueryString.getOrderByElements() != null) {
				for (Object el : parsedQueryString.getOrderByElements()) {
					OrderByElement toAdd = (OrderByElement) el;
					if (toAdd.getExpression() instanceof net.sf.jsqlparser.schema.Column) {
						net.sf.jsqlparser.schema.Column column = (net.sf.jsqlparser.schema.Column) toAdd.getExpression();
						if (StringUtils.isEmpty(column.getTable().getName()) && propertyName.equals(column.getColumnName())) {
							continue; // do not add
						}
					}
					orderByElements.add(toAdd);
				}
			}
			parsedQueryString.setOrderByElements(orderByElements);
		}
		String fullQueryString = parsedQueryString.toString();
		if (fullQueryString.toLowerCase().startsWith(FAKE_SELECT_PREFIX)) {
			fullQueryString = fullQueryString.substring(FAKE_SELECT_PREFIX.length());
		}

		// merge the parameters
		ArrayList<Object> mergedParametersList = new ArrayList<Object>();
		mergedParametersList.addAll(Arrays.asList(parameters));
		mergedParametersList.addAll(Arrays.asList(criteriaParameters));
		Object[] mergedParameters = new Object[mergedParametersList.size()];
		mergedParametersList.toArray(mergedParameters);

		return new QueryStringWithParameters(fullQueryString, mergedParameters);
	}

	public static final String FAKE_SELECT_PREFIX = "select __portofino_fake_select__ ";

	public static PlainSelect parseQuery(CCJSqlParserManager parserManager, String query) throws JSQLParserException {
		PlainSelect parsedQueryString;
		if (!query.toLowerCase().trim().startsWith("select")) {
			query = FAKE_SELECT_PREFIX + query;
		}
		parsedQueryString = (PlainSelect) ((Select) parserManager.parse(new StringReader(query))).getSelectBody();
		return parsedQueryString;
	}

	public static List<Object> runHqlQuery(Session session, String queryString, @Nullable Object[] parameters) {
		return runHqlQuery(session, queryString, parameters, null, null);
	}

	public static List<Object> runHqlQuery(Session session, String queryString, @Nullable Object[] parameters, @Nullable Integer firstResult, @Nullable Integer maxResults) {

		Query query = session.createQuery(queryString);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i, parameters[i]);
			}
		}

		if (firstResult != null) {
			query.setFirstResult(firstResult);
		}

		if (maxResults != null) {
			query.setMaxResults(maxResults);
		}

		// noinspection unchecked
		try {
			List<Object> result = query.list();
			return result;
		} catch (HibernateException e) {
			logger.error("Error running query", e);
			session.getTransaction().rollback();
			session.beginTransaction();
			throw e;
		}
	}

	public static Object getObjectByPk(Persistence persistence, String database, String entityName, Serializable pk) {
		Session session = persistence.getSession(database);
		TableAccessor table = persistence.getTableAccessor(database, entityName);
		return getObjectByPk(session, table, pk);
	}

	public static Object getObjectByPk(Session session, TableAccessor table, Serializable pk) {
		String actualEntityName = table.getTable().getActualEntityName();
		Object result;
		PropertyAccessor[] keyProperties = table.getKeyProperties();
		int size = keyProperties.length;
		if (size > 1) {
			result = session.get(actualEntityName, pk);
			return result;
		}
		PropertyAccessor propertyAccessor = keyProperties[0];
		Serializable key = (Serializable) propertyAccessor.get(pk);
		result = session.get(actualEntityName, key);
		return result;
	}

	public static Object getObjectByPk(Persistence persistence, Table baseTable, Serializable pkObject) {
		return getObjectByPk(persistence, baseTable.getDatabaseName(), baseTable.getActualEntityName(), pkObject);
	}

	public static Object getObjectByPk(Persistence persistence, Table baseTable, Serializable pkObject, String query, Object rootObject) {
		return getObjectByPk(persistence, baseTable.getDatabaseName(), baseTable.getActualEntityName(), pkObject, query, rootObject);
	}

	public static Object getObjectByPk(Persistence persistence, String database, String entityName, Serializable pk, String hqlQueryString, Object rootObject) {
		TableAccessor table = persistence.getTableAccessor(database, entityName);
		List<Object> result;
		PropertyAccessor[] keyProperties = table.getKeyProperties();
		OgnlSqlFormat sqlFormat = OgnlSqlFormat.create(hqlQueryString);
		String formatString = sqlFormat.getFormatString();
		Object[] ognlParameters = sqlFormat.evaluateOgnlExpressions(rootObject);
		int i = keyProperties.length;
		int p = ognlParameters.length;
		Object[] parameters = new Object[p + i];
		System.arraycopy(ognlParameters, 0, parameters, i, p);
		try {
			PlainSelect parsedQuery = parseQuery(new CCJSqlParserManager(), formatString);
			if (parsedQuery.getWhere() == null) {
				return getObjectByPk(persistence, database, entityName, pk);
			}

			Alias mainEntityAlias = getEntityAlias(entityName, parsedQuery);
			net.sf.jsqlparser.schema.Table mainEntityTable;
			if (mainEntityAlias != null) {
				mainEntityTable = new net.sf.jsqlparser.schema.Table(null, mainEntityAlias.getName());
			} else {
				mainEntityTable = new net.sf.jsqlparser.schema.Table();
			}

			for (PropertyAccessor propertyAccessor : keyProperties) {
				i--;
				EqualsTo condition = new EqualsTo();
				parsedQuery.setWhere(new AndExpression(condition, new Parenthesis(parsedQuery.getWhere())));
				net.sf.jsqlparser.schema.Column column = new net.sf.jsqlparser.schema.Column(mainEntityTable, propertyAccessor.getName());
				condition.setLeftExpression(column);
				condition.setRightExpression(new JdbcParameter());
				parameters[i] = propertyAccessor.get(pk);
			}

			String fullQueryString = parsedQuery.toString();
			if (fullQueryString.toLowerCase().startsWith(FAKE_SELECT_PREFIX)) {
				fullQueryString = fullQueryString.substring(FAKE_SELECT_PREFIX.length());
			}
			Session session = persistence.getSession(database);
			result = runHqlQuery(session, fullQueryString, parameters);
			if (result != null && !result.isEmpty()) {
				return result.get(0);
			} else {
				return null;
			}
		} catch (JSQLParserException e) {
			throw new Error(e);
		}
	}

	protected static Alias getEntityAlias(String entityName, PlainSelect query) {
		FromItem fromItem = query.getFromItem();
		if (hasEntityAlias(entityName, fromItem)) {
			return fromItem.getAlias();
		}
		if (query.getJoins() != null) {
			for (Object o : query.getJoins()) {
				Join join = (Join) o;
				if (hasEntityAlias(entityName, join.getRightItem())) {
					return join.getRightItem().getAlias();
				}
			}
		}
		logger.debug("Alias from entity " + entityName + " not found in query " + query);
		return null;
	}

	private static boolean hasEntityAlias(String entityName, FromItem fromItem) {
		return fromItem instanceof net.sf.jsqlparser.schema.Table && ((net.sf.jsqlparser.schema.Table) fromItem).getName().equals(entityName) && fromItem.getAlias() != null && !StringUtils.isBlank(fromItem.getAlias().getName());
	}

	public static void commit(Persistence persistence, String databaseName) {
		Session session = persistence.getSession(databaseName);
		try {
			session.getTransaction().commit();
		} catch (HibernateException e) {
			persistence.closeSession(databaseName);
			throw e;
		}
	}

	@SuppressWarnings({ "unchecked" })
	public static List<Object> getRelatedObjects(Persistence persistence, String databaseName, String entityName, Object obj, String oneToManyRelationshipName) {
		Model model = persistence.getModel();
		ForeignKey relationship = DatabaseLogic.findOneToManyRelationship(model, databaseName, entityName, oneToManyRelationshipName);
		if (relationship == null) {
			throw new IllegalArgumentException("Relationship not defined: " + oneToManyRelationshipName);
		}
		Table fromTable = relationship.getFromTable();
		Session session = persistence.getSession(fromTable.getDatabaseName());

		ClassAccessor toAccessor = persistence.getTableAccessor(databaseName, entityName);

		try {
			org.hibernate.Criteria criteria = session.createCriteria(fromTable.getActualEntityName());
			for (Reference reference : relationship.getReferences()) {
				Column fromColumn = reference.getActualFromColumn();
				Column toColumn = reference.getActualToColumn();
				PropertyAccessor toPropertyAccessor = toAccessor.getProperty(toColumn.getActualPropertyName());
				Object toValue = toPropertyAccessor.get(obj);
				criteria.add(Restrictions.eq(fromColumn.getActualPropertyName(), toValue));
			}
			// noinspection unchecked
			List<Object> result = criteria.list();
			return result;
		} catch (Throwable e) {
			String msg = String.format("Cannot access relationship %s on entity %s.%s", oneToManyRelationshipName, databaseName, entityName);
			logger.warn(msg, e);
		}
		return null;
	}
}