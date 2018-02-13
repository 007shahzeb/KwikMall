package mall.kwik.kwikmall.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.BaseFragActivity.BaseFragment;
import mall.kwik.kwikmall.activities.aplicationclass.AppController;
import mall.kwik.kwikmall.adapters.RecyclerViewAdapterSearch;
import mall.kwik.kwikmall.apiresponse.GetStoreProducts.StoreProductsPayload;
import mall.kwik.kwikmall.apiresponse.GetStoreProducts.StoreProductsSuccess;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.dialogs.DialogMenu;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.SharedPrefData;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;
import mall.kwik.kwikmall.sharedpreferences.UtilityCartData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mall.kwik.kwikmall.activities.FragmentsActivity.nearby;

//import static mall.kwik.kwikmall.Activities.FragmentsActivity.tvCounter;

/**
 * Created by dharamveer on 15/12/17.
 */


public class FragmentSearchResults extends BaseFragment implements View.OnClickListener{


    private View view;
    private ArrayList<StoreProductsPayload> storeProductsPayloadArrayList = new ArrayList<>();
    private RecyclerView recyclerViewSearch;
    private RecyclerViewAdapter recyclerViewAdapter;


    Bundle bundle;
    private TextView tvHotelNamebold,tvTypeLight,tvStarRatingsMedium,tvDeliveryTimeMedium,
            tvRatingsTextLight,tvRatingsnumberLight,txtDeliveryTimeLight,
            tvFoodTypeMedium,tvDiscountoffersMedium,tvRecommededBold;


    private RecyclerViewAdapterSearch recyclerViewAdapterSearch;
    private ImageView imageBackArrow,imageSearchProducts;
    private Button btnMenu;
    private ScaleAnimation scaleAnimation;
    String itemname,nameOfHotel,address,string_form;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        //Bold Text Views
        tvHotelNamebold = view.findViewById(R.id.tvHotelNamebold);
        tvRecommededBold = view.findViewById(R.id.tvRecommededBold);

        //Light TextViews
        tvTypeLight = view.findViewById(R.id.tvTypeLight);
        //tvRatingsTextLight = view.findViewById(R.id.tvRatingsTextLight);
        //tvRatingsnumberLight = view.findViewById(R.id.tvRatingsnumberLight);
        txtDeliveryTimeLight = view.findViewById(R.id.txtDeliveryTimeLight);


        //Medium Text Views
        //tvStarRatingsMedium = view.findViewById(R.id.tvStarRatingsMedium);
        tvDeliveryTimeMedium = view.findViewById(R.id.tvDeliveryTimeMedium);
        //tvFoodTypeMedium = view.findViewById(R.id.tvFoodTypeMedium);
        //tvDiscountoffersMedium = view.findViewById(R.id.tvDiscountoffersMedium);

        imageBackArrow = view.findViewById(R.id.imageBackArrow);
        imageSearchProducts = view.findViewById(R.id.imageSearchProducts);


        btnMenu = view.findViewById(R.id.btnMenu);


        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);

        SharedPrefData sharedPrefData = new SharedPrefData(getActivity());

        bundle = getArguments();


        if(bundle != null){

            itemname = bundle.getString("itemname");
            nameOfHotel=getArguments().getString("nameOfHotel");
            address=getArguments().getString("address");
            string_form=getArguments().getString("string_form");

        }

        nameOfHotel = sharedPrefData.getNameOfHotel();
        address = sharedPrefData.getAddress();
        string_form = sharedPrefData.getString_form();

        tvHotelNamebold.setText(nameOfHotel);
        tvTypeLight.setText(address);
        tvDeliveryTimeMedium.setText(string_form);


        if(bundle != null){

            itemname = bundle.getString("itemname");

        }

        clikListeners();


        GetStoreProductsApi();


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(mLayoutManager);
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());



        return view;

    }

    public  interface ClickListener {
        void onItemClick(int productid,int position,String nameOfFood, String price, View v,String imageUri,String description);
        void onItemLongClick(int position,String nameOfFoof, String price, View v,String imageUri);
    }




    private void clikListeners() {


        btnMenu.setOnClickListener(this);

        imageSearchProducts.setOnClickListener(this);

        imageBackArrow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        if(v==btnMenu){


            DialogMenu dialogMenu = new DialogMenu(getActivity());


            Window window = dialogMenu.getWindow();

            window.setGravity(Gravity.CENTER_HORIZONTAL);

            WindowManager.LayoutParams wmlp = dialogMenu.getWindow().getAttributes();

            wmlp.gravity = Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
            // wmlp.x = 350;   //x position
            wmlp.y = 200;   //y position


            dialogMenu.show();

        }


        if(v==imageSearchProducts){

            Fragment fragment = new SearchRestaurantsFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrame,fragment,fragment.getClass().getSimpleName()).addToBackStack(null).commit();


        }



        if(v==imageBackArrow){

          /*  if ( getFragmentManager().getBackStackEntryCount() > 0)
            {
                getFragmentManager().popBackStack();
                return;
            }
            getActivity().onBackPressed();*/

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

    }



    private void GetStoreProductsApi() {

        int businessId= sharedPrefsHelper.get(AppConstants.STORE_ID,0);




        compositeDisposable.add(apiService.getStoreProducts(String.valueOf(businessId))
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoreProductsSuccess>() {
                    @Override
                    public void accept(StoreProductsSuccess storeProductsSuccess) throws Exception {

                        if(storeProductsSuccess.getSuccess()){



                            storeProductsPayloadArrayList = new ArrayList<>(storeProductsSuccess.getPayload());

                            ArrayList<StoreProductsPayload> productsPayloadArrayList1 = new ArrayList<>();


                            for (int k = 0; k < storeProductsPayloadArrayList.size(); k++) {

                                String item = storeProductsPayloadArrayList.get(k).getName();

                                if (item != null && item.contains(itemname)) {

                                    StoreProductsPayload storeProductsPayload2 = new StoreProductsPayload();

                                    storeProductsPayload2.setId(storeProductsPayloadArrayList.get(k).getId());
                                    storeProductsPayload2.setCatagoryType(storeProductsPayloadArrayList.get(k).getCatagoryType());
                                    storeProductsPayload2.setName(storeProductsPayloadArrayList.get(k).getName());
                                    storeProductsPayload2.setPrice(storeProductsPayloadArrayList.get(k).getPrice());
                                    storeProductsPayload2.setDescription(storeProductsPayloadArrayList.get(k).getDescription());
                                    storeProductsPayload2.setImage("http://smartit.ventures/kwiqmall/SuperAdmin/img/products/" + storeProductsPayloadArrayList.get(k).getImage());


                                    productsPayloadArrayList1.add(storeProductsPayload2);

                                }

                            }

                            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), productsPayloadArrayList1);
                            recyclerViewSearch.setAdapter(recyclerViewAdapter);
                            recyclerViewAdapter.notifyDataSetChanged();


                            recyclerViewAdapter.setOnItemClickListener(new ClickListener() {
                                @Override
                                public void onItemClick(int productid, int position, String nameOfFood, String price, View v, String imageUri, String description) {

                                    Bundle bundle = new Bundle();

                                    UtilityCartData utilityCartData = new UtilityCartData(getActivity());
                                    utilityCartData.setProductid(productid);


                                    bundle.putInt("productid", productid);
                                    bundle.putString("nameOfFood", nameOfFood);
                                    bundle.putString("price", price);
                                    bundle.putString("imageUri", imageUri);
                                    bundle.putInt("position", position);
                                    bundle.putString("description", description);


                                    Fragment foodDeatailsFragment = new FoodDetailsFragment();
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.mainFrame, foodDeatailsFragment, "foodDeatailsFragment");
                                    fragmentTransaction.addToBackStack("foodDeatailsFragment");
                                    foodDeatailsFragment.setArguments(bundle);
                                    fragmentTransaction.commit();




                                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                                }

                                @Override
                                public void onItemLongClick(int position, String nameOfFoof, String price, View v, String imageUri) {

                                }
                            });


                        }
                        else {


                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        showAlertDialog("Retry",throwable.getMessage());
                    }
                }));


/*
        restClient.GetStoreProducts(stringStringHashMap).enqueue(new Callback<StoreProductsSuccess>() {
            @Override
            public void onResponse(Call<StoreProductsSuccess> call, Response<StoreProductsSuccess> response) {

                if(response.body().getSuccess()){


                    storeProductsPayloadArrayList = new ArrayList<>(response.body().getPayload());

                    ArrayList<StoreProductsPayload> productsPayloadArrayList1 = new ArrayList<>();


                        for (int k = 0; k < storeProductsPayloadArrayList.size(); k++) {

                            String item = storeProductsPayloadArrayList.get(k).getName();

                            if (item != null && item.contains(itemname)) {

                                StoreProductsPayload storeProductsPayload2 = new StoreProductsPayload();

                                storeProductsPayload2.setId(storeProductsPayloadArrayList.get(k).getId());
                                storeProductsPayload2.setCatagoryType(storeProductsPayloadArrayList.get(k).getCatagoryType());
                                storeProductsPayload2.setName(storeProductsPayloadArrayList.get(k).getName());
                                storeProductsPayload2.setPrice(storeProductsPayloadArrayList.get(k).getPrice());
                                storeProductsPayload2.setDescription(storeProductsPayloadArrayList.get(k).getDescription());
                                storeProductsPayload2.setImage("http://smartit.ventures/kwiqmall/SuperAdmin/img/products/" + storeProductsPayloadArrayList.get(k).getImage());


                                productsPayloadArrayList1.add(storeProductsPayload2);

                            }

                        }

                    recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), productsPayloadArrayList1);
                    recyclerViewSearch.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();


                    recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int productid, int position, String nameOfFood, String price, View v, String imageUri, String description) {

                            Bundle bundle = new Bundle();

                            UtilityCartData utilityCartData = new UtilityCartData(getActivity());
                            utilityCartData.setProductid(productid);


                            bundle.putInt("productid", productid);
                            bundle.putString("nameOfFood", nameOfFood);
                            bundle.putString("price", price);
                            bundle.putString("imageUri", imageUri);
                            bundle.putInt("position", position);
                            bundle.putString("description", description);


                            Fragment foodDeatailsFragment = new FoodDetailsFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.mainFrame, foodDeatailsFragment, "foodDeatailsFragment");
                            fragmentTransaction.addToBackStack("foodDeatailsFragment");
                            foodDeatailsFragment.setArguments(bundle);
                            fragmentTransaction.commit();




                            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                        }

                        @Override
                        public void onItemLongClick(int position, String nameOfFoof, String price, View v, String imageUri) {

                        }
                    });





                }
                else {

                }
            }

            @Override
            public void onFailure(Call<StoreProductsSuccess> call, Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage(t.getMessage());
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


            }
        });
*/

    }




    public  class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        public Context context;
        public View view;
        public ArrayList<StoreProductsPayload> productsPayloadArrayList = new ArrayList<>();
        private ClickListener clickListener;
        DBHelper databaseHelper;
        public  int _counter = 0;







        public void setOnItemClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;

        }

        public RecyclerViewAdapter(Context context, ArrayList<StoreProductsPayload> productsPayloadArrayList) {
            this.context = context;
            this.productsPayloadArrayList = productsPayloadArrayList;
        }


        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridlayout, parent, false);

            return new RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position) {

            final StoreProductsPayload storeProductsPayload = productsPayloadArrayList.get(position);


            holder.tvTypeofFood.setText(storeProductsPayload.getName());
            holder.tvPricefood.setText(storeProductsPayload.getPrice());

            if(storeProductsPayload.getCatagoryType().equalsIgnoreCase("Non-veg")){

                holder.imageVedNonVeg.setBackgroundResource(R.drawable.nonveg);

            }
            holder.imageVedNonVeg.setBackgroundResource(R.drawable.veg);

            final Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            Picasso.with(context)
                    .load(storeProductsPayload.getImage())
                    .resize(500,250)
                    .error(R.drawable.errortriangle)
                    .into(holder.imageFood);





            holder.btnAddItemCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.btnAddItemCart.setEnabled(false);


                    vibe.vibrate(50);//80 represents the milliseconds (the duration of the vibration)

                    // UserDataUtility utility = new UserDataUtility(context);

                    int user_d = sharedPrefsHelper.get(AppConstants.USER_ID,0);

                    databaseHelper = new DBHelper(context);
                    databaseHelper.insert(user_d,
                            storeProductsPayload.getId(),
                            storeProductsPayload.getName(),
                            storeProductsPayload.getPrice(),"1",
                            storeProductsPayload.getImage(),
                            storeProductsPayload.getPrice());

                    databaseHelper.saveOrders(user_d,storeProductsPayload.getId(),storeProductsPayload.getName(),storeProductsPayload.getPrice()
                            ,"1",storeProductsPayload.getImage(),storeProductsPayload.getPrice());


                    int counter = databaseHelper.getProductsCount();

                    //tvCounter.setVisibility(View.VISIBLE);

                    //tvCounter.setText(String.valueOf(counter));

                    nearby.setBadgeCount(counter);


                    holder.btnAddItemCart.setTextColor(Color.BLACK);
                    clickListener.onItemClick(storeProductsPayload.getId(),position,storeProductsPayload.getName(),storeProductsPayload.getPrice(),v,storeProductsPayload.getImage(),storeProductsPayload.getDescription());

                }
            });


            holder.gridRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.gridRow.setEnabled(false);

                    clickListener.onItemClick(storeProductsPayload.getId(),position,storeProductsPayload.getName(),storeProductsPayload.getPrice(),v,storeProductsPayload.getImage(),storeProductsPayload.getDescription());

                }
            });


            holder.btnAddItemCart.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    clickListener.onItemLongClick(position,storeProductsPayload.getName(),storeProductsPayload.getPrice(),v,storeProductsPayload.getImage());

                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            return productsPayloadArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageFood,imageVedNonVeg;
            private TextView tvTypeofFood,tvFoodNationality,tvPricefood;
            private RelativeLayout gridRow;
            private Button btnAddItemCart;

            public ViewHolder(View itemView) {
                super(itemView);

                //Image Views
                imageFood = itemView.findViewById(R.id.imageFood);
                imageVedNonVeg = itemView.findViewById(R.id.imageVedNonVeg);

                tvTypeofFood=itemView.findViewById(R.id.tvTypeofFood);
                tvFoodNationality=itemView.findViewById(R.id.tvFoodNationality);
                tvPricefood=itemView.findViewById(R.id.tvPricefood);
                btnAddItemCart=itemView.findViewById(R.id.btnAddItemCart);

                gridRow=itemView.findViewById(R.id.gridRow);
                btnAddItemCart=itemView.findViewById(R.id.btnAddItemCart);




            }

        }




    }



}
