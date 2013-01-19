package com.emilevictor.wit.helpers;

import java.io.Serializable;

public class Class implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7492529789345967789L;
	public String courseCode;
	public String semesterId;
	public String semesterNum;
	public String semesterYear;
	public String campusCode;
	public String day;
	public String startTime;
	public String finishTime;
	public String room;
	public String classType;
	public String buildingName;




	public Class (String courseCode, String semesterId, String semesterNum,
			String semesterYear, String campusCode,
			String day, String startTime, String finishTime,
			String room, String classType) {
		this.courseCode = courseCode;
		this.semesterId = semesterId;
		this.semesterNum = semesterNum;
		this.semesterYear = semesterYear;
		this.campusCode = campusCode;
		this.day = day;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.room = room;
		this.classType = classType;
	}
	
	public Class() {
		this.courseCode = "";
		this.semesterId = "";
		this.semesterNum = "";
		this.semesterYear = "";
		this.campusCode = "";
		this.day = "";
		this.startTime = "";
		this.finishTime = "";
		this.room = "";
		this.classType = "";
	}
}