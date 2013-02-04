package com.emilevictor.wit.computeravailability;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emilevictor.wit.MainActivity;
import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.Settings;

public class ComputerAvailabilityOverview extends Activity {

	private ProgressBar progressBar;
	private TextView progressText;

	private Handler progressBarHandler = new Handler();
	private int progressStatus = 0;

	private Runnable animationRunnable = new Runnable() {
		public void run() {
			if (progressBar.getProgress() < progressStatus) {
				progressBar.incrementProgressBy(1);
				progressBar.invalidate();
				progressBarHandler.postDelayed(animationRunnable, 10); //run again after 10 ms

			}
		}};

		@Override
		public boolean onOptionsItemSelected(MenuItem menuItem)
		{       
			startActivity(new Intent(ComputerAvailabilityOverview.this,MainActivity.class)); 
			return true;
		}


		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_computer_availability_overview);

			final Intent intent = new Intent(this, ComputerAvailabilityOverviewResults.class);


			//Add a back button to the action bar
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					progressBar = (ProgressBar) findViewById(R.id.progressBarCompOverview);
					progressText = (TextView) findViewById(R.id.progressTextCompOverview);

					List<Room> eaitRooms = null;
					GetEAITAvailabilityTask eaitTask = new GetEAITAvailabilityTask();

					ignoreExpiredCertificatesHack();
					
					try {
						eaitRooms = eaitTask.execute(Settings.eaitAvailabilityXMLurl).get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					progressStatus = 100;
					progressBarHandler.post(new Runnable() {
						@Override
						public void run() {
							// This gets executed on the UI thread so it can safely modify Views

							progressText.setText("Fetching EAIT computers");
						}
					});
					progressBarHandler.postDelayed(animationRunnable,10);


					//Current room
					int thisRoomIndex = 0;
					//Set the number of rooms
					for (Room room : eaitRooms)
					{
						intent.putExtra("ROOM"+String.valueOf(thisRoomIndex),room);
						thisRoomIndex++;
					}

					intent.putExtra("numberOfRooms", thisRoomIndex);

					startActivity(intent);
				}

				private void ignoreExpiredCertificatesHack() {
					/**
					 * The following HACK is intended to ignore the expired certificate
					 * currently residing in UQ's CA.
					 */
					
					TrustManagerFactory tmf = null;
					try {
						tmf = TrustManagerFactory.getInstance(
							    TrustManagerFactory.getDefaultAlgorithm());
					} catch (NoSuchAlgorithmException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
						// Initialise the TMF as you normally would, for example:
						try {
							tmf.init((KeyStore)null);
						} catch (KeyStoreException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						} 

						TrustManager[] trustManagers = tmf.getTrustManagers();
						final X509TrustManager origTrustmanager = (X509TrustManager)trustManagers[0];

						TrustManager[] wrappedTrustManagers = new TrustManager[]{
						   new X509TrustManager() {
						       public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						          return origTrustmanager.getAcceptedIssuers();
						       }

						       public void checkClientTrusted(X509Certificate[] certs, String authType) {
						           try {
									origTrustmanager.checkClientTrusted(certs, authType);
								} catch (CertificateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						       }

						       public void checkServerTrusted(X509Certificate[] certs, String authType) {
						           try {
									origTrustmanager.checkServerTrusted(certs, authType);
								} catch (CertificateException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						       }
						   }
						};

						SSLContext sc = null;
						try {
							sc = SSLContext.getInstance("TLS");
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							sc.init(null, wrappedTrustManagers, null);
						} catch (KeyManagementException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				}


			}).start();



		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(
					R.menu.activity_computer_availability_overview, menu);
			return true;
		}

}
