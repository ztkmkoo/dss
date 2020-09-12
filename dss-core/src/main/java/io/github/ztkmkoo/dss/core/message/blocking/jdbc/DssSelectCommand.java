package io.github.ztkmkoo.dss.core.message.blocking.jdbc;

import io.github.ztkmkoo.dss.core.message.DssCommand;

import java.util.Map;

public interface DssSelectCommand extends DssCommand {
	
	String getTableName();
	String[] getColumns();
	Map<String, String> getParam();
	
}
