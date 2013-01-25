package com.emilevictor.wit.computeravailability;

public class AvailabilityOverviewResult {
	public String roomName;
	public Integer totalAvailable;
	public Integer currentAvailable;
	
	public AvailabilityOverviewResult() {
		super();
	}
	
	
	public AvailabilityOverviewResult(String roomName, Integer totalAvailable, Integer currentAvailable)
	{
		super();
		this.roomName = roomName;
		this.totalAvailable = totalAvailable;
		this.currentAvailable = currentAvailable;
	}
}
