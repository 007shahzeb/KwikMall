package mall.kwik.kwikmall.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.supercharge.shimmerlayout.ShimmerLayout;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.BaseFragActivity.BaseFragment;
import mall.kwik.kwikmall.activities.FragmentsActivity;
import mall.kwik.kwikmall.activities.aplicationclass.AppController;
import mall.kwik.kwikmall.adapters.AdapterRestaurantsFragment;
import mall.kwik.kwikmall.apiresponse.RestaurantsListSuccess.RestaurantsListPayload;
import mall.kwik.kwikmall.apiresponse.RestaurantsListSuccess.RestaurantsListSuccess;
import mall.kwik.kwikmall.dialogs.DialogInternet;
import mall.kwik.kwikmall.events.FilterEvent;
import mall.kwik.kwikmall.models.DistanceModel;
import mall.kwik.kwikmall.receiver.NetworkChangeReceiverForNearMeFrag;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sharedpreferences.SharedPrefData;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;


public class RestaurantsFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private View view;
    private RecyclerView recyclerViewRestaurants;
    private AdapterRestaurantsFragment recyclerViewAdapter;
    private List<RestaurantsListPayload> restaurantsListPayloadArrayList = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 1;
    // private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private String latitute,longitude;
    private BroadcastReceiver mNetworkReceiver;
    private FrameLayout frame_near_me;
    private SharedPreferences mSharedPreference1;
    //  private BroadcastReceiver mMessageReceiver;
    private ShimmerLayout shimmerlayoutRestaurants;
    private FrameLayout afterShimmerLayout;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_restaurants, container, false);

        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);


       /* if(googleApiClient == null)
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();*/

        mNetworkReceiver = new NetworkChangeReceiverForNearMeFrag();
        registerNetworkBroadcastForNougat();


        findViewId();


        // getActivity().startService(new Intent(getActivity(), LocationService.class));

        mSharedPreference1 =   PreferenceManager.getDefaultSharedPreferences(getActivity());


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerViewRestaurants.setHasFixedSize(true);

        recyclerViewRestaurants.setLayoutManager(mLayoutManager);
        recyclerViewRestaurants.setItemAnimator(new DefaultItemAnimator());

        RestaurantsListApi();
/*

        if(isConnected()){


            mMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    latitute = intent.getStringExtra("lat");
                    longitude = intent.getStringExtra("longi");

                    RestaurantsListApi();

                }
            };

            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                    mMessageReceiver, new IntentFilter("intentKey"));


        }
        else {


            //Retrieve the values
            Gson gson = new Gson();
            String jsonText = mSharedPreference1.getString("key", null);
            RestaurantsListPayload[] restaurantsListPayloadsNoNet = gson.fromJson(jsonText, RestaurantsListPayload[].class);  //EDIT: gso to gson


            recyclerViewAdapter = new AdapterRestaurantsFragment(Arrays.asList(restaurantsListPayloadsNoNet), getActivity());


            recyclerViewRestaurants.setAdapter(recyclerViewAdapter);

            shimmerlayoutRestaurants.setVisibility(View.GONE);
            afterShimmerLayout.setVisibility(View.VISIBLE);


            DialogInternet dialogInternet = new DialogInternet(getActivity());

            dialogInternet.show();


        }*/



        return view;
    }




    public void dialogFrag(boolean value){


        if(value){


        }
        else {

            //Retrieve the values
            Gson gson = new Gson();
            String jsonText = mSharedPreference1.getString("key", null);
            RestaurantsListPayload[] restaurantsListPayloadsNoNet = gson.fromJson(jsonText, RestaurantsListPayload[].class);  //EDIT: gso to gson


            recyclerViewAdapter = new AdapterRestaurantsFragment(Arrays.asList(restaurantsListPayloadsNoNet), getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

            recyclerViewRestaurants.setHasFixedSize(true);

            recyclerViewRestaurants.setLayoutManager(mLayoutManager);
            recyclerViewRestaurants.setItemAnimator(new DefaultItemAnimator());

            recyclerViewRestaurants.setAdapter(recyclerViewAdapter);
            shimmerlayoutRestaurants.setVisibility(View.GONE);


        }

    }

    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }


    private void registerNetworkBroadcastForNougat() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

    }
    protected void unregisterNetworkChanges() {
        try {
            getActivity().unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterNetworkChanges();
        compositeDisposable.dispose();
        // LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);

    }

    private void RestaurantsListApi() {


        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        locationProvider.getLastKnownLocation()
                .subscribe(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {

                        latitute = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());


                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                        stringStringHashMap.put("lat", latitute);
                        stringStringHashMap.put("lng", longitude);
                        stringStringHashMap.put("type", "Restaurant");

                        stringStringHashMap.put("catId","");


                        ((AppController)  getActivity().getApplication()).bus().toObservable().subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {

                                if(o instanceof FilterEvent){
                                    String ids =     ((FilterEvent)o) .getFilteredIds();


                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });


                     /*   if(sharedPrefsHelper.get(AppConstants.FILTER_DATA,"")!=null){

                            stringStringHashMap.put("catId",sharedPrefsHelper.get(AppConstants.FILTER_DATA,""));
                            imageFilteOn.setVisibility(View.VISIBLE);

                        }
                        else {

                            stringStringHashMap.put("catId","");
                            imageFilteOn.setVisibility(View.GONE);


                        }*/

                        compositeDisposable.add(apiService.restaurantsList(stringStringHashMap)
                                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<RestaurantsListSuccess>() {
                                    @Override
                                    public void accept(RestaurantsListSuccess restaurantsListSuccess) throws Exception {

                                        if(restaurantsListSuccess.getSuccess()){

                                            restaurantsListPayloadArrayList = new ArrayList<>(restaurantsListSuccess.getPayload());

                                            SharedPreferences.Editor mEdit1 = mSharedPreference1.edit();


                                          /*  Gson gson = new Gson();
                                            List<RestaurantsListPayload> textList = new ArrayList<RestaurantsListPayload>();
                                            textList.addAll(restaurantsListPayloadArrayList);
                                            String jsonText = gson.toJson(textList);
                                            mEdit1.putString("key", jsonText);
                                            mEdit1.commit();
*/

                                            recyclerViewAdapter = new AdapterRestaurantsFragment(restaurantsListPayloadArrayList, getActivity());


                                            recyclerViewAdapter.setOnPhoneClickListener(new AdapterRestaurantsFragment.PhoneClickLisetener() {
                                                @Override
                                                public void onPhoneTextClick(String phone_no) {

                                                    try {
                                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                        callIntent.setData(Uri.parse("tel:"+Uri.encode(phone_no.trim())));
                                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(callIntent);
                                                    } catch (Exception e) {


                                                        showAlertDialog("Retry","No Activity found to handle Intent.");

                                                    }
                                                }
                                            });


                                            recyclerViewAdapter.setOnItemClickListener(new AdapterRestaurantsFragment.ClickListener() {
                                                @Override
                                                public void onItemClick(String string_form,String nameOfHotel, String address, int StoreId, View v) {


                                                    sharedPrefsHelper.put(AppConstants.STORE_ID,StoreId);

                                                    SharedPrefData sharedPrefData = new SharedPrefData(getActivity());


                                                    sharedPrefData.setNameOfHotel(nameOfHotel);
                                                    sharedPrefData.setAddress(address);
                                                    sharedPrefData.setString_form(string_form);

                                                    Bundle bundle = new Bundle();

                                                    bundle.putString("nameOfHotel",nameOfHotel);
                                                    bundle.putString("address",address);
                                                    bundle.putString("string_form",string_form);



                                                    ((FragmentsActivity) getActivity()).replace();


                                 /*   Fragment fragment = null;
                                    fragment = new RestaurantsProductsFragment();

                                    if(fragment!=null){



                                        FragmentManager fragmentManager = getChildFragmentManager();
                                        fragment.setArguments(bundle);
                                        fragmentManager.beginTransaction().replace(R.id.openProducts, fragment, "restaurantsProductsFragment")
                                                .addToBackStack(null).commit();
                                        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);

                                    }*/


                                                }

                                                @Override
                                                public void onItemLongClick(String nameOfHotel, String address, int StoreId, View v) {

                                                }



                                            });

                                            recyclerViewRestaurants.setAdapter(recyclerViewAdapter);

                                            shimmerlayoutRestaurants.setVisibility(View.GONE);
                                            afterShimmerLayout.setVisibility(View.VISIBLE);



                                        }
                                        else {

                                            showAlertDialog("Retry","Success False");

                                        }

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {


                                        showAlertDialog("Retry",throwable.getMessage());

                                    }
                                }));





                    }
                });








    }

    private void findDistanceHotelFromMe() {


        ArrayList<DistanceModel> distanceModelArrayList = new ArrayList<>();

        for (int i = 0; i < restaurantsListPayloadArrayList.size(); i++) {

            DistanceModel distanceModel = new DistanceModel();
            final Location startPoint = new Location("locationA");
            startPoint.setLatitude(Double.parseDouble(latitute));
            startPoint.setLongitude(Double.parseDouble(longitude));

            final Location endPoint = new Location("locationB");
            // endPoint.setLatitude(Double.parseDouble(restaurantsListPayloadArrayList.get(0).getLattitude()));
            endPoint.setLatitude(30.898727);
            // endPoint.setLongitude(Double.parseDouble(restaurantsListPayloadArrayList.get(0).getLongitude()));
            endPoint.setLongitude(76.549389);


            //Distance in meters
            float distance = startPoint.distanceTo(endPoint);

            distanceModel.setDistance(distance);

            distanceModelArrayList.add(distanceModel);

        }

    }


    private void findViewId() {

        recyclerViewRestaurants = view.findViewById(R.id.recyclerViewRestaurants);


        //Frame layout
        frame_near_me = view.findViewById(R.id.viewPager);
        afterShimmerLayout = view.findViewById(R.id.afterShimmerLayout);

        //shimmer layout
        shimmerlayoutRestaurants = view.findViewById(R.id.shimmerlayoutRestaurants);

        shimmerlayoutRestaurants.startShimmerAnimation();


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {


     /*   if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);


        }

        else {
            if(googleApiClient.isConnected()) {

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                if (mLastLocation != null) {
                    System.out.println("latiTude....." + String.valueOf(mLastLocation.getLatitude()));
                    System.out.println("longiTude....." + String.valueOf(mLastLocation.getLongitude()));


                   *//* latitute = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();*//*

                    findDistanceHotelFromMe();



                }

            }

        }*/



    }

    @Override
    public void onStart() {
        //googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        // googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();
        isCheckLocationServiceisOn(getActivity());
    }

    private void isCheckLocationServiceisOn(FragmentActivity activity) {



        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("Location Services Disabled. \n Please enable location services.");

            dialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getActivity().startActivity(myIntent);
                    //get gps
                }
            });

            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {


                }
            });
            //dialog.show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("onConnectionSuspended...."+ i);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("onConnectionFailed...."+ connectionResult.toString());

    }

}
