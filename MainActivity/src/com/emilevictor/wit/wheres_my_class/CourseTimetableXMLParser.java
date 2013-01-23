package com.emilevictor.wit.wheres_my_class;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.emilevictor.wit.helpers.Class;
import com.emilevictor.wit.whats_in_there.ClassNotCompleteException;

public class CourseTimetableXMLParser {
	private static final String ns = null;
	private static List<Class> sessions;
	private static String courseCode;
	private static String groupNumber;

	public CourseTimetableXMLParser() {

	}

	private static void readInnerSeries(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "series");
		String currentSeriesType = "";
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();

			if (name.equals("name"))
			{
				currentSeriesType = readText(parser);
			} else if (name.equals("groups")) {
				readGroups(currentSeriesType,parser);
			}else {
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG,ns,"series");
	}

	private static Class readBuilding(Class cls, XmlPullParser parser) throws IOException, XmlPullParserException
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
				cls.buildingName = readText(parser);
			} else if (name.equals("campus")) {
				cls.campusCode = readText(parser);
			} else if (name.equals("number")) {
				cls.buildingNumber = readText(parser);
			}else {
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG,ns,"building");
		
		return cls;
	}
	
	private static void readSessions(String currentSeriesType, XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "sessions");
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();

			if (name.equals("session"))
			{
				try {
					sessions.add(readSession(currentSeriesType,parser));
				} catch (ClassNotCompleteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG,ns,"sessions");
	}


	private static Class readSession(String currentSeriesType, XmlPullParser parser) throws XmlPullParserException, IOException, ClassNotCompleteException
	{
		Class cls = new Class(null,null,null,null,null,null,null,null,null,null);
		parser.require(XmlPullParser.START_TAG, ns, "session");
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();

			if (name.equals("day"))
			{
				cls.day = readText(parser);
			} else if (name.equals("start")) {
				cls.startTime = readText(parser);

			} else if (name.equals("finish")) {
				cls.finishTime = readText(parser);

			} else if (name.equals("building")) {
				cls = readBuilding(cls, parser);
			} else if (name.equals("room")) {
				cls.room = readText(parser);
			}else {
			
				skip(parser);
			}
		}
		cls.courseCode = courseCode;
		cls.classType = currentSeriesType + groupNumber;
		parser.require(XmlPullParser.END_TAG,ns,"session");

		if (cls.buildingName != null &&
				cls.classType != null &&
				cls.day != null &&
				cls.finishTime != null &&
				cls.startTime != null
				)
		{
			return cls;
		} else {
			throw new ClassNotCompleteException("The class that was returned was " +
					"not complete - there was missing information.");
		}


	}

	private static void readGroup(String currentSeriesType, XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "group");
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();

			if (name.equals("sessions"))
			{
				
				readSessions(currentSeriesType,parser);
				
			} else if (name.equals("name")) {
				groupNumber = readText(parser);
			}else {
			
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG,ns,"group");
	}

	private static void readGroups(String currentSeriesType, XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "groups");
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();

			if (name.equals("group"))
			{
				readGroup(currentSeriesType,parser);
			}else {
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG,ns,"groups");
	}

	private static void readClass(XmlPullParser parser) throws XmlPullParserException, IOException, ClassNotCompleteException {
		parser.require(XmlPullParser.START_TAG, ns, "series");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("series")) {
				readInnerSeries(parser);
			} 
			else {
				skip(parser);
			}
		}

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


		parser.require(XmlPullParser.START_TAG, ns, "offering");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("series")) {
				try {
					readClass(parser);
					//sessions.add(readClass(parser));
				} catch (ClassNotCompleteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (name.equals("course")) {
				courseCode = readText(parser);
			}else {
			
				skip(parser);
			}
		}
		return sessions;
	}


	public static List<Class> parse (InputStream in) throws XmlPullParserException, IOException {
		sessions = new ArrayList<Class>();
		courseCode = "";
		groupNumber = "";

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
