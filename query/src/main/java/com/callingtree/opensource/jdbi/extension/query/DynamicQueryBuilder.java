package com.callingtree.opensource.jdbi.extension.query;

import com.callingtree.opensource.jdbi.extension.annotation.Equals;
import org.apache.commons.lang3.StringUtils;
import org.skife.jdbi.v2.Handle;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamicQueryBuilder {

    public static void build(Handle handle, String selectSql, WhereConditions whereConditions) throws IllegalAccessException {

        List<Field> clauses = Arrays.stream(whereConditions.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Equals.class)).collect(Collectors.toList());

        Map<String, Object> properties = new HashMap<>();

        int varCount = 0;
        StringBuilder sqlBuilder = new StringBuilder(selectSql);

        for (int i = 0; i < clauses.size(); i++) {

            sqlBuilder = i == 0 ? sqlBuilder.append("  WHERE ") : sqlBuilder.append("   AND ");

            Field field = clauses.get(i);

            if (field.getDeclaredAnnotation(Equals.class) != null) {
                varCount += appendEquals(whereConditions, field, sqlBuilder, properties, varCount);
            }
        }
    }

    private static int appendEquals(WhereConditions whereConditions, Field field,
                                    StringBuilder sqlBuilder, Map<String, Object> properties, int varCount) throws IllegalAccessException {

        Equals equals = field.getDeclaredAnnotation(Equals.class);

        StringBuilder property = new StringBuilder();
        if (StringUtils.isNotEmpty(equals.alias())) {
            sqlBuilder.append(equals.alias()).append(".");
            property.append(equals.alias()).append("_");
        }

        property.append(equals.columnName()).append("_").append(varCount);
        sqlBuilder.append(equals.columnName()).append(" = ").append(":").append(property.toString()).append(" \n");

        // FIXME: is there a better way to do this?
        field.setAccessible(true);
        Object fieldValue = field.get(whereConditions);

        properties.put(property.toString(), fieldValue);

        return 1;
    }

}
