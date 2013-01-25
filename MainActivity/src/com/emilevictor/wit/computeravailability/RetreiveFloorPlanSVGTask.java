package com.emilevictor.wit.computeravailability;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

import com.emilevictor.wit.helpers.Class;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;


public class RetreiveFloorPlanSVGTask extends AsyncTask<String, Void, SVG> {

    private InputStream downloadUrl(String urlPlusRoomPlusBuilding) throws IOException {
    	

    	
    	
        URL url = new URL(urlPlusRoomPlusBuilding);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        
        InputStream stream = conn.getInputStream();
        return stream;
    }
	
	@Override
	protected SVG doInBackground(String... urls) {
		
    	//urlPlusRoomPlusBuilding[0] url
    	//urlPlusRoomPlusBuilding[1] room
    	//urlPlusRoomPlusBuilding[2] building
		
		InputStream stream = null;
		
		try {
			stream = downloadUrl(urls[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SVGParser.getSVGFromInputStream(stream);
	}

}
