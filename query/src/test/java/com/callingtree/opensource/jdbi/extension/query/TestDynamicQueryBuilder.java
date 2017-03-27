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

        WhereConditions whereCondition = new WhereConditions() {
            @Equals(alias = "a", columnName = "Nickname")
            private String nickname = "foo";

            private String notUsedStr;

            private Integer notUsedInt;
        };

        DynamicQueryBuilder.build(dbi.open(), "", whereCondition);

        // TODO - insert checks
        assertEquals(1, 1);

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
