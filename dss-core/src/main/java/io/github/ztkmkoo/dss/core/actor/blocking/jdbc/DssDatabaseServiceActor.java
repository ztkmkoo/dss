package io.github.ztkmkoo.dss.core.actor.blocking.jdbc;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.healthmarketscience.sqlbuilder.InsertSelectQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.message.blocking.jdbc.DssJdbcCommand;


public class DssDatabaseServiceActor implements DssDatabaseService<DssJdbcCommand, DssJdbcCommand> {
	
    public static Behavior<DssJdbcCommand> create() {
        return Behaviors.setup(context -> new DssDatabaseServiceActor(context).dssDatabaseServiceActor());
    }
    
	private final ActorContext<DssJdbcCommand> context;
	
	private DssDatabaseServiceActor(ActorContext<DssJdbcCommand> context) {
		this.context = context;
	}
	
	private Behavior<DssJdbcCommand> dssDatabaseServiceActor() {
		return Behaviors
				.receive(DssJdbcCommand.class)
				.onMessage(DssJdbcCommand.Select.class, this::select)
				.build();
	}
	

	@Override
	public void commit() {
		
	}
	
	@Override
	public Behavior<DssJdbcCommand> select(DssJdbcCommand msg) {
		final DssJdbcCommand.Select selectMsg = (DssJdbcCommand.Select)msg;
		
		SelectQuery selectQuery = new SelectQuery()
				.addCustomFromTable(selectMsg.getTableName())
				.addCustomColumns(selectMsg.getColumns())
				.addCondition(BinaryCondition.equalTo("name", "이주현"))
				.addCondition(BinaryCondition.equalTo("age", "10"))
				.validate();
		String selectStatement = selectQuery.toString();
		
		selectMsg.getSender().tell(DssJdbcCommand.SelectResponse.builder().query(selectStatement).build());
		
		return Behaviors.same();
	} 
	
	@Override
	public int insert(DssJdbcCommand msg) {
		return 0;
	}

	@Override
	public int update(DssJdbcCommand msg) {
		return 0;
	}

	@Override
	public int delete(DssJdbcCommand msg) {
		return 0;
	}
}
