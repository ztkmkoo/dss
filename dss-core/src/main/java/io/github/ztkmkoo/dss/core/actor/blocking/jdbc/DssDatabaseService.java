package io.github.ztkmkoo.dss.core.actor.blocking.jdbc;

import akka.actor.typed.Behavior;
import io.github.ztkmkoo.dss.core.message.DssCommand;

public interface DssDatabaseService<T, E extends DssCommand> {
	
	void commit();
	Behavior<T> select(E msg);
	int insert(E msg);
	int update(E msg);
	int delete(E msg);
}
