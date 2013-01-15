package com.emilevictor.wit;

public class TimetableResult {
	public String courseCode;
	public String classType;
	
	public TimetableResult() {
		super();
	}
	
	
	public TimetableResult(String classType, String courseCode)
	{
		super();
		this.classType = classType;
		this.courseCode = courseCode;
	}
}
