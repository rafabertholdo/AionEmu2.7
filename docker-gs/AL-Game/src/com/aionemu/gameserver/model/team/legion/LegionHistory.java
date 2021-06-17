package com.aionemu.gameserver.model.team.legion;

import java.sql.Timestamp;

public class LegionHistory {

	private LegionHistoryType legionHistoryType;
	private String name = "";
	private Timestamp time;
	private String description;

	public LegionHistory(LegionHistoryType legionHistoryType, String name, Timestamp time, String description) {
		this.legionHistoryType = legionHistoryType;
		this.name = name;
		this.time = time;
		this.description = description;
	}

	public LegionHistoryType getLegionHistoryType() { return legionHistoryType; }
	public String getName() { return name; }
	public Timestamp getTime() { return time; }
	public String getDescription() { return description; }
}
