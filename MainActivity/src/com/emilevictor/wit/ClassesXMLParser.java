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

	private static Class readClass(XmlPullParser parser) throws XmlPullParserException, IOException, ClassNotCompleteException {
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

		if (cls.buildingName != null &&
				cls.campusCode != null &&
				cls.classType != null &&
				cls.courseCode != null &&
				cls.day != null &&
				cls.finishTime != null &&
				cls.room != null &&
				cls.semesterId != null &&
				cls.semesterNum != null &&
				cls.semesterYear != null &&
				cls.startTime != null &&
				cls.finishTime != null
				)
		{
			return cls;
		} else {
			throw new ClassNotCompleteException("The class that was returned was " +
					"not complete - there was missing information.");
		}

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
				try {
					sessions.add(readClass(parser));
				} catch (ClassNotCompleteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				skip(parser);
			}
		}
		return sessions;
	}

	public static List<Class> parse (InputStream in) throws XmlPullParserException, IOException {
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
