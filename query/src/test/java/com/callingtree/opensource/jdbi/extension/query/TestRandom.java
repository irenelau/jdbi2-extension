package com.callingtree.opensource.jdbi.extension.query;

import com.callingtree.opensource.jdbi.extension.query.testrule.DbiResource;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestRandom {

    private static final String CREATE_EMPLOYEE_TABLE_SQL = "" +
            "  CREATE TABLE EMPLOYEE ( \n" +
            "      Alias    varchar(32)     not null, \n" +
            "      Age      integer         not null \n" +
            "  ); \n";

    @Rule
    public DbiResource dbiResource = new DbiResource(CREATE_EMPLOYEE_TABLE_SQL);

    @Test
    public void test1() {
        assertNotNull(dbiResource.getDbi());
        assertEquals(1, 1);
    }
}
