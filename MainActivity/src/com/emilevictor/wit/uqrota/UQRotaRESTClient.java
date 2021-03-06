package com.emilevictor.wit.uqrota;

import com.emilevictor.wit.helpers.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UQRotaRESTClient {
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		client.get(getAbsoluteUrl(url),params,responseHandler);
	}
	
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler)
	{
		client.post(getAbsoluteUrl(url),params,responseHandler);
	}
	

	private static String getAbsoluteUrl(String relativeUrl) {
		return Settings.UQRotaBaseUrl + relativeUrl;
	}
}
