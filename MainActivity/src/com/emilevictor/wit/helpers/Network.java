package com.emilevictor.wit.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {
	private Context context;
	private ConnectivityManager connManager;

	public Network(Context ctx) {
		this.context = ctx;
	}

	public boolean getConnectivityStatus() {
		connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null)
			return info.isConnected(); // WIFI connected
		else
			return false; // no info object implies no connectivity
	}
	
	public static String convertInputStreamToString(InputStream is) throws IOException
	{
		//Read into a bufferedreader
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		StringBuilder sb = new StringBuilder();
		
		String line;
		while ((line = br.readLine()) != null)
		{
			sb.append(line);
			
		}
		
		br.close();
		
		return sb.toString();
	}
}