package com.callingtree.opensource.jdbi.extension.query;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

public abstract class TestDataDao {

    @SqlUpdate(" INSERT INTO PERSON ( Id ) VALUES ( :id ) ")
    abstract int insertPerson(@BindBean TestEntities.Person person);

    @SqlUpdate(" INSERT INTO NICKNAME ( Id, Nickname ) VALUES ( :id, :nickname ) ")
    abstract int insertAlias(@BindBean TestEntities.Nickname nickname);

    @SqlUpdate(" INSERT INTO PHONE ( Id, Type, Number, IsActive ) VALUES ( :id, :type, :number, :isActive ) ")
    abstract int insertPhone(@BindBean TestEntities.Phone phone);

    @RegisterMapper(TestMappers.PersonMapper.class)
    @SqlQuery(" SELECT * FROM PERSON ")
    abstract List<TestEntities.Person> findAllPersons();

    @RegisterMapper(TestMappers.AliasMapper.class)
    @SqlQuery(" SELECT * FROM NICKNAME ")
    abstract List<TestEntities.Nickname> findAllAliases();

    @RegisterMapper(TestMappers.PhoneMapper.class)
    @SqlQuery(" SELECT * FROM PHONE ")
    abstract List<TestEntities.Phone> findAllPhones();
}
