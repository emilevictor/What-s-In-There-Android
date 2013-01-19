package com.emilevictor.wit.wheres_my_class;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.emilevictor.wit.whats_in_there.buildingXMLParser.Building;

public class CourseIDFinder {
	private static final String ns = null;

	public CourseIDFinder(String courseCode)
	{

	}

	public static class Building {
		public final String id;
		public final String number;
		public final String campusCode;

		private Building(String id, String number, String campusCode) {
			this.id = id;
			this.number = number;
			this.campusCode = campusCode;
		}
	}

	private static String readOffering(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "offering");
		String id = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("id")) {
				id = readText(parser);
			} else {

				skip(parser);
			}
		}

		return id;
	}


	private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private static String readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

		String id = null;
		parser.require(XmlPullParser.START_TAG, ns, "offerings");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("offering")) {
				id = readOffering(parser);
			} else {
				skip(parser);
			}
		}
		return id;
	}

	public static String parse (InputStream in) throws XmlPullParserException, IOException {
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
