package com.emilevictor.wit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class buildingXMLParser {
	private static final String ns = null;


	public buildingXMLParser() {

	}

	public static String getBuildingIdFromNumber(String realBuildingNumber, List<Building> buildings)
	{
		for (Building building : buildings)
		{
			if (building.number.equals(realBuildingNumber))
			{
				return building.id;
			}
		}
		return "NOTFOUND";

	}

	public static class Building {
		public final String id;
		public final String number;

		private Building(String id, String number) {
			this.id = id;
			this.number = number;
		}
	}

	private static Building readBuilding(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "building");
		String id = null;
		String number = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("id")) {
				id = readId(parser);
			} else if (name.equals("number")) {
				number = readNumber(parser);
			} else {
				skip(parser);
			}
		}
		return new Building(id,number);
	}



	private static String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "id");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "id");
		return title;
	}

	private static String readNumber (XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "number");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "number");
		return title;
	}

	private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private static List<Building> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<Building> entries = new ArrayList<Building>();

		parser.require(XmlPullParser.START_TAG, ns, "buildings");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("building")) {
				entries.add(readBuilding(parser));
			} else {
				skip(parser);
			}
		}
		return entries;
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
