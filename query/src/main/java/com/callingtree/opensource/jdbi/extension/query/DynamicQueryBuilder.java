package com.callingtree.opensource.jdbi.extension.query;

import com.callingtree.opensource.jdbi.extension.annotation.Equals;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DynamicQueryBuilder {

    private static final String TOKEN_SEPERATOR = "_";

    private static final String ALIAS_SEPERATOR = ".";

    private static final String NEW_LINE = " \n";

    private final StringBuilder modifiedSelectSql = new StringBuilder();

    private final WhereConditions whereConditions;

    private final Map<String, Object> properties = new HashMap<>();

    public DynamicQueryBuilder(String selectSql, WhereConditions whereConditions) {
        modifiedSelectSql.append(selectSql).append(NEW_LINE);
        this.whereConditions = whereConditions;
    }

    public void build() throws IllegalAccessException {

        List<Field> clauses = Arrays.stream(whereConditions.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Equals.class)).collect(Collectors.toList());

        int varCount = 0;

        for (int i = 0; i < clauses.size(); i++) {

            if (i == 0) {
                modifiedSelectSql.append(" WHERE ");
            }
            else {
                modifiedSelectSql.append(" AND ");
            }

            varCount += appendEquals(whereConditions, clauses.get(i), modifiedSelectSql, properties, varCount);
        }
    }

    /**
     * Depending on the field is null or not, it does the followings:
     * <ul>
     *     <li>Append the where condition to the SQL</li>
     *     <li>Creates a token for binding the property if not null</li>
     * </ul>
     *
     * @param whereConditions   The where condition
     * @param field             The field in the WhereCondition that we are looking at
     * @param sqlBuilder        The string build for the modified SQL
     * @param properties        HashMap of all properties created
     * @param currVarCount      The current var count for adding to the token for binding value using JDBI
     *
     * @return The number of properties added (1 if there not null, 0 otherwise because there's nothing for JDBI to bind)
     * @throws IllegalAccessException    If there is issue to look up the field
     */
    int appendEquals(WhereConditions whereConditions, Field field,
                     StringBuilder sqlBuilder, Map<String, Object> properties,
                     int currVarCount) throws IllegalAccessException {

        Equals equals = field.getDeclaredAnnotation(Equals.class);
        StringBuilder jdbiPropertyName = new StringBuilder();
        StringBuilder jdbiColumnName = new StringBuilder();

        if (StringUtils.isNotEmpty(equals.alias())) {
            jdbiColumnName.append(equals.alias()).append(ALIAS_SEPERATOR);
            jdbiPropertyName.append(equals.alias()).append(TOKEN_SEPERATOR);
        }
        jdbiColumnName.append(equals.columnName());
        jdbiPropertyName.append(equals.columnName()).append(TOKEN_SEPERATOR).append(currVarCount);

        sqlBuilder.append(jdbiColumnName);

        Optional<Object> fieldValue = getFieldValue(field, whereConditions);
        if (fieldValue.isPresent()) {
            sqlBuilder.append(" = ").append(":").append(jdbiPropertyName.toString()).append(NEW_LINE);
            properties.put(jdbiPropertyName.toString(), fieldValue.get());
            return 1;
        }
        else {
            sqlBuilder.append(" is null").append(NEW_LINE);
            return 0;
        }
    }

    private Optional<Object> getFieldValue(Field field, Object parent) throws IllegalAccessException {
        // FIXME: is there a better way to do this?
        field.setAccessible(true);
        return Optional.ofNullable(field.get(parent));
    }

    String getModifiedSelectSql() {
        return modifiedSelectSql.toString();
    }

    Map<String, Object> getProperties() {
        return properties;
    }
}
