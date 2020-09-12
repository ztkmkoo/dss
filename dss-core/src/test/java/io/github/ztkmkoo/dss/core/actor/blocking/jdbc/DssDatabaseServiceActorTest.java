package io.github.ztkmkoo.dss.core.actor.blocking.jdbc;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;

public class DssDatabaseServiceActorTest extends AbstractDssActorTest {

    @Test
    void selectTest() throws IOException {
        SelectQuery query = new SelectQuery()
                .addCustomFromTable("test_table")
                .addCustomColumns("*")
                .addCondition(BinaryCondition.equalTo("name", "JuHyun"))
                .addCondition(BinaryCondition.equalTo("age", "10"))
                .validate();
        String statement = query.toString();
        System.out.println(statement);
    }
}
