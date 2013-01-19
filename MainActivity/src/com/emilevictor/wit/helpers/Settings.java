package com.emilevictor.wit.helpers;

public class Settings {

	public static String currentSemesterNumber = "6220";
	
	//String parts for the rota course ID API
	public static String idAPIurlStart = "http://rota.eait.uq.edu.au/offerings/find.xml?with={%22course_code%22:%22";
	public static String idAPIurlEnd = "%22,%22semester_id%22:%22"+currentSemesterNumber+"%22}";
}
