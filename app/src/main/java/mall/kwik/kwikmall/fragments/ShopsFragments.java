package mall.kwik.kwikmall.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.supercharge.shimmerlayout.ShimmerLayout;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.baseFragActivity.BaseFragment;
import mall.kwik.kwikmall.activities.FragmentsActivity;
import mall.kwik.kwikmall.activities.aplicationclass.AppController;
import mall.kwik.kwikmall.apiresponse.RestaurantsListSuccess.RestaurantsListPayload;
import mall.kwik.kwikmall.apiresponse.RestaurantsListSuccess.RestaurantsListSuccess;
import mall.kwik.kwikmall.R;

import mall.kwik.kwikmall.events.FilterEvent;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;




public class ShopsFragments extends BaseFragment{

    private View view;
    private RecyclerView recyclerViewShops;
    private RecyclerViewAdapterShops recyclerViewAdapter;
    private List<RestaurantsListPayload> shopsListPayloadArrayList = new ArrayList<>();
    static SharedPreferences sharedPreferencesshops;
    private String latitute,longitude;
    private ShimmerLayout shimmerlayoutShops;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private String ids;
    private FrameLayout afterShimmerLayout;
    private ImageView imageNoFoodProd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shops, container, false);


        findViewId();


        sharedPreferencesshops =   PreferenceManager.getDefaultSharedPreferences(getActivity());

        shopsListApi("");

       // getActivity().startService(new Intent(getActivity(), LocationService.class));

      /*  if(isConnected()){

            mMessageReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    latitute = intent.getStringExtra("lat");
                    longitude = intent.getStringExtra("longi");


                    ShopsListApi();

                }
            };

            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                    mMessageReceiver, new IntentFilter("intentKey"));






        }
        else {

            //Retrieve the values
            Gson gson = new Gson();
            String jsonText = sharedPreferencesshops.getString("keyshops", null);
            RestaurantsListPayload[] shopsListPayloadsnoNet = gson.fromJson(jsonText, RestaurantsListPayload[].class);  //EDIT: gso to gson

            recyclerViewAdapter = new RecyclerViewAdapterShops(Arrays.asList(shopsListPayloadsnoNet),getActivity());


            recyclerViewAdapter.setOnPhoneClickListener(new RecyclerViewAdapterShops.ShopsPhoneClickListener() {
                @Override
                public void onPhoneTextClick(String phone_no) {

                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:"+Uri.encode(phone_no.trim())));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    } catch (Exception e) {


                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("No Activity found to handle Intent.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();                            }

                }
            });

            recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapterShops.ClickListener() {
                @Override
                public void onItemClick(String string_form, String nameOfHotel, String address,int StoreId, View v) {


                    if(isConnected()){



                    }
                    else {

                        Snackbar.with(getActivity(),null)
                                .type(Type.ERROR)
                                .message("Internet Not Connected")
                                .duration(Duration.SHORT)
                                .show();

                    }

                }

                @Override
                public void onItemLongClick(String nameOfHotel, String address, int StoreId, View v) {

                }
            });


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

            recyclerViewShops.setHasFixedSize(true);

            recyclerViewShops.setLayoutManager(mLayoutManager);
            recyclerViewShops.setItemAnimator(new DefaultItemAnimator());
            recyclerViewShops.setAdapter(recyclerViewAdapter);

            shimmerlayoutShops.setVisibility(View.GONE);
            recyclerViewShops.setVisibility(View.VISIBLE);


            Snackbar.with(getActivity(),null).type(Type.ERROR).message("Internet Not Connected").duration(Duration.SHORT).show();


        }*/


        ((AppController)  getActivity().getApplication()).bus().toObservable().subscribe(new Consumer<Object>()
        {
            @Override
            public void accept(Object o) throws Exception {

                if (o instanceof FilterEvent) {


                    ids = ((FilterEvent) o).getFilteredIds();

                    shopsApi(ids,latitute,longitude);



                }

            }
        });



        return view;
    }

    private void shopsApi(String ids, String latitute, String longitude) {

        compositeDisposable.add(apiService.restaurantsList(latitute,longitude,"Shop",ids)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RestaurantsListSuccess>() {
                    @Override
                    public void accept(RestaurantsListSuccess restaurantsListSuccess) throws Exception {


                        if(restaurantsListSuccess.getSuccess()){


                            shopsListPayloadArrayList = new ArrayList<>(restaurantsListSuccess.getPayload());

                            recyclerViewAdapter = new RecyclerViewAdapterShops(shopsListPayloadArrayList,getActivity());

                            recyclerViewAdapter.setOnPhoneClickListener(new RecyclerViewAdapterShops.ShopsPhoneClickListener() {
                                @Override
                                public void onPhoneTextClick(String phone_no) {

                                    try {
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                        callIntent.setData(Uri.parse("tel:"+Uri.encode(phone_no.trim())));
                                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(callIntent);
                                    } catch (Exception e) {


                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                        builder1.setMessage("No Activity found to handle Intent.");
                                        builder1.setCancelable(true);

                                        builder1.setPositiveButton(
                                                "Yes",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        builder1.setNegativeButton(
                                                "No",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();                            }

                                }
                            });


                            recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapterShops.ClickListener() {
                                @Override
                                public void onItemClick(String string_form, String nameOfHotel, String address,int StoreId, View v) {

                                    sharedPrefsHelper.put(AppConstants.NAME_HOTEL,nameOfHotel);
                                    sharedPrefsHelper.put(AppConstants.ADDRESS,address);
                                    sharedPrefsHelper.put(AppConstants.STRING_FORM,string_form);

                                    Bundle bundle = new Bundle();

                                    sharedPrefsHelper.put(AppConstants.STORE_ID,StoreId);

                                    bundle.putString("nameOfHotel",nameOfHotel);
                                    bundle.putString("address",address);
                                    bundle.putString("string_form",string_form);




                                    ((FragmentsActivity) getActivity()).replace2();


                                                /*    Fragment fragment = null;
                                                    fragment = new RestaurantsProductsFragment();


                                                    if(fragment!=null){

                                                        FragmentManager fragmentManager = getFragmentManager();
                                                        fragment.setArguments(bundle);
                                                        fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment, "restaurantsProductsFragment")
                                                                .addToBackStack(null).commit();
                                                        getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);

                                                    }*/


                                }

                                @Override
                                public void onItemLongClick(String nameOfHotel, String address, int StoreId, View v) {

                                }
                            });


                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

                            recyclerViewShops.setHasFixedSize(true);

                            recyclerViewShops.setLayoutManager(mLayoutManager);
                            recyclerViewShops.setItemAnimator(new DefaultItemAnimator());
                            recyclerViewShops.setAdapter(recyclerViewAdapter);


                            shimmerlayoutShops.setVisibility(View.GONE);
                            afterShimmerLayout.setVisibility(View.VISIBLE);



                        }
                        else {

                            //showAlertDialog("Retry","Success False");


                            imageNoFoodProd.setVisibility(View.VISIBLE);
                            recyclerViewShops.setVisibility(View.GONE);


                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        showAlertDialog("Retry",throwable.getMessage());
                    }
                }));

    }


    private void shopsListApi(String ids) {


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


                        shopsApi(ids,latitute,longitude);

                    }
                });





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


    private void findViewId() {

        recyclerViewShops = view.findViewById(R.id.recyclerViewShops);
        afterShimmerLayout = view.findViewById(R.id.afterShimmerLayout);
        imageNoFoodProd = view.findViewById(R.id.imageNoFoodProd);
        shimmerlayoutShops = view.findViewById(R.id.shimmerlayoutShops);

        shimmerlayoutShops.startShimmerAnimation();

    }


    private static class RecyclerViewAdapterShops extends RecyclerView.Adapter<RecyclerViewAdapterShops.MyHolder> {

        List<RestaurantsListPayload> shopsListPayloadList = Collections.emptyList();
        Context context;
        View view;
        private static ShopsPhoneClickListener phoneClickLisetener;


        private static RecyclerViewAdapterShops.ClickListener clickListener;

        public interface ShopsPhoneClickListener {
            void onPhoneTextClick( String phone_no);
        }

        public void setOnPhoneClickListener(ShopsPhoneClickListener phoneClickListener){

            RecyclerViewAdapterShops.phoneClickLisetener = phoneClickListener;
        }

        public interface ClickListener {
            void onItemClick(String string_form, String nameOfHotel, String address,int StoreId, View v);
            void onItemLongClick( String nameOfHotel, String address,int StoreId, View v);
        }

        public void setOnItemClickListener(RecyclerViewAdapterShops.ClickListener clickListener) {
            RecyclerViewAdapterShops.clickListener = clickListener;
        }

        public RecyclerViewAdapterShops(List<RestaurantsListPayload> shopsListPayloadList, Context context) {
            this.shopsListPayloadList = shopsListPayloadList;
            this.context = context;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shops_fragment, parent, false);

            return new RecyclerViewAdapterShops.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, int position) {

            final RestaurantsListPayload shopsListPayload = shopsListPayloadList.get(position);

            if(shopsListPayload.getCatType().equalsIgnoreCase("Non-veg")){
                holder.imageVedNonVeg.setBackgroundResource(R.drawable.nonveg);
            }
            else {
                holder.imageVedNonVeg.setBackgroundResource(R.drawable.veg);

            }



            double lat = Double.parseDouble(shopsListPayload.getLattitude());
            double longi = Double.parseDouble(shopsListPayload.getLongitude());

            Location location1 = new Location("");
            //currentlocation lat long

            location1.setLatitude(lat);
            location1.setLongitude(longi);

            Location location2 = new Location("");
            location2.setLatitude(30.9755037);
            location2.setLongitude(76.5248561);

            float distanceInMeters = location1.distanceTo(location2);

            //For example spead is 10 meters per minute.
            int speedIs10MetersPerMinute = 80;
            float estimatedDriveTimeInMinutes =distanceInMeters / speedIs10MetersPerMinute;

            String string_temp = new Double(estimatedDriveTimeInMinutes).toString();
            String string_form = string_temp.substring(0,string_temp.indexOf('.'));


            holder.tvDistanceShop.setText(string_form);

            holder.tvShopName.setText(shopsListPayload.getName());
            holder.tvAddressShop.setText(shopsListPayload.getAddress());
            holder.tvPhoneNoShop.setText(shopsListPayload.getPhoneNo());





            final String phone_no= holder.tvPhoneNoShop.getText().toString().replaceAll("-", "");


            holder.tvPhoneNoShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneClickLisetener.onPhoneTextClick(phone_no);
                }
            });

            Picasso.with(view.getContext())
                    .load("http://employeelive.com/kwiqmall/SuperAdmin/img/stores/" + shopsListPayload.getImage())
                    .error(R.drawable.errortriangle)
                    .into(holder.imageViewShops);

            holder.relativeLayoutShopsRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickListener.onItemClick(holder.tvDistanceShop.getText().toString(),shopsListPayload.getName(),shopsListPayload.getAddress(),shopsListPayload.getId(), v);




                }
            });

        }

        @Override
        public int getItemCount() {
            return shopsListPayloadList.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            private TextView tvShopName,tvAddressShop,tvDiscountShop,tvRatingShop,tvDistanceShop,tvPhoneNoShop;
            private ImageView imageViewShops;
            private RelativeLayout relativeLayoutShopsRow;
            private ImageView imageVedNonVeg;


            public MyHolder(View itemView) {
                super(itemView);


                //Relative layout
                relativeLayoutShopsRow = itemView.findViewById(R.id.relativeLayoutShopsRow);


                tvShopName = itemView.findViewById(R.id.tvShopName);
                tvAddressShop = itemView.findViewById(R.id.tvAddressShop);
               // tvDiscountShop = itemView.findViewById(R.id.tvDiscountShop);
                //tvRatingShop = itemView.findViewById(R.id.tvRatingShop);
                tvDistanceShop = itemView.findViewById(R.id.tvDistanceShop);
                tvPhoneNoShop = itemView.findViewById(R.id.tvPhoneNoShop);


                imageViewShops = itemView.findViewById(R.id.imageViewShops);
                imageVedNonVeg = itemView.findViewById(R.id.imageVedNonVeg);




            }



        }
    }
}
