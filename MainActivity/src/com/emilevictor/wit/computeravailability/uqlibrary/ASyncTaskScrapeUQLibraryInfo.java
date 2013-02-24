package com.emilevictor.wit.computeravailability.uqlibrary;

import java.io.IOException;
import java.util.ArrayList;
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
		
		List<Library> libraries = new ArrayList<Library>(0);
		try {
			Document mHourPageDoc = Jsoup.connect(Settings.libraryHourInfoPageUrl).get();
			Document availabilityDoc = Jsoup.connect(Settings.libraryComputerOverviewUrl).get();
			
			//Get the libraries and their opening hours.
			libraries = getLibraryOpenHours(libraries, mHourPageDoc);
			libraries = getLibraryComputerAvailabilities(libraries, availabilityDoc);
			
			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
		return null;
	}

	private List<Library> getLibraryComputerAvailabilities(List<Library> libraries,
			Document availabilityDoc) {
		
		Elements libraryRows = availabilityDoc.select(".left");
		
		for (Element lib : libraryRows)
		{
			
		}
		
		
		
		return libraries;
		
	}

	private List<Library> getLibraryOpenHours(List<Library> libraries,
			Document mHourPageDoc) {
		Element hoursTable = mHourPageDoc.getElementById("hourstable");
		
		Elements hoursTableRows = hoursTable.getElementsByTag("tr");
		
		int realRowIndex = 1;
		boolean increaseRowIndex = false;
		//For each table row, except the first one
		for (int i = 1; i < hoursTableRows.size(); i++)
		{
			increaseRowIndex = false;
			Library library = new Library();
			
			Elements libraryName = hoursTableRows.get(i).select(".uqlnavigationlink");
			
			if (libraryName.size() > 0)
			{
				library.setName(libraryName.get(0).text());
				Log.w("Library name", libraryName.get(0).text());
				increaseRowIndex = true;
			} else {
				
			}
			
			
			
			//Now we look up today's cell. This is done by concatenating the current row to the first column.
			
			Element myCell = mHourPageDoc.getElementById("c"+String.valueOf(realRowIndex) + "x1");
			//Log.w("Cell","c"+String.valueOf(realRowIndex) + "x1");
			if (myCell != null)
			{
				if (myCell.text().equals("CLOSED"))
				{
					library.setOpenToday(false);
				} else {
					library.setOpenToday(true);
				}
				
				if (myCell.text().equals("open 24 hours"))
				{
					library.setOpenTimesToday(myCell.text());
				} else if (!myCell.text().equals("CLOSED"))
				{
					library.setOpenTimesToday(myCell.text());
				}	
				
				Log.w("My cell times", myCell.text());
				
			}
			
			if (increaseRowIndex)
			{
				realRowIndex++;
				libraries.add(library);
			}
			
		}
		return libraries;
	}
	
	

}
