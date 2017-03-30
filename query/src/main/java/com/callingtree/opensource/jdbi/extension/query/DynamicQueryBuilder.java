package com.callingtree.opensource.jdbi.extension.query;

import com.callingtree.opensource.jdbi.extension.annotation.Equals;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            Field field = clauses.get(i);
            if (field.getDeclaredAnnotation(Equals.class) != null) {
                varCount += appendEquals(whereConditions, field, modifiedSelectSql, properties, varCount);
            }
        }
    }

    private int appendEquals(WhereConditions whereConditions, Field field,
                                    StringBuilder sqlBuilder, Map<String, Object> properties, int varCount) throws IllegalAccessException {

        Equals equals = field.getDeclaredAnnotation(Equals.class);

        StringBuilder property = new StringBuilder();
        if (StringUtils.isNotEmpty(equals.alias())) {
            sqlBuilder.append(equals.alias()).append(ALIAS_SEPERATOR);
            property.append(equals.alias()).append(TOKEN_SEPERATOR);
        }

        property.append(equals.columnName()).append(TOKEN_SEPERATOR).append(varCount);
        sqlBuilder.append(equals.columnName()).append(" = ").append(":").append(property.toString()).append(NEW_LINE);

        properties.put(property.toString(), getFieldValue(field, whereConditions));

        return 1;
    }

    private Object getFieldValue(Field field, Object parent) throws IllegalAccessException {
        // FIXME: is there a better way to do this?
        field.setAccessible(true);
        return field.get(parent);
    }

    StringBuilder getModifiedSelectSql() {
        return modifiedSelectSql;
    }

    Map<String, Object> getProperties() {
        return properties;
    }
}
