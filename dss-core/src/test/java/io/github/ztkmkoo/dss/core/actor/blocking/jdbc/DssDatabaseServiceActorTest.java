package io.github.ztkmkoo.dss.core.actor.blocking.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UpdateQuery;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.message.blocking.jdbc.DssJdbcCommand;

public class DssDatabaseServiceActorTest extends AbstractDssActorTest {
	
	private static final String testTable = "test_table";

    @Test
    void selectTest() throws IOException {
        SelectQuery query = new SelectQuery()
                .addCustomFromTable(testTable)
                .addCustomColumns("name", "age")
                .addCondition(BinaryCondition.equalTo("name", "JuHyun"))
                .addCondition(BinaryCondition.equalTo("age", "10"))
                .validate();
        
        String statement = query.toString();
        System.out.println(statement);
    }
    
    @Test
    void selectTest2() {
    	TestProbe<DssJdbcCommand.SelectResponse> actorRef = testKit.createTestProbe();
    	ActorRef<DssJdbcCommand> a = testKit.spawn(DssDatabaseServiceActor.create());
    	
    	List<Condition> conditions = new ArrayList<>();
    	conditions.add(BinaryCondition.equalTo("name", "JuHyun"));
    	conditions.add(BinaryCondition.equalTo("age", "10"));
    	
    	DssJdbcCommand c = DssJdbcCommand.Select
    			.builder()
    			.tableName("test_table")
    			.columns(new String[] {"name", "age"})
    			.conditions(conditions)
    			.sender(actorRef.getRef())
    			.build();
    	
    	a.tell(c);
    	
    	/* selectTest() 결과 쿼리 */
    	String testQuery = "SELECT 'name','age' FROM test_table WHERE (('name' = 'JuHyun') AND ('age' = '10'))";
    	DssJdbcCommand.SelectResponse response = actorRef.receiveMessage();
    	assertEquals(testQuery, response.getQuery());
    }
    
    @Test
    void insertTest() throws IOException {
    	InsertQuery query = new InsertQuery(testTable)
    			.addCustomColumn("name", "JuHyun")
    			.addCustomColumn("age", "10")
    			.validate();
    	String statement = query.toString();
    	System.out.println(statement);
    }
    
    @Test
    void updateTest() throws IOException {
    	UpdateQuery query = new UpdateQuery(testTable)
    			.addCustomSetClause("name", "JuHyun")
    			.addCondition(BinaryCondition.equalTo("age", "10"))
    			.validate();
    	
    	String statement = query.toString();
    	System.out.println(statement);
    }
    
    @Test
    void deleteTest() throws IOException {
    	DeleteQuery query = new DeleteQuery(testTable)
    			.addCondition(BinaryCondition.equalTo("name", "JuHyun"))
    			.addCondition(BinaryCondition.equalTo("age", 10))
    			.validate();
    	
    	String statement = query.toString();
    	System.out.println(statement);
    }
}
