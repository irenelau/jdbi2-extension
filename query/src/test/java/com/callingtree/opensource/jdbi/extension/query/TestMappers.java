package com.callingtree.opensource.jdbi.extension.query;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestMappers {

    public static class PersonMapper implements ResultSetMapper<TestEntities.Person> {

        public TestEntities.Person map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
            return new TestEntities.Person()
                    .setId(getNInt(resultSet, "Id"));
        }
    }

    public static class AliasMapper implements ResultSetMapper<TestEntities.Alias> {

        public TestEntities.Alias map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
            return new TestEntities.Alias()
                    .setId(getNInt(resultSet, "Id"))
                    .setAlias(resultSet.getString("Alias"));
        }
    }

    public static class PhoneMapper implements ResultSetMapper<TestEntities.Phone> {

        public TestEntities.Phone map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {

            return new TestEntities.Phone()
                    .setId(getNInt(resultSet, "Id"))
                    .setType(TestEnums.PhoneType.toPhoneType(resultSet.getString("Type")))
                    .setNumber(getNLong(resultSet, "Number"))
                    .setIsActive(getNBoolean(resultSet, "IsActive"));
        }
    }

    private static Integer getNInt(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName) == null ? null : rs.getInt(columnName);
    }

    private static Boolean getNBoolean(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName) == null ? null : rs.getBoolean(columnName);
    }

    private static Long getNLong(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName) == null ? null : rs.getLong(columnName);
    }
}
