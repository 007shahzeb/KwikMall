package mall.kwik.kwikmall.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.BaseFragActivity.BaseFragment;
import mall.kwik.kwikmall.activities.EnterAddressActivity;
import mall.kwik.kwikmall.activities.SignInActivity;
import mall.kwik.kwikmall.adapters.RecyclerViewAdapterTouch;
import mall.kwik.kwikmall.apiresponse.PlaceOrderResponse.PlaceOrder;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.CartModel;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;
import mall.kwik.kwikmall.sharedpreferences.UtilityCartData;
import mall.kwik.kwikmall.swipedItemtouchhelper.RecyclerItemTouchHelper;


import static mall.kwik.kwikmall.activities.FragmentsActivity.nearby;
import static mall.kwik.kwikmall.fragments.FragmentWithoutSearchResults.RecyclerViewAdapter._counter;

/**
 * Created by dharamveer on 2/1/18.
 */

public class ViewCartFragment extends BaseFragment implements View.OnClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private View view;
    private TextView txtViewCart, txtItems, txtTotalItems, txtTotal, txtTotatPriceViewCart, txtPlaceorder, loginForOrder;
    private ImageView imageViewCartBack, imageCartEmpty;
    private RecyclerView recyclerViewCart;

    private RecyclerViewAdapterTouch recyclerViewAdapter;
    public List<CartModel> viewCartModels;
    DBHelper database;
    RelativeLayout main_layout;
    private String nameOfFood, price, totalItems, imageUri, totalPrice;
    private int sum;
    int finalsum;
    int deleted;
    public int size;
    private LinearLayout bottomPlaceOrder;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private double latituteR, longitudeR;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_cart, container, false);

        viewCartModels = new ArrayList<CartModel>();

        findViewId();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        clickListeners();

        database = new DBHelper(getActivity());


        final UtilityCartData utilityCartData = new UtilityCartData(getActivity());
        nameOfFood = utilityCartData.getNameOfFood();
        price = nameOfFood = utilityCartData.getPrice();
        totalItems = utilityCartData.getTotalItems();
        imageUri = utilityCartData.getImageUri();
        totalPrice = utilityCartData.getTotalPrice();


        viewCartModels = database.getStoreProducts();

        size = viewCartModels.size();

        for (int i = 0; i < size; i++) {


            sum += Integer.parseInt(viewCartModels.get(i).getKEY_price());

        }

        if (size > 0) {


            if (UserDataUtility.getLogin(getActivity())) {  //true


                txtPlaceorder.setVisibility(View.VISIBLE);
                loginForOrder.setVisibility(View.GONE);

            } else {                                      //false
                txtPlaceorder.setVisibility(View.GONE);
                loginForOrder.setVisibility(View.VISIBLE);

            }


        }


        txtTotatPriceViewCart.setText(String.valueOf(sum));


        txtTotalItems.setText(String.valueOf(size));

        if (!isConnected()) {

            com.chootdev.csnackbar.Snackbar.with(getActivity(), null)
                    .type(Type.ERROR)
                    .message("Internet Not Connected")
                    .duration(Duration.SHORT)
                    .show();
        }

        if (size == 0) {


            txtPlaceorder.setVisibility(View.GONE);

            recyclerViewCart.setVisibility(View.INVISIBLE);
            imageCartEmpty.setVisibility(View.VISIBLE);

        } else {


            recyclerViewAdapter = new RecyclerViewAdapterTouch(viewCartModels, getActivity());

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewCart.setLayoutManager(mLayoutManager);
            recyclerViewCart.setItemAnimator(new DefaultItemAnimator());
            recyclerViewCart.setAdapter(recyclerViewAdapter);


            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewCart);


        }


        return view;


    }


    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
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

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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


    private void clickListeners() {

        imageViewCartBack.setOnClickListener(this);
        txtPlaceorder.setOnClickListener(this);
        loginForOrder.setOnClickListener(this);

    }

    private void findViewId() {


        //Relative Layout
        main_layout = view.findViewById(R.id.main_layout);


        txtViewCart = view.findViewById(R.id.txtViewCart);
        loginForOrder = view.findViewById(R.id.loginForOrder);

        txtItems = view.findViewById(R.id.txtItems);
        txtTotalItems = view.findViewById(R.id.txtTotalItems);
        txtTotal = view.findViewById(R.id.txtTotal);
        txtTotatPriceViewCart = view.findViewById(R.id.txtTotatPriceViewCart);

        imageViewCartBack = view.findViewById(R.id.imageViewCartBack);
        imageCartEmpty = view.findViewById(R.id.imageCartEmpty);
        txtPlaceorder = view.findViewById(R.id.txtPlaceorder);

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);


        //Linear layout
        bottomPlaceOrder = view.findViewById(R.id.bottomPlaceOrder);


    }


    @Override
    public void onClick(View v) {


        if (v == imageViewCartBack) {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                fm.popBackStack();
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");
                super.getActivity().onBackPressed();
            }

            getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


        }


        if (v == txtPlaceorder) {

            if (isConnected()) {
                placeOrderApi();


            } else {

                com.chootdev.csnackbar.Snackbar.with(getActivity(), null)
                        .type(Type.ERROR)
                        .message("Internet Not Connected")
                        .duration(Duration.SHORT)
                        .show();
            }

        }

        if (v == loginForOrder) {


            Intent intent = new Intent(getActivity(), SignInActivity.class);
            startActivity(intent);
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);


        }


    }


    public void placeOrderApi() {


        JsonArray jsonArray = new JsonArray();


        for(int i=0;i<viewCartModels.size();i++){

            JsonObject object = new JsonObject();

            object.addProperty("userId",sharedPrefsHelper.get(AppConstants.USER_ID,1));
            object.addProperty("productId",viewCartModels.get(i).getKEY_ID());
            object.addProperty("productName",viewCartModels.get(i).getKEY_name());
            object.addProperty("productPrice",viewCartModels.get(i).getKEY_price());
            object.addProperty("productQty",viewCartModels.get(i).getKEY_qty());
            object.addProperty("bussinessId",sharedPrefsHelper.get(AppConstants.STORE_ID,0));
            object.addProperty("total",viewCartModels.get(i).getKEY_TotalPrice());
            object.addProperty("userLat","30.975465");
            object.addProperty("userLng","76.524882");

            jsonArray.add(object);



        }


        compositeDisposable.add(apiService.placeOrder(jsonArray)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PlaceOrder>() {
                    @Override
                    public void accept(PlaceOrder placeOrder) throws Exception {


                        if(placeOrder.getSuccess()){


                            showSuccessDialog("Order Registered");
                            Bundle bundle = new Bundle();

                            bundle.putString("totalAmount", String.valueOf(sum));

                            Intent intent = new Intent(getActivity(),EnterAddressActivity.class);
                            intent.putExtras(bundle);

                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);

                        }
                        else {

                            showAlertDialog("Retry",placeOrder.getMessage());

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        noInternetDialog.show();
                    }
                }));


    }






    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        latituteR = location.getLatitude();
        longitudeR = location.getLongitude();

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {


        if(viewHolder instanceof RecyclerViewAdapterTouch.ViewCartHolder){


            // get the removed item name to display it in snack bar
            String name = viewCartModels.get(viewHolder.getAdapterPosition()).getKEY_name();

            // backup of removed item for undo purpose
            final CartModel deletedItem = viewCartModels.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();


            database.deleteSingleRowTABLE1(name);


            if(viewCartModels.size()>=1) {

                deleted = Integer.parseInt(deletedItem.getKEY_price());

                sum = sum - deleted;

            }


            txtTotatPriceViewCart.setText(String.valueOf(sum));


            recyclerViewAdapter.removeItem(viewHolder.getAdapterPosition());


            txtTotalItems.setText(String.valueOf(viewCartModels.size()));


            recyclerViewAdapter.notifyDataSetChanged();


            //tvCounter.setText(String.valueOf(viewCartModels.size()));
            nearby.setBadgeCount(viewCartModels.size());

         /*   CounterModel counterModel = new CounterModel(this);

            counterModel.setCounter(viewCartModels.size());*/
            if(viewCartModels.size()==0){

                recyclerViewCart.setVisibility(View.INVISIBLE);
                imageCartEmpty.setVisibility(View.VISIBLE);
                //tvCounter.setVisibility(View.GONE);
                //nearby.setBa
                _counter = 0;
                bottomPlaceOrder.setVisibility(View.GONE);

            }

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(main_layout, name + " removed from cart!", Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    recyclerViewAdapter.restoreItem(deletedItem, deletedIndex);

                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();


        }



    }

}
