package com.emilevictor.wit.helpers;

public class Settings {

	public static String currentSemesterNumber = "6220";
	
	//String parts for the rota course ID API
	public static String idAPIurlStart = "http://rota.eait.uq.edu.au/offerings/find.xml?with={%22course_code%22:%22";
	public static String idAPIurlEnd = "%22,%22semester_id%22:%22"+currentSemesterNumber+"%22}";
	
	//Library overview
	public static String libraryComputerOverviewUrl = "http://www.library.uq.edu.au/uqlsm/availablepcsembed.php?q=ask-it/computer-availability";
	public static String libraryHourInfoPageUrl = "http://www.library.uq.edu.au/hours/";
	
	//Library floor plans
	public static String libraryFloorPlanUrlStart = "http://www.library.uq.edu.au/uqlsm/map.php?embed&building=";
	public static String libraryFloorPlanUrlEnd = "&room=";
	
	//Computer availability APIs
	public static String eaitAvailabilityXMLurl = "https://student.eait.uq.edu.au/data/uqlsm/api.php?key=f8176970-6527-11e2-bcfd-0800200c9a66&cmd=xmlsummary";

	//UQRota login url
	public static String uqRotaLoginUrl = "https://www.uqrota.net/login";
	public static String uqRotaTimetableSearchUrl = "https://www.uqrota.net/timetables/search?mine=yes&_dc=1234"; //Fake DC...
	public static String uqRotaTimetableFetchUrl = "https://www.uqrota.net/timetables/get?timetable_id=";
	public static String UQRotaBaseUrl = "https://www.uqrota.net/";
	public static String rotaCacheFilename = "/uqRotaTimetable.rota";
	//Preferences file name
	public static String preferencesFilename = "prefsFile";
	
	//I'm feeling lucky
	public static String firstIFL = "http://rota.eait.uq.edu.au/sparql?query=PREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0APREFIX%20tt%3A%20%3Chttp%3A%2F%2Frota.eait.uq.edu.au%2Fvocab%2Fresource%2Ftt%2F%3E%0APREFIX%20rota%3A%20%3Chttp%3A%2F%2Frota.eait.uq.edu.au%2Fvocab%2Fresource%2Frota%2F%3E%0Aselect%20%3Fcode%20%3Fbldgnum%20%3Fbldg%20%3Froom%20%7B%0A%20%3Fs%20a%20tt%3ASession%3B%0A%20%20%20%20tt%3AdayOfWeek%20%3Fday%3B%0A%20%20%20%20tt%3AstartMinutes%20%3Fstart%3B%0A%20%20%20%20tt%3AfinishMinutes%20%3Ffinish%3B%0A%20%20%20%20tt%3AtaughtInRoom%20%3Froom%3B%0A%20%20%20%20tt%3AtaughtInBuilding%20%3Fb.%0A%20%3Fb%20a%20rota%3ABuilding%3B%0A%20%20%20%20rota%3AcontainedIn%20%3Chttp%3A%2F%2Frota.eait.uq.edu.au%2Fresource%2Fcampus%2F";
	//Insert STLUC, GATTN or IPSWCH
	public static String secondIFL = "%3E%3B%0A%20%20%20%20rota%3AbuildingNumber%20%3Fbldgnum%3B%0A%20%20%20%20rdfs%3Alabel%20%3Fbldg.%0A%20%3Fg%20a%20tt%3AGroup%3B%20tt%3AdividedInto%20%3Fs.%0A%20%3Fss%20a%20tt%3ASeries%3B%20rdfs%3Alabel%20%22L%22%3B%20tt%3AdividedInto%20%3Fg.%0A%20%3Fo%20a%20rota%3AOffering%3B%0A%20%20%20%20tt%3Atimetables%20%3Fss%3B%0A%20%20%20%20rota%3AofferedIn%20%5Ba%20rota%3ASemester%3B%20rdfs%3Aidentifier%20%3Fstrm%5D.%0A%20%3Fc%20a%20rota%3ACourse%3B%20%0A%20%20%20%20rota%3AcourseCode%20%3Fcode%3B%0A%20%20%20%20rota%3AofferedAs%20%3Fo.%0A%20FILTER%20((%3Fstart%20%3C%20";
	//Insert start time
	public static String thirdIFL = ")%20%26%26%20(%3Ffinish%20%3E%20";
	//insert finish time
	public static String fourthIFL = "))%0A%7D%20order%20by%20asc(rand())%20limit%201&day=";
	//Insert day in 3 letter form (Mon,Tue,Wed, etc)
	public static String fifthIFL = "&time=";
	//Insert start time again
	public static String sixthIFL = "&time_type=integer&strm="+currentSemesterNumber+"&strm_type=integer&output=json";
	
}
