package io.github.ztkmkoo.dss.core.actor.blocking.jdbc;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.InsertQuery;import com.healthmarketscience.sqlbuilder.InsertSelectQuery;
import com.healthmarketscience.sqlbuilder.SelectQuery;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.message.blocking.jdbc.DssJdbcCommand;
import lombok.extern.log4j.Log4j;


@Log4j
public class DssDatabaseServiceActor implements DssDatabaseService<DssJdbcCommand, DssJdbcCommand> {
	
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
	
    public static Behavior<DssJdbcCommand> create() {
        return Behaviors.setup(context -> new DssDatabaseServiceActor(context).dssDatabaseServiceActor());
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
				.validate();
		
		String selectStatement = selectQuery.toString();
		log.info("selectStatement: " + selectStatement);
		
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
