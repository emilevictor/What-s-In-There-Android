package com.emilevictor.wit.whats_in_there;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

import com.emilevictor.wit.helpers.Class;


public class RetreiveClassesTask extends AsyncTask<String, Void, List<Class>> {

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
	protected List<Class> doInBackground(String... urlPlusRoomPlusBuilding) {
		
    	//urlPlusRoomPlusBuilding[0] url
    	//urlPlusRoomPlusBuilding[1] room
    	//urlPlusRoomPlusBuilding[2] building
		
		InputStream stream = null;
		
		try {
			stream = downloadUrl(urlPlusRoomPlusBuilding[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return ClassesXMLParser.parse(stream);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
