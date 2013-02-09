package com.emilevictor.wit.helpers;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;

public class FileCache {

	
	
	public static boolean checkCacheValidityAgainstCurrentTimetable(Context context)
	{
		//Get sharedpreferences timetableID
		SharedPreferences settings = context.getSharedPreferences(Settings.preferencesFilename, 0);
		Long mCurrentTimetable = settings.getLong("rotaDefaultTimetableId", -1);
		Long cacheTimetableId = -1L;
		
		//Get timetableID from file
		if (mCurrentTimetable != -1)
		{
			File cacheDirId = new File(context.getCacheDir(), "rotaTimetableCaches");

			File cacheFileId = new File(cacheDirId,"rotaTimetableId");
			if (cacheFileId.exists())
			{
				try {
					DataInputStream in = 
					        new DataInputStream(new FileInputStream(cacheFileId.getAbsolutePath()));
					cacheTimetableId = in.readLong();
					in.close();
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return false;
			}
			
		} else {
			return false;
		}
		
		//Compare
		if (cacheTimetableId.intValue() == mCurrentTimetable.intValue())
		{
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean checkExistenceOfCachedTimetable(Context context)
	{
		File cacheDir = new File(context.getCacheDir(), "rotaTimetableCaches");
		File cacheFile = new File(cacheDir,"rotaTimetable");
		return cacheFile.exists();
	}
}
