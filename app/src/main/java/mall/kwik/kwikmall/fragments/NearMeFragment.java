package mall.kwik.kwikmall.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.location.LocationRequest;


import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mall.kwik.kwikmall.activities.FilterActivity;
import mall.kwik.kwikmall.activities.SearchForAreaActivity;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.activities.aplicationclass.AppController;
import mall.kwik.kwikmall.events.FilterEvent;
import mall.kwik.kwikmall.services.LocationAddress;
import mall.kwik.kwikmall.sharedpreferences.UtilitySP;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

import static mall.kwik.kwikmall.activities.FragmentsActivity.bottomBar;


public class NearMeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private LinearLayout filterLayout, linearNearmeLocation;

    private TextView tvCityName, tvAddress;
    private String city;
    // private BroadcastReceiver mMessageReceiver;

    private double latituteR, longitudeR;
    public ImageView imageFilteOn;
    UtilitySP utilitySP;

    private TextView txtRestaurants, txtShops;
    private ViewPager frame_near_me;

    FragmentPagerAdapter adapterViewPager;

    private Disposable subscription;
    private long lastClickTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_near_me, container, false);

        findViewId();

        clikListeners();

        bottomBar.getTabWithId(R.id.tab_nearyby).performClick();
        //  getActivity().startService(new Intent(getActivity(), LocationService.class));

        utilitySP = new UtilitySP(getActivity());

        if (isConnected()) {

        } else {

            tvAddress.setText(utilitySP.getAddress());
            tvCityName.setText(utilitySP.getCityName());

        }

    /*    mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                latituteR = Double.parseDouble(intent.getStringExtra("lat"));
                longitudeR = Double.parseDouble(intent.getStringExtra("longi"));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LocationAddress locationAddress = new LocationAddress();
                        locationAddress.getAddressFromLocation(latituteR, longitudeR,
                                getActivity(), new GeocoderHandler());
                    }
                });

            }
        };*/

        // LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("intentKey"));

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        LocationRequest request = LocationRequest.create() //Standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(100);


        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getActivity());


        subscription = locationProvider.getUpdatedLocation(request)
                .subscribe(new Consumer<Location>() {
                    @Override
                    public void accept(Location location) throws Exception {


                        latituteR = location.getLatitude();
                        longitudeR = location.getLongitude();


                        Observable<List<Address>> reverseGeocodeObservable = locationProvider
                                .getReverseGeocodeObservable(location.getLatitude(), location.getLongitude(), 5);

                        reverseGeocodeObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<Address>>() {
                                    @Override
                                    public void accept(List<Address> addresses) throws Exception {


                                        String addres = addresses.get(0).getSubLocality();
                                        String cityName = addresses.get(0).getLocality();
                                        String stateName = addresses.get(0).getAdminArea();

                                        tvCityName.setText(cityName);


                                        tvAddress.setText(addres);


                                    }
                                });

                    }
                });


        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        frame_near_me.setAdapter(adapterViewPager);

        frame_near_me.setCurrentItem(0);

        frame_near_me.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                frame_near_me.setCurrentItem(position);

                if (position == 0) {

                    txtRestaurants.setBackgroundResource(R.drawable.layout_bg_white);
                    txtShops.setBackgroundResource(R.drawable.layout_bg_green);

                    txtRestaurants.setTextColor(Color.parseColor("#008000")); //green
                    txtShops.setTextColor(Color.parseColor("#ffffff"));  //white


                } else {

                    txtShops.setBackgroundResource(R.drawable.layout_bg_white);
                    txtRestaurants.setBackgroundResource(R.drawable.layout_bg_green);

                    txtShops.setTextColor(Color.parseColor("#008000"));  //green
                    txtRestaurants.setTextColor(Color.parseColor("#ffffff")); //white

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

// data passing from one activity to another without opening that particular activity where we want to send the data

        ((AppController) getActivity().getApplication()).bus().toObservable().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

                if (o instanceof FilterEvent) {

                    String ids = ((FilterEvent) o).getFilteredIds();

                    imageFilteOn.setVisibility(View.VISIBLE);

                    if (TextUtils.isEmpty(ids)) {


                    }

                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });


        return view;

    }


    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 2;


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment


                    return new RestaurantsFragment();

                case 1: // Fragment # 0 - This will show FirstFragment different title


                    return new ShopsFragments();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            System.out.println("MyPagerAdapter.getPageTitle  - - - Position is " + position);
            return "Page " + position;
        }

    }


    // Checking the connectivity of the Internet
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //  LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);


    }

    private void clikListeners() {

        txtRestaurants.setOnClickListener(this);
        txtShops.setOnClickListener(this);

        filterLayout.setOnClickListener(this);
        linearNearmeLocation.setOnClickListener(this);

    }

    private void findViewId() {


        tvCityName = view.findViewById(R.id.tvCityName);
        tvAddress = view.findViewById(R.id.tvAddress);

        //text views
        txtRestaurants = view.findViewById(R.id.txtRestaurants);
        txtShops = view.findViewById(R.id.txtShops);

        filterLayout = view.findViewById(R.id.filterLayout);
        linearNearmeLocation = view.findViewById(R.id.linearNearmeLocation);

        //Image view
        imageFilteOn = view.findViewById(R.id.imageFilteOn);

        frame_near_me = view.findViewById(R.id.viewPager);


        MaterialRippleLayout.on(filterLayout)
                .rippleColor(Color.parseColor("#006400"))
                .rippleAlpha(0.5f)
                .rippleHover(true)
                .create();

    }


    @Override
    public void onClick(View v) {


        if (v == filterLayout) {

            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            onButtonClick(filterLayout);

        }


        if (v == linearNearmeLocation) {

            Intent intent = new Intent(getActivity(), SearchForAreaActivity.class);
            startActivity(intent);

            getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);

        }


        if (v == txtRestaurants) {

            txtRestaurants.setBackgroundResource(R.drawable.layout_bg_white);
            txtShops.setBackgroundResource(R.drawable.layout_bg_green);

            txtRestaurants.setTextColor(Color.parseColor("#008000")); //green
            txtShops.setTextColor(Color.parseColor("#ffffff"));  //white


            frame_near_me.setCurrentItem(0);

        }
        if (v == txtShops) {


            txtShops.setBackgroundResource(R.drawable.layout_bg_white);
            txtRestaurants.setBackgroundResource(R.drawable.layout_bg_green);

            txtShops.setTextColor(Color.parseColor("#008000"));  //green
            txtRestaurants.setTextColor(Color.parseColor("#ffffff")); //white

            frame_near_me.setCurrentItem(1);


        }


    }


    // "Go to Second Activity" button click
    public void onButtonClick(View view) {

        // Start the SecondActivity
        startActivity(new Intent(getActivity(), FilterActivity.class));
        getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

    }


    private class GeocoderHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {

            String locationAddress;
            switch (msg.what) {

                case 1:
                    Bundle bundle = msg.getData();
                    locationAddress = bundle.getString("address");
                    city = bundle.getString("cityname");
                    break;
                default:
                    locationAddress = null;
            }


            //  utilitySP.setAddress(locationAddress);
            //  utilitySP.setCityName(city);

            tvAddress.setText(locationAddress);
            tvCityName.setText(city);
            //tvAddressLoadingText.setVisibility(View.GONE);


            Log.e("location Address=", locationAddress);

        }
    }


}
