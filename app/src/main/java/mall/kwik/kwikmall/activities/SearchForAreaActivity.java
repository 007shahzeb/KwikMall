package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.adapters.PlaceAutocompleteAdapter;
import mall.kwik.kwikmall.R;

public class SearchForAreaActivity extends AppCompatActivity implements
        PlaceAutocompleteAdapter.PlaceAutoCompleteInterface,
        GoogleApiClient.ConnectionCallbacks,View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout linearcurrentlocation;
    private ImageView imageBackArrowLocation;
    private AutoCompleteTextView searchAutoComplete;

    private RelativeLayout crossSearchText;
    private Button btnCross;
    private ProgressBar setDeliveryProgressBar;
    private RecyclerView searchRecyclerview;
    PlaceAutocompleteAdapter mAdapter;
    LinearLayoutManager llm;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));
    Context mContext;
    GoogleApiClient mGoogleApiClient;
    private Context context;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurants);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();


        initViews();

        searchRecyclerview.setVisibility(View.GONE);

        crossSearchText.setOnClickListener(this);


        Typeface hindVadodaraBold = Typeface.createFromAsset(getAssets(), "fonts/HindVadodara-Bold.ttf");
        searchAutoComplete.setTypeface(hindVadodaraBold);


        imageBackArrowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);

            }
        });


        linearcurrentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchForAreaActivity.this,SetDeliveryLocationActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private void initViews() {


        linearcurrentlocation = findViewById(R.id.linearcurrentlocation);
        imageBackArrowLocation = findViewById(R.id.imageBackArrowLocation);
        searchAutoComplete = findViewById(R.id.searchAutoComplete);


        //Button
        crossSearchText = findViewById(R.id.crossSearchText);
        btnCross = findViewById(R.id.btnCross);

        //progress bar
        setDeliveryProgressBar = findViewById(R.id.setDeliveryProgressBar);


        //Recyclerview
        searchRecyclerview = findViewById(R.id.searchRecyclerview);
        searchRecyclerview.setHasFixedSize(true);

        llm = new LinearLayoutManager(mContext);
        searchRecyclerview.setLayoutManager(llm);



        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.view_placesearch,
                mGoogleApiClient, BOUNDS_INDIA, null);

        searchRecyclerview.setAdapter(mAdapter);



        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                setDeliveryProgressBar.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        setDeliveryProgressBar.setVisibility(View.GONE);

                    }
                }, 1000);


                if (count > 0) {
                    btnCross.setVisibility(View.VISIBLE);
                    linearcurrentlocation.setVisibility(View.GONE);
                    searchRecyclerview.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        searchRecyclerview.setAdapter(mAdapter);
                    }
                } else {
                    searchRecyclerview.setVisibility(View.GONE);

                    btnCross.setVisibility(View.GONE);
//                    if (mSavedAdapter != null && mSavedAddressList.size() > 0) {
//                        searchRecyclerview.setAdapter(mSavedAdapter);
//                    }
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
                    Log.e("", "NOT CONNECTED");
                }





            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });




    }


    @Override
    public void onClick(View v) {

        if(v==crossSearchText){

            searchAutoComplete.setText("");
            searchRecyclerview.setVisibility(View.GONE);
            linearcurrentlocation.setVisibility(View.VISIBLE);

            if(mAdapter!=null){
                mAdapter.clearList();
            }


        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {


        if(mResultList!=null){
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);


                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if(places.getCount()==1){

                            Intent intent = new Intent(SearchForAreaActivity.this,SetDeliveryLocationActivity.class);
                            Bundle params = new Bundle();

                            params.putDouble("lat",places.get(0).getLatLng().latitude);
                            params.putDouble("lng",places.get(0).getLatLng().longitude);
                            intent.putExtras(params);
                            startActivity(intent);

                            overridePendingTransition(R.anim.slide_up, R.anim.stay);

                        }else {
                            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (Exception e){

            }

        }



    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
