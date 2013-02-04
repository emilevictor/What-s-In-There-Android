package com.emilevictor.wit.uqrota;

import java.io.Serializable;

public class RotaClass implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9013941395728050073L;
	private Long startTime;
	private Long finishTime;
	private String day;
	private String building;
	private String room;
	private String buildingNo;
	private String courseNameAndType;
	private String courseType;
	
	public RotaClass()
	{
		this.setStartTime(-1L);
		this.setFinishTime(-1L);
		this.setDay(null);
		this.setBuilding(null);
		this.setRoom(null);
		this.setBuildingNo(null);
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Long finishTime) {
		this.finishTime = finishTime;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}

	public String getCourseNameAndType() {
		return courseNameAndType;
	}

	public void setCourseNameAndType(String courseNameAndType) {
		this.courseNameAndType = courseNameAndType;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

}
