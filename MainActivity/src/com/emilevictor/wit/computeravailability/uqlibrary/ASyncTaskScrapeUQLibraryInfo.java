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

		
		
		
		
		return libraries;
	}

	private List<Library> getLibraryComputerAvailabilities(List<Library> libraries,
			Document availabilityDoc) {
		
		Elements libraryRows = availabilityDoc.select("tr");
		
		for (Element row : libraryRows)
		{
			//Get the title, then identify the index of the current library (if it exists)
			//Then update info.
			
			String currentLibrary = row.select("[href]").text();
			int currentLibraryNumber = -1;
			
			if (currentLibrary.equals("Architecture & Music Library"))
			{
				currentLibrary = "Architecture / Music Library";
			} else if (currentLibrary.equals("Herston Medical Library"))
			{
				currentLibrary = "Herston Health Sciences Library";
			} else if (currentLibrary.equals("D.H. Engineering & Sciences Library"))
			{
				currentLibrary = "Dorothy Hill Engineering & Sciences Library";
			} else if (currentLibrary.equals("Mater Hospital Library"))
			{
				currentLibrary = "Mater McAuley Library";
			} else if (currentLibrary.equals("Gatton Campus Library"))
			{
				currentLibrary = "Gatton Library";
			} else if (currentLibrary.equals("Duhig Building"))
			{
				currentLibrary = "Study Areas in the Duhig Library Building";
			}
			
			//Check which library this matches.
			for (int i = 0; i < libraries.size(); i++)
			{
				if (libraries.get(i).getName().equals(currentLibrary))
				{
					currentLibraryNumber = i;
					break;
				}
			}
			
			if (currentLibraryNumber >= 0)
			{
				//Get current and total pc availability
				String pcAvailability = row.select(".right").text();
				String [] splitAvailability = pcAvailability.split(" ");
				
				//Update library.
				libraries.get(currentLibraryNumber).setNumberComputersAvailable(Integer.valueOf(splitAvailability[0]));
				libraries.get(currentLibraryNumber).setNumberComputersTotal(Integer.valueOf(splitAvailability[3]));
			}
			
			
			
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
				Elements elems = hoursTableRows.get(i).select(".uqtext");
				if (elems.size() > 0)
				{
					libraries.get(libraries.size()-1).setExtraComments(elems.get(0).text());
				}
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
