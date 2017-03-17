package com.callingtree.opensource.jdbi.extension.query;

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import java.util.Map;

public class DynamicQueryBuilder {

    public void build(Handle handle) {
        Query<Map<String, Object>> query = handle.createQuery("");
    }

}
