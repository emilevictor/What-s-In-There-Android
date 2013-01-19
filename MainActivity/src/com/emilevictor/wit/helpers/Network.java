package com.emilevictor.wit.helpers;

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
}