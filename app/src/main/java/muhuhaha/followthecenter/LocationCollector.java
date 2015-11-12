package muhuhaha.followthecenter;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by muhuhaha on 2015-11-07.
 */
public class LocationCollector extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
	private static final String TAG = "LifeLog_LocationCollector";
	private boolean isRunning = true;
	protected GoogleApiClient mGoogleApiClient;
	private static final long INTERVAL = 1000 * 60 * 5; // 5 minutes
	private static final long FASTEST_INTERVAL = 1000 * 1; // 1 sec
	private static final long DISPLACEMENT = 10;
	LocationRequest mLocationRequest;

	public LocationCollector() {
		super("LocationCollector");
		Log.d(TAG, "[LocationCollector] constructed!");
	}

	/**
	 * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
	 */
	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "[onHandleIntent] received!");

		String command = intent.getStringExtra("command");

		if (command.equals("construct")) {
			createLocationRequest();
			buildGoogleApiClient();
		}

		if (command.equals("onstart")) {
			mGoogleApiClient.connect();
		}

		if (command.equals("onstop")) {
			if (mGoogleApiClient.isConnected()) {
				mGoogleApiClient.disconnect();
			}
		}
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	public void onDestroy() {
		isRunning = false;
	}

	@Override
	public void onConnected(Bundle bundle) {
		// Provides a simple way of getting a device's location and is well suited for
		// applications that do not require a fine-grained location and that do not need location
		// updates. Gets the best and most recent location currently available, which may be null
		// in rare cases when a location is not available.
		Log.d(TAG, "[onConnected] onConnected!");
		startLocationUpdates();
	}

	@Override
	public void onConnectionSuspended(int i) {
		// The connection to Google Play services was lost for some reason. We call connect() to
		// attempt to re-establish the connection.
		Log.i(TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "[onLocationChanged] Location changed!");

		// location intent
		//setLocationOnMap(location);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// Refer to the javadoc for ConnectionResult to see what error codes might be returned in
		// onConnectionFailed.
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
	}

	protected void startLocationUpdates() {
		PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
		Log.d(TAG, "[startLocationUpdates] location update started");
	}
}
