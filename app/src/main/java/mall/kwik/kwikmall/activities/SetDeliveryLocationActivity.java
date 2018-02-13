package mall.kwik.kwikmall.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arsy.maps_library.MapRadar;
import com.arsy.maps_library.MapRipple;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;

public class SetDeliveryLocationActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener {


    private GoogleMap mMap;
    private FrameLayout mapLayoutAddress;
    private ImageView imageBackArrowEnter;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private EditText edLocationCurrent;
    private GoogleApiClient mGoogleApiClient;
    private double latitude, longitude;
    private DBHelper dbHelper;
    private TextView txtEnterHouseNoSubmit;
    private LinearLayout homeLinear, workLinear, otherLinear, linearAll;
    private TextView tvHome, tvWork, tvOther;
    private ImageView imageHome, imageWork, imageOther, markerImage;
    private View lineHome, lineWork, lineOther;
    private Typeface hindVadodaraBold, hindVadodaraLight, hindVadodaraMedium;
    private RelativeLayout relativeOther;
    private EditText edOtherText;
    private TextView txt_cancel;
    private TextInputLayout input_layout_flat, input_layout_landmark, input_layout_current;
    private EditText edFlatNo, edLandmark;
    private String homeworkother;
    private Circle mCircle;
    private Marker mMarker;
    private ProgressBar progressBar1;
    MapRipple mapRipple;
    private MapRadar mapRadar;
    LatLng latLng;


    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;
    private Context context;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_address);
        ButterKnife.bind(this);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();


        findViewByIdM();


        imageBackArrowEnter.setOnClickListener(this);

        //Linear click
        homeLinear.setOnClickListener(this);
        workLinear.setOnClickListener(this);
        otherLinear.setOnClickListener(this);
        txt_cancel.setOnClickListener(this);
        txtEnterHouseNoSubmit.setOnClickListener(this);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        edFlatNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0) {

                    txtEnterHouseNoSubmit.setText("ENTER LANDMARK");
                } else {

                    txtEnterHouseNoSubmit.setText("ENTER HOUSE / FLAT NO.");
                    txtEnterHouseNoSubmit.setBackgroundColor(Color.parseColor("#FFDB99")); //dim dim orange


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edLandmark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.toString().length() > 0) {

                    txtEnterHouseNoSubmit.setText("SAVE AND PROCEED");
                    txtEnterHouseNoSubmit.setBackgroundColor(Color.parseColor("#FF6347")); //tomato dark orange
                } else {

                    txtEnterHouseNoSubmit.setText("ENTER LANDMARK");
                    txtEnterHouseNoSubmit.setBackgroundColor(Color.parseColor("#FFDB99")); //dim dim orange


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dbHelper = new DBHelper(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapLayoutAddress);
        mapFragment.getMapAsync(this);


        //Initializing googleapi client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    private void findViewByIdM() {


        imageBackArrowEnter = findViewById(R.id.imageBackArrowEnter);
        markerImage = findViewById(R.id.markerImage);

        //TextInputLayout
        input_layout_flat = findViewById(R.id.input_layout_flat);
        input_layout_landmark = findViewById(R.id.input_layout_landmark);
        input_layout_current = findViewById(R.id.input_layout_current);

        //Edit text
        edLocationCurrent = findViewById(R.id.edLocationCurrent);
        edFlatNo = findViewById(R.id.edFlatNo);
        edLandmark = findViewById(R.id.edLandmark);


        //Text views
        tvHome = findViewById(R.id.tvHome);
        tvWork = findViewById(R.id.tvWork);
        tvOther = findViewById(R.id.tvOther);
        txt_cancel = findViewById(R.id.txt_cancel);

        //Image view
        imageHome = findViewById(R.id.imageHome);
        imageWork = findViewById(R.id.imageWork);
        imageOther = findViewById(R.id.imageOther);

        //View line
        lineHome = findViewById(R.id.lineHome);
        lineWork = findViewById(R.id.lineWork);
        lineOther = findViewById(R.id.lineOther);


        //Edit text
        edOtherText = findViewById(R.id.edOtherText);


        //Text view
        txtEnterHouseNoSubmit = findViewById(R.id.tvSubmitFlat);

        //Linear layouts
        homeLinear = findViewById(R.id.homeLinear);
        workLinear = findViewById(R.id.workLinear);
        otherLinear = findViewById(R.id.otherLinear);
        linearAll = findViewById(R.id.linearAll);


        //Relative layout
        relativeOther = findViewById(R.id.relativeOther);

        //Progress bar
        progressBar1 = findViewById(R.id.progressBar1);


    }


    @Override
    public void onClick(View v) {

        if (v == imageBackArrowEnter) {
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }


        if (v == homeLinear) {


            imageHome.setBackgroundResource(R.drawable.homebtnblack);
            tvHome.setTypeface(hindVadodaraMedium);
            lineHome.setBackgroundColor(Color.parseColor("#fd923f")); // dim orange

            imageWork.setBackgroundResource(R.drawable.workwhite);
            tvWork.setTypeface(hindVadodaraLight);
            lineWork.setBackgroundColor(Color.parseColor("#cacbcc")); //light grey color

            imageOther.setBackgroundResource(R.drawable.workwhite);
            tvOther.setTypeface(hindVadodaraLight);
            lineOther.setBackgroundColor(Color.parseColor("#cacbcc")); // dim orange


            homeworkother = "Home";
        }

        if (v == workLinear) {

            imageWork.setBackgroundResource(R.drawable.workblack);
            tvWork.setTypeface(hindVadodaraMedium);
            lineWork.setBackgroundColor(Color.parseColor("#fd923f")); // dim orange

            imageHome.setBackgroundResource(R.drawable.home);
            tvHome.setTypeface(hindVadodaraLight);
            lineHome.setBackgroundColor(Color.parseColor("#cacbcc"));//light grey color

            imageOther.setBackgroundResource(R.drawable.workwhite);
            tvOther.setTypeface(hindVadodaraLight);
            lineOther.setBackgroundColor(Color.parseColor("#cacbcc")); // dim orange

            homeworkother = "Work";
        }

        if (v == otherLinear) {

            Animation RightSwipe = AnimationUtils.loadAnimation(SetDeliveryLocationActivity.this, R.anim.left_swipe);
            relativeOther.startAnimation(RightSwipe);

            relativeOther.setVisibility(View.VISIBLE);
            linearAll.setVisibility(View.GONE);

            imageOther.setBackgroundResource(R.drawable.workblack);
            tvOther.setTypeface(hindVadodaraMedium);
            lineOther.setBackgroundColor(Color.parseColor("#fd923f")); // dim orange

            imageHome.setBackgroundResource(R.drawable.home);
            tvHome.setTypeface(hindVadodaraLight);
            lineHome.setBackgroundColor(Color.parseColor("#cacbcc"));//light grey color

            homeworkother = edOtherText.getText().toString();

        }

        if (v == txt_cancel) {


            imageWork.setBackgroundResource(R.drawable.workwhite);
            tvWork.setTypeface(hindVadodaraLight);
            lineWork.setBackgroundColor(Color.parseColor("#cacbcc")); //light grey color


            Animation LeftSwipe = AnimationUtils.loadAnimation(SetDeliveryLocationActivity.this, R.anim.right_swipe);
            linearAll.startAnimation(LeftSwipe);

            relativeOther.setVisibility(View.GONE);
            linearAll.setVisibility(View.VISIBLE);

        }


        if (v == txtEnterHouseNoSubmit) {


            if (edFlatNo.getText().toString().length() == 0) {

                edFlatNo.setError("Required field");
            } else if (edLandmark.getText().toString().length() == 0) {

                edLandmark.setError("Required field");
            } else {


                String location = edLocationCurrent.getText().toString();
                String flatNo = edFlatNo.getText().toString();
                String landmark = edLandmark.getText().toString();

                if (homeworkother != null) {

                    dbHelper.addNewAddress(location, flatNo, landmark, homeworkother);

                } else {

                    dbHelper.addNewAddress(location, flatNo, landmark, homeworkother);

                }


                startActivity(new Intent(this, FragmentsActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        }


    }


    //Getting current location
    private void getCurrentLocation() {
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (location != null) {
            //Getting longitude and latitude

            longitude = location.getLongitude();
            latitude = location.getLatitude();


            //moving the map to location
            //moveMap();
        }
    }


    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        //String msg = latitude + ", "+longitude;
        Intent it = getIntent();

        if (it != null) {


            Bundle params = it.getExtras();
            if (params != null) {
                latitude = params.getDouble("lat");
                longitude = params.getDouble("lng");

            }
        }


        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);


        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.markerpin);


        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("Move pin to adjust")
                .snippet("Move pin to adjust")
                .icon(icon);
        mMap.addMarker(markerOptions);

        mMarker = mMap.addMarker(markerOptions);


        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 25), 2000, null);

        //Moving the camera

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                String locality = addressList.get(0).getAddressLine(0);
                String country = addressList.get(0).getCountryName();
                if (!locality.isEmpty() && !country.isEmpty())
                    edLocationCurrent.setText(locality + "  " + country);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;



        progressBar1.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                progressBar1.setVisibility(View.GONE);

            }
        }, 3000);



        latLng = new LatLng(latitude, longitude);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mMap.setMyLocationEnabled(true);




        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {


                LatLng midLatLong = mMap.getCameraPosition().target;

                Geocoder geocoder = new Geocoder(SetDeliveryLocationActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(midLatLong.latitude, midLatLong.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        if (!locality.isEmpty() && !country.isEmpty())
                            edLocationCurrent.setText(locality + "  " + country);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



        if (mMap != null) {
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            Location location = mMap.getMyLocation();
            if (location == null)
                getCurrentLocation();
            try {

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),10));


                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    public void run() {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 18));

                    }
                }, 1000);

            } catch (Exception e) {
                e.printStackTrace();
            }


            mapRipple = new MapRipple(mMap, latLng, SetDeliveryLocationActivity.this);

            mapRipple.withNumberOfRipples(3);
            mapRipple.withFillColor(Color.parseColor("#FFA3D2E4"));
            mapRipple.withStrokeColor(Color.BLACK);
            mapRipple.withStrokewidth(0);      // 10dp
            mapRipple.withDistance(2000);      // 2000 metres radius
            mapRipple.withRippleDuration(12000);    //12000ms
            mapRipple.withTransparency(0.5f);
            mapRipple.startRippleMapAnimation();


            mapRadar = new MapRadar(mMap, latLng, SetDeliveryLocationActivity.this);
            //mapRadar.withClockWiseAnticlockwise(true);
            mapRadar.withDistance(2000);
            mapRadar.withClockwiseAnticlockwiseDuration(2);
            //mapRadar.withOuterCircleFillColor(Color.parseColor("#12000000"));
            mapRadar.withOuterCircleStrokeColor(Color.parseColor("#fccd29"));
            //mapRadar.withRadarColors(Color.parseColor("#00000000"), Color.parseColor("#ff000000"));  //starts from transparent to fuly black
            mapRadar.withRadarColors(Color.parseColor("#00fccd29"), Color.parseColor("#fffccd29"));  //starts from transparent to fuly black
            //mapRadar.withOuterCircleStrokewidth(7);
            //mapRadar.withRadarSpeed(5);
            mapRadar.withOuterCircleTransparency(0.5f);
            mapRadar.withRadarTransparency(0.5f);





        }
    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        if (mapRipple.isAnimationRunning()) {
            mapRipple.stopRippleMapAnimation();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

        getCurrentLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



 /*   @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        mMap.clear();

        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;



        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                String locality = addressList.get(0).getAddressLine(0);
                String country = addressList.get(0).getCountryName();
                if (!locality.isEmpty() && !country.isEmpty())
                    edLocationCurrent.setText(locality + "  " + country);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Moving the map
        moveMap();
    }

*/

    @Override
    public void onMapClick(LatLng latLng) {


        mMap.clear();

        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));


        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                String locality = addressList.get(0).getAddressLine(0);
                String country = addressList.get(0).getCountryName();
                if (!locality.isEmpty() && !country.isEmpty())
                    edLocationCurrent.setText(locality + "  " + country);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
