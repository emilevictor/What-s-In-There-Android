package com.emilevictor.wit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class ClassesXMLParser {
	private static final String ns = null;


	public ClassesXMLParser() {

	}



	public static class Class {
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




		private Class (String courseCode, String semesterId, String semesterNum,
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
	}
	
	private static Class readGroup(XmlPullParser parser, Class inputClass) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "group");
		//Read until the end group tag
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String name = parser.getName();
			
			if (name.equals("id"))
			{
				skip(parser);
			} else if (name.equals("series"))
			{
				inputClass = readSeries(parser,inputClass);
			} else if (name.equals("name"))
			{
				skip(parser);
			} else if (name.equals("groupname")) {
				skip(parser);
			}
		}
		
		parser.require(XmlPullParser.END_TAG, ns, "group");
		
		
		return inputClass;
	}
	
	private static Class readSeries(XmlPullParser parser, Class inputClass) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "series");
		
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String name = parser.getName();
			
			if (name.equals("id"))
			{
				skip(parser);
			} else if (name.equals("offering"))
			{
				inputClass = readOffering(parser, inputClass);
			} else if (name.equals("name"))
			{
				inputClass.classType = readText(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG, ns, "series");
		return inputClass;
	}
	
	private static Class readOffering(XmlPullParser parser, Class inputClass) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "offering");
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String name = parser.getName();
			
			if (name.equals("id"))
			{
				skip(parser);
			} else if (name.equals("course"))
			{
				inputClass.courseCode = readText(parser);
			} else if (name.equals("semester"))
			{
				inputClass = readSemester(parser,inputClass);
			} else if (name.equals("location"))
			{
				skip(parser);
			} else if(name.equals("mode"))
			{
				skip(parser);
			} else if (name.equals("sinet_class"))
			{
				skip(parser);
			} else if (name.equals("campus"))
			{
				inputClass = readCampus(parser, inputClass);
			} else if (name.equals("last_update"))
			{
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG, ns, "offering");
		return inputClass;
	}
	
	private static Class readCampus(XmlPullParser parser, Class inputClass) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG,ns,"campus");
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String name = parser.getName();
			
			if (name.equals("code"))
			{
				inputClass.campusCode = readText(parser);
			} else if (name.equals("name"))
			{
				skip(parser);
			}
		}
		
		parser.require(XmlPullParser.END_TAG,ns,"campus");
		return inputClass;
	}
	
	private static Class readSemester(XmlPullParser parser, Class inputClass) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG,ns,"semester");
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String name = parser.getName();
			
			if (name.equals("id"))
			{
				inputClass.semesterId = readText(parser);
			} else if (name.equals("name"))
			{
				skip(parser);
			} else if (name.equals("number"))
			{
				inputClass.semesterNum = readText(parser);
			} else if (name.equals("year"))
			{
				inputClass.semesterYear = readText(parser);
			}
		}
		
		parser.require(XmlPullParser.END_TAG,ns,"semester");
		return inputClass;
	}

	private static Class readClass(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "session");
		//String id = null;
		//String number = null;
		Class cls = new Class(null,null,null,null,null,null,null,null,null,null);
		
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("id")) {
				//id = readId(parser);
				skip(parser);
			} else if (name.equals("group")) { 
				cls = readGroup(parser,cls);
			} else if (name.equals("day")) {
				cls.day = readText(parser);
			} else if (name.equals("start")) {
				cls.startTime = readText(parser);
			} else if (name.equals("finish"))
			{
				cls.finishTime = readText(parser);
			} else if (name.equals("room"))
			{
				cls.room = readText(parser);
			} else if (name.equals("building"))
			{
				cls = readBuilding(parser,cls);
			}
			else {
				skip(parser);
			}
		}
		return cls;
	}
	
	private static Class readBuilding(XmlPullParser parser, Class inputClass) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "building");
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String name = parser.getName();
			
			if (name.equals("name"))
			{
				inputClass.buildingName = readText(parser);
			} else
			{
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG, ns, "building");
		return inputClass;
	}


	private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private static List<Class> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<Class> sessions = new ArrayList<Class>();

		parser.require(XmlPullParser.START_TAG, ns, "sessions");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("session")) {
				sessions.add(readClass(parser));
			} else {
				skip(parser);
			}
		}
		return sessions;
	}

	public static List parse (InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);

		} finally {
			in.close();
		}


	}

	private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

}
