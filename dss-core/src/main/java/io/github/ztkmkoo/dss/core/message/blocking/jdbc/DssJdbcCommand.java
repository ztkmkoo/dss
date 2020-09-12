package io.github.ztkmkoo.dss.core.message.blocking.jdbc;

import akka.actor.typed.ActorRef;
import com.healthmarketscience.sqlbuilder.Condition;
import io.github.ztkmkoo.dss.core.message.DssCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


public interface DssJdbcCommand extends DssCommand {

	@Getter @Setter
	class SelectResponse implements DssJdbcCommand {
		private static final long serialVersionUID = -3627484230969177121L;
		String query;

		@Builder
		public SelectResponse(String query) {
			this.query = query;
		}
	}

	@Getter @Setter
	class Select implements  DssJdbcCommand, DssSelectCommand {
		private static final long serialVersionUID = 1430528845479750266L;
		private String tableName;
		private String[] columns;
		private Map<String, String> param;
		private List<Condition> conditions;
		private ActorRef<SelectResponse> sender;

		@Builder
		public Select(String tableName, String[] columns, Map<String, String> param, 
				List<Condition> conditions, ActorRef<SelectResponse> sender) {
			this.tableName = tableName;
			this.columns = columns;
			this.param = param;
			this.conditions = conditions;
			this.sender = sender;
		}
	}
}
