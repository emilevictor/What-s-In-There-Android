package com.emilevictor.wit.computeravailability;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class EAITXMLParser {
	private static final String ns = null;


	public EAITXMLParser() {

	}

	private static Room readRoom(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "lab");
		String room = null;
		Integer totalAvailable = null;
		Integer currentAvailable = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("name")) {
				room = readText(parser);
			} else if (name.equals("total")) {
				totalAvailable = Integer.valueOf(readText(parser));
			} else if (name.equals("available")) {
				currentAvailable = Integer.valueOf(readText(parser));
			}else {

				skip(parser);
			}
		}
		


		return new Room(room,totalAvailable,currentAvailable);
	}



	

	private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private static List<Room> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		List<Room> rooms = new ArrayList<Room>();

		parser.require(XmlPullParser.START_TAG, ns, "summary");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("lab")) {
				rooms.add(readRoom(parser));
			} else {
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG, ns, "summary");
		return rooms;
	}

	@SuppressWarnings({ "rawtypes"})
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
