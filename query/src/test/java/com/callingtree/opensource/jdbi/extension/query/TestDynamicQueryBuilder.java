package com.callingtree.opensource.jdbi.extension.query;

import com.callingtree.opensource.jdbi.extension.annotation.Equals;
import com.callingtree.opensource.jdbi.extension.query.rule.DbiResource;
import org.junit.Rule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestDynamicQueryBuilder {

    @Rule
    public DbiResource dbiResource = new DbiResource(TestUtil.CREATE_PERSON_TABLE_SQL,
            TestUtil.CREATE_ALIAS_TABLE_SQL,
            TestUtil.CREATE_PHONE_TABLE_SQL);

    @SuppressWarnings("unused")
    @Test
    public void testAppendEquals_equals_notNull_checkCount() throws NoSuchFieldException, IllegalAccessException {

        final String tableAlias = "a";
        final String tableColumnName = "Nickname";
        final String nicknameValue = "foo";

        WhereConditions inputWhereConds = new WhereConditions() {

            @Equals(alias = tableAlias, columnName = tableColumnName)
            private String nickname = nicknameValue;

            private String notUsedStr;

            private Integer notUsedInt;

        };

        DynamicQueryBuilder builder = new DynamicQueryBuilder("", inputWhereConds);

        Field field = inputWhereConds.getClass().getDeclaredField("nickname");
        StringBuilder sqlSb = new StringBuilder();
        Map<String, Object> properties = new HashMap<>();

        int addedPropCount = builder.appendEquals(inputWhereConds, field, sqlSb, properties, 1);
        assertEquals("Only one property added", 1, addedPropCount);
    }

    @SuppressWarnings("unused")
    @Test
    public void testAppendEquals_equals_null_checkCount() throws NoSuchFieldException, IllegalAccessException {

        final String tableAlias = "a";
        final String tableColumnName = "Nickname";
        final String nicknameValue = null;

        WhereConditions inputWhereConds = new WhereConditions() {

            @Equals(alias = tableAlias, columnName = tableColumnName)
            private String nickname = nicknameValue;

            private String notUsedStr;

            private Integer notUsedInt;

        };

        DynamicQueryBuilder builder = new DynamicQueryBuilder("", inputWhereConds);

        Field field = inputWhereConds.getClass().getDeclaredField("nickname");
        StringBuilder sqlSb = new StringBuilder();
        Map<String, Object> properties = new HashMap<>();

        int addedPropCount = builder.appendEquals(inputWhereConds, field, sqlSb, properties, 1);
        assertEquals("No property added", 0, addedPropCount);
    }

    @SuppressWarnings("unused")
    @Test
    public void testBuilder_build_notNull_equals() throws IllegalAccessException {
        DBI dbi = dbiResource.getDbi();
        TestDataDao dao = dbi.onDemand(TestDataDao.class);

        int id1 = 1001;
        dao.insertPerson(new TestEntities.Person().setId(id1));
        dao.insertPhone(new TestEntities.Phone().setId(id1).setType(TestEnums.PhoneType.Home).setNumber(1001001L).setIsActive(true));

        int id2 = 2002;
        dao.insertPerson(new TestEntities.Person().setId(id2));
        dao.insertPhone(new TestEntities.Phone().setId(id2).setType(TestEnums.PhoneType.Home).setNumber(1001001L).setIsActive(false));
        dao.insertPhone(new TestEntities.Phone().setId(id2).setType(TestEnums.PhoneType.Home).setNumber(2002001L).setIsActive(true));

        // Person 1 and person 2 has the same number but person2's one is inactive now

        final String tableAlias = "a";
        final String tableColumnName = "Nickname";
        final String nicknameValue = "foo";
        WhereConditions inputWhereConds = new WhereConditions() {

            @Equals(alias = tableAlias, columnName = tableColumnName)
            private String nickname = nicknameValue;

            private String notUsedStr;

            private Integer notUsedInt;

        };

        DynamicQueryBuilder builder = new DynamicQueryBuilder("", inputWhereConds);
        builder.build();

        final String expectedAlias = tableAlias + "." + tableColumnName;
        final String expectedProperty = tableAlias + "_" + tableColumnName + "_0";
        assertEquals("Only one property added",1, builder.getProperties().size());
        assertEquals("Check property name correct", expectedProperty, builder.getProperties().keySet().iterator().next());
        assertEquals("Check property value correct", nicknameValue, builder.getProperties().values().iterator().next());
        assertEquals("Check select SQL", " \n WHERE " + expectedAlias + " = :" + expectedProperty + " \n" , builder.getModifiedSelectSql());
    }

    @SuppressWarnings("unused")
    @Test
    public void testBuilder_build_null_equals() throws IllegalAccessException {
        DBI dbi = dbiResource.getDbi();
        TestDataDao dao = dbi.onDemand(TestDataDao.class);

        int id1 = 1001;
        dao.insertPerson(new TestEntities.Person().setId(id1));
        dao.insertPhone(new TestEntities.Phone().setId(id1).setType(TestEnums.PhoneType.Home).setNumber(1001001L).setIsActive(true));

        int id2 = 2002;
        dao.insertPerson(new TestEntities.Person().setId(id2));
        dao.insertPhone(new TestEntities.Phone().setId(id2).setType(TestEnums.PhoneType.Home).setNumber(1001001L).setIsActive(false));
        dao.insertPhone(new TestEntities.Phone().setId(id2).setType(TestEnums.PhoneType.Home).setNumber(2002001L).setIsActive(true));

        // Person 1 and person 2 has the same number but person2's one is inactive now

        final String tableAlias = "a";
        final String tableColumnName = "Nickname";
        final String nicknameValue = null;
        WhereConditions inputWhereConds = new WhereConditions() {

            @Equals(alias = tableAlias, columnName = tableColumnName)
            private String nickname = nicknameValue;

            private String notUsedStr;

            private Integer notUsedInt;

        };

        DynamicQueryBuilder builder = new DynamicQueryBuilder("", inputWhereConds);
        builder.build();

        final String expectedAlias = tableAlias + "." + tableColumnName;
        assertEquals("No property added",0, builder.getProperties().size());
        assertEquals("Check select SQL", " \n WHERE " + expectedAlias + " is null \n" , builder.getModifiedSelectSql());
    }

    @Test
    public void testInnerJoin() {
        TestDataDao dao = dbiResource.getDbi().onDemand(TestDataDao.class);

        int id1 = 1001;
        dao.insertPerson(new TestEntities.Person().setId(id1));
        dao.insertAlias(new TestEntities.Nickname().setId(id1).setNickname("Person1"));
        dao.insertAlias(new TestEntities.Nickname().setId(id1).setNickname("Human"));

        int id2 = 2002;
        dao.insertPerson(new TestEntities.Person().setId(id2));
        dao.insertAlias(new TestEntities.Nickname().setId(id2).setNickname("Person1"));
        dao.insertAlias(new TestEntities.Nickname().setId(id2).setNickname("Human"));

        // TODO - insert checks
        assertEquals(1, 1);
    }
}
