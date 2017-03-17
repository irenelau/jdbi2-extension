package com.callingtree.opensource.jdbi.extension.query;

import com.callingtree.opensource.jdbi.extension.annotation.Where;
import org.skife.jdbi.v2.Handle;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicQueryBuilder {

    public static void build(Handle handle, String selectSql, WhereConditions whereConditions) {

        List<Field> whereFilters = Arrays.stream(whereConditions.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Where.class)).collect(Collectors.toList());

        // todo - next!
    }

}
