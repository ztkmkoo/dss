package io.github.ztkmkoo.dss.core.actor.blocking.jdbc;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.DeleteQuery;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UpdateQuery;

import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;

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
