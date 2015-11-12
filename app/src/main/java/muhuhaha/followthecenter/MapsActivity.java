package muhuhaha.followthecenter;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationReceiver.Receiver {
    private static final String TAG = "LifeLog_Map";
    private GoogleMap mMap;
//    String mMarkerString;
    PolylineOptions polylineOptions;
//    private static int markerSequence;
	private LocationReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	    //show error dialog if GoolglePlayServices not available
	    if (!isGooglePlayServicesAvailable()) {
		    finish();
	    }

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

		mReceiver = new LocationReceiver(new Handler());
	    mReceiver.setReceiver(this);

	    final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, LocationCollector.class);
	    intent.putExtra("command", "construct");
	    startService(intent);

//        markerSequence = 0;
    }


    @Override
    protected void onStart() {
        super.onStart();

	    final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, LocationCollector.class);
	    intent.putExtra("command", "onstart");
	    //startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "[onMapReady] Map is ready");
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        polylineOptions = new PolylineOptions();
    }

    private void addLocationInfo(Location mCurrentLocation) {
//        MarkerOptions options = new MarkerOptions();
//        markerSequence++;

        // following four lines requires 'Google Maps Android API Utility Library'
        // https://developers.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info
/*
        IconGenerator iconFactory = new IconGenerator(this);
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mMarkerString)));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
*/
        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//        options.position(currentLatLng);
//        Marker mapMarker = mMap.addMarker(options);
//        mMarkerString = markerSequence + "";
//        mapMarker.setTitle(mMarkerString);
//        Log.d(TAG, "[addMarker] Marker added at " + mCurrentLocation.getLatitude() + "::" + mCurrentLocation.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
        Log.d(TAG, "[addMarker] Zoom done");

        polylineOptions.add(currentLatLng);
        Polyline polyline = mMap.addPolyline(polylineOptions
            .width(12)
            .color(Color.GREEN)
            .geodesic(true));
    }

    private void setLocationOnMap(Location location) {
        if (location == null) {
            Log.d(TAG, "[setLocationOnMap] location is null!");
            Toast.makeText(this, "No Location!", Toast.LENGTH_LONG).show();
        }
        else {
            Log.d(TAG, "[setLocationOnMap] let's move to center");
            addLocationInfo(location);
        }
    }

    private int calculateArea(PolylineOptions polylineOptions) {
        int zoomNumber = 5;

        // 모든 점과 line이 표시되려면...
        // marker 크기도 변경이 가능할까?

        return zoomNumber;
    }

	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
			//case
		}
	}
}
