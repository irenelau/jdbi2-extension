package com.callingtree.opensource.jdbi.extension.query;

import com.callingtree.opensource.jdbi.extension.query.rule.DbiResource;
import org.junit.Rule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import static org.junit.Assert.assertEquals;

public class TestRandom {

    @Rule
    public DbiResource dbiResource = new DbiResource(TestUtil.CREATE_PERSON_TABLE_SQL,
            TestUtil.CREATE_ALIAS_TABLE_SQL,
            TestUtil.CREATE_PHONE_TABLE_SQL);

    private DBI dbi;

    @Test
    public void testInClause() {
        TestDataDao dao = dbiResource.getDbi().onDemand(TestDataDao.class);

        int id1 = 1001;
        dao.insertPerson(new TestEntities.Person().setId(id1));
        dao.insertPhone(new TestEntities.Phone().setId(id1).setType(TestEnums.PhoneType.Home).setNumber(1001001L).setIsActive(true));

        int id2 = 2002;
        dao.insertPerson(new TestEntities.Person().setId(id2));
        dao.insertPhone(new TestEntities.Phone().setId(id2).setType(TestEnums.PhoneType.Home).setNumber(1001001L).setIsActive(false));
        dao.insertPhone(new TestEntities.Phone().setId(id2).setType(TestEnums.PhoneType.Home).setNumber(2002001L).setIsActive(true));

        // Person 1 and person 2 has the same number but person2's one is inactive now

        dao.findAllPersons();
        dao.findAllAliases();
        dao.findAllPhones();

        // TODO - insert checks
        assertEquals(1, 1);

    }

    @Test
    public void testInnerJoin() {
        TestDataDao dao = dbiResource.getDbi().onDemand(TestDataDao.class);

        int id1 = 1001;
        dao.insertPerson(new TestEntities.Person().setId(id1));
        dao.insertAlias(new TestEntities.Alias().setId(id1).setAlias("Person1"));
        dao.insertAlias(new TestEntities.Alias().setId(id1).setAlias("Human"));

        int id2 = 2002;
        dao.insertPerson(new TestEntities.Person().setId(id2));
        dao.insertAlias(new TestEntities.Alias().setId(id2).setAlias("Person1"));
        dao.insertAlias(new TestEntities.Alias().setId(id2).setAlias("Human"));

        // TODO - insert checks
        assertEquals(1, 1);
    }
}
