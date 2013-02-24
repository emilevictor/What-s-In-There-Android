package com.emilevictor.wit.computeravailability.uqlibrary;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

import com.emilevictor.wit.helpers.Settings;

public class ASyncTaskScrapeUQLibraryInfo  extends AsyncTask<String, Void, List<Library>> {


	
	@Override
	
	protected List<Library> doInBackground(String... params) {
		try {
			Document doc = Jsoup.connect(Settings.libraryHourInfoPageUrl).get();
			
			Elements libraryRows = doc.getElementsByTag("tr");
			
			for (Element row : libraryRows)
			{
				Elements titleLink = row.getElementsByClass("uqlnavigationlink");
				
				Log.w("Text in title",titleLink.get(0).text());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
		return null;
	}

}
