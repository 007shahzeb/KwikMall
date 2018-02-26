package mall.kwik.kwikmall.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;
import mall.kwik.kwikmall.sharedpreferences.MarkerDataPreference;
import mall.kwik.kwikmall.R;

public class TrackOrderMapActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {

    private TextView txtTotalTime,tvTotaltime,txtTotalDistance,tvTotaldistance;
    private GoogleMap mMap;
    private FrameLayout mapLayout;


    private ArrayList<LatLng> MarkerPoints = new ArrayList<>();
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private MarkerOptions options = new MarkerOptions();
    String distance = "";
    String duration = "";
    double dis;
    RelativeLayout footerSuccess;
    private Button btnGotoFragmentAct;
    private Context context;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_track_order_map);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        findViewId();


        clickListeners();

        // Initializing
        MarkerPoints = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapLayout);
        mapFragment.getMapAsync(this);



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }

    private void findViewId() {


        //Text Views
        txtTotalTime = findViewById(R.id.txtTotalTime);
        tvTotaltime = findViewById(R.id.tvTotaltime);
        txtTotalDistance = findViewById(R.id.txtTotalDistance);
        tvTotaldistance = findViewById(R.id.tvTotaldistance);

        footerSuccess = findViewById(R.id.footerSuccess);

        //Buttons
        btnGotoFragmentAct = findViewById(R.id.btnGotoFragmentAct);

    }



    private void clickListeners() {

        btnGotoFragmentAct.setOnClickListener(this);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        MarkerDataPreference markerData = new MarkerDataPreference(this);


        double latshop, longishop;
        double latUserHomeLat, longiUserHomeLong;
        latshop = Double.parseDouble(markerData.getShopAddLat());
        longishop = Double.parseDouble(markerData.getShopAddLong());


        if(sharedPrefsHelper.get(AppConstants.DEL_LAT,0)!=null && sharedPrefsHelper.get(AppConstants.DEL_LONG,0)!=null) {

            latUserHomeLat = sharedPrefsHelper.get(AppConstants.DEL_LAT,0);
            longiUserHomeLong = sharedPrefsHelper.get(AppConstants.DEL_LONG,0);

            Location startPoint = new Location("locationA");
            startPoint.setLatitude(latshop);
            startPoint.setLongitude(longishop);

            Location endPoint = new Location("locationA");
            endPoint.setLatitude(latUserHomeLat);
            endPoint.setLongitude(longiUserHomeLong);

            double distance = startPoint.distanceTo(endPoint);

            double km = distance / 1000;

            String string_temp = new Double(km).toString();
            String string_form = string_temp.substring(0, string_temp.indexOf('.'));
            dis = Double.valueOf(string_form);


            //For example spead is 10 meters per minute.
            int speedIs10MetersPerMinute = 80;
            double estimatedDriveTimeInMinutes = km / speedIs10MetersPerMinute;

            String string_temp1 = new Double(estimatedDriveTimeInMinutes).toString();
            String string_form1 = string_temp1.substring(0, string_temp1.indexOf('.'));
            double t1 = Double.valueOf(string_form1);

            tvTotaltime.setText(String.valueOf(t1));


            MarkerPoints.add(new LatLng(latshop, longishop)); //some latitude and logitude value
            //MarkerPoints.add(new LatLng(30.704649, 76.717873)); //some latitude and logitude value
            MarkerPoints.add(new LatLng(latUserHomeLat, longiUserHomeLong)); //some latitude and logitude value

            for (LatLng point : MarkerPoints) {
                options.position(point);
                options.title("someTitle");
                options.snippet("someDesc");
                googleMap.addMarker(options);
            }

            // Checks, whether start and end locations are captured
            if (MarkerPoints.size() >= 2) {
                LatLng origin = MarkerPoints.get(0);
                LatLng dest = MarkerPoints.get(1);

                // Getting URL to the Google Directions API
                String url = getUrl(origin, dest);
                FetchUrl FetchUrl = new FetchUrl();

                // Start downloading json data from Google Directions API
                FetchUrl.execute(url);
                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

            }

        }
        else {


            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("No orders to track");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();


            footerSuccess.setVisibility(View.GONE);

        }




}


    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {

        if(v==btnGotoFragmentAct){

            MaterialRippleLayout.on(btnGotoFragmentAct)
                    .rippleColor(Color.parseColor("#006400"))
                    .rippleAlpha(0.5f)
                    .rippleHover(true)
                    .create();

            startActivity(new Intent(this,FragmentsActivity.class));
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }


    }


    // Fetches data from url passed
private class FetchUrl extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... url) {

        // For storing data from web service
        String data = "";

        try {
            // Fetching the data from web service
            data = downloadUrl(url[0]);
            Log.d("Background Task data", data.toString());
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask();

        // Invokes the thread for parsing the JSON data
        parserTask.execute(result);

    }
}


/**
 * A class to parse the Google Places in JSON format
 */
private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("ParserTask", jsonData[0].toString());
            DataParser parser = new DataParser();
            Log.d("ParserTask", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("ParserTask", "Executing routes");
            Log.d("ParserTask", routes.toString());

        } catch (Exception e) {
            Log.d("ParserTask", e.toString());
            e.printStackTrace();
        }
        return routes;
    }



    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                    /*points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();*/


                if(j==0){    // Get distance from the list
                    distance = point.get("distance");

                    continue;
                }else if(j==1){ // Get duration from the list
                    duration = point.get("duration");
                    continue;
                }


                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.parseColor("#FFA500"));

            Log.d("onPostExecute","onPostExecute lineoptions decoded");

        }

        // Drawing polyline in the Google Map for the i-th route
        if(lineOptions != null) {
            mMap.addPolyline(lineOptions);
            tvTotaldistance.setText(String.valueOf(dis)+" km"); //distance in meters

        }
        else {
            Log.d("onPostExecute","without Polylines drawn");
        }
    }
}





    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
