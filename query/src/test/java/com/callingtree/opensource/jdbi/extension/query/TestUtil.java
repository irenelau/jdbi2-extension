package com.callingtree.opensource.jdbi.extension.query;

class TestUtil {

    static final String CREATE_PERSON_TABLE_SQL = "" +
            "  CREATE TABLE PERSON ( \n" +
            "      Id         INTEGER         not null \n" +
            "  ); \n";

    static final String CREATE_ALIAS_TABLE_SQL = "" +
            "  CREATE TABLE ALIAS ( \n" +
            "      Id         INTEGER         not null, \n" +
            "      Alias      VARCHAR(16)     not null \n" +
            "  ); \n";

    static final String CREATE_PHONE_TABLE_SQL = "" +
            "  CREATE TABLE PHONE ( \n" +
            "      Id         INTEGER         not null, \n" +
            "      Type       VARCHAR(16)     not null, \n" +
            "      Number     BIGINT          not null, \n" +
            "      IsActive   BIT             not null \n" +
            "  ); \n";
}
