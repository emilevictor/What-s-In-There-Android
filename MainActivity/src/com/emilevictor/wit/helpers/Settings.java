package com.emilevictor.wit.helpers;

public class Settings {

	public static String currentSemesterNumber = "6220";
	
	//String parts for the rota course ID API
	public static String idAPIurlStart = "http://rota.eait.uq.edu.au/offerings/find.xml?with={%22course_code%22:%22";
	public static String idAPIurlEnd = "%22,%22semester_id%22:%22"+currentSemesterNumber+"%22}";
	
	//Library floor plans
	public static String libraryFloorPlanUrlStart = "http://www.library.uq.edu.au/uqlsm/map.php?embed&building=";
	public static String libraryFloorPlanUrlEnd = "&room=";
	
	//Computer availability APIs
	public static String eaitAvailabilityXMLurl = "https://student.eait.uq.edu.au/data/uqlsm/api.php?key=f8176970-6527-11e2-bcfd-0800200c9a66&cmd=xmlsummary";

	//UQRota login url
	public static String uqRotaLoginUrl = "https://www.uqrota.net/login";
}
