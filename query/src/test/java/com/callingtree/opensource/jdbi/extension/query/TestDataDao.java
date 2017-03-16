package com.callingtree.opensource.jdbi.extension.query;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public abstract class TestDataDao {

    @SqlUpdate(" INSERT INTO PERSON ( Id ) VALUES ( :id ) ")
    abstract int insertPerson(@BindBean TestEntities.Person person);

    @SqlUpdate(" INSERT INTO ALIAS ( Id, Alias ) VALUES ( :id, :alias ) ")
    abstract int insertAlias(@BindBean TestEntities.Alias alias);

    @SqlUpdate(" INSERT INTO PHONE ( Id, Type, Number, IsActive ) VALUES ( :id, :type, :number, :isActive ) ")
    abstract int insertPhone(@BindBean TestEntities.Phone phone);

}
