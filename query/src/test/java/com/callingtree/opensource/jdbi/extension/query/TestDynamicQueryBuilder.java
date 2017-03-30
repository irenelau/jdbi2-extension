package com.callingtree.opensource.jdbi.extension.query;

import com.callingtree.opensource.jdbi.extension.annotation.Equals;
import com.callingtree.opensource.jdbi.extension.query.rule.DbiResource;
import org.junit.Rule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import static org.junit.Assert.assertEquals;

public class TestDynamicQueryBuilder {

    @Rule
    public DbiResource dbiResource = new DbiResource(TestUtil.CREATE_PERSON_TABLE_SQL,
            TestUtil.CREATE_ALIAS_TABLE_SQL,
            TestUtil.CREATE_PHONE_TABLE_SQL);

    @Test
    public void testSimpleWhereCondition() throws IllegalAccessException {
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

        assertEquals("Only one properties added",1, builder.getProperties().size());
        assertEquals("Check property name correct", tableAlias + "_" + tableColumnName + "_0", builder.getProperties().keySet().iterator().next());
        assertEquals("Check property value correct", nicknameValue, builder.getProperties().values().iterator().next());
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
