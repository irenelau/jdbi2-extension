package com.callingtree.opensource.jdbi.extension.query.testrule;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.rules.ExternalResource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.HandleCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class DbiResource extends ExternalResource {

    private static Logger logger = LoggerFactory.getLogger(DbiResource.class);

    private DBI dbi;

    private final String[] createSqls;

    public DbiResource(String... createSqls) {
        this.createSqls = createSqls;
    }

    @Override
    protected void before() throws Throwable {
        final DataSource ds = JdbcConnectionPool.create("jdbc:h2:mem:test", "test", "test");
        this.dbi = new DBI(ds);

        getDbi().withHandle(new HandleCallback<Void>() {
            public Void withHandle(Handle handle) throws Exception {

                for (String createSql : createSqls) {
                    handle.createStatement(createSql).execute();
                }

                logger.debug("Table(s) created");
                return null;
            }
        });
    }

    public DBI getDbi() {
        return dbi;
    }
}
