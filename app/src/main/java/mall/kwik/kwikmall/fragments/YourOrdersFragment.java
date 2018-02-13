package mall.kwik.kwikmall.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.BaseFragActivity.BaseFragment;
import mall.kwik.kwikmall.activities.FoodDetailsActivity;
import mall.kwik.kwikmall.activities.FragmentsActivity;
import mall.kwik.kwikmall.adapters.AdapterYourOrders;
import mall.kwik.kwikmall.apiresponse.GetOrderListResponse.GetOrderListPayload;
import mall.kwik.kwikmall.apiresponse.GetOrderListResponse.GetOrderListSuccess;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;
import mall.kwik.kwikmall.sharedpreferences.UtilityCartData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mall.kwik.kwikmall.fragments.AccountFragment.tvOrdersCount;

/**
 * Created by dharamveer on 10/1/18.
 */

public class YourOrdersFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private ImageView imageViewCartBack,imageCartEmpty;
    private RecyclerView recyclerViewCart;

    private AdapterYourOrders adapterYourOrders;
    public ArrayList<GetOrderListPayload> saveOrdersModels = new ArrayList<>();
    DBHelper database;
    RelativeLayout main_layout;
    private String nameOfFood,price,totalItems,imageUri,totalPrice;
    private int sum;
    public  int size;
    private LinearLayout linearStartNew;
    private RelativeLayout layoutStartneworders;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_your_orders, container, false);

        findViewId();


        clickListeners();

        GetOrderListApi();

        return view;

    }


    private void GetOrderListApi() {


        String idUser = String.valueOf(sharedPrefsHelper.get(AppConstants.USER_ID,0));

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("userId",idUser);

        compositeDisposable.add(apiService.getOrderList(stringStringHashMap)
                        .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetOrderListSuccess>() {
                    @Override
                    public void accept(GetOrderListSuccess getOrderListSuccess) throws Exception {

                        if(getOrderListSuccess.getSuccess()){

                            saveOrdersModels = new ArrayList<>(getOrderListSuccess.getPayload());

                            size = saveOrdersModels.size();

                            tvOrdersCount.setText(String.valueOf(size));


                            adapterYourOrders = new AdapterYourOrders(saveOrdersModels, getActivity());

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerViewCart.setLayoutManager(mLayoutManager);
                            recyclerViewCart.setItemAnimator(new DefaultItemAnimator());

                            recyclerViewCart.setAdapter(adapterYourOrders);


                            adapterYourOrders.setItemClickListenerYourOrder(new AdapterYourOrders.ItemClickListenerYourOrder() {
                                @Override
                                public void ItemClick(int productid, String itemname, String price, String imagepath, String des, String nameofhotel) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("itemname",itemname);

                                    UtilityCartData utilityCartData = new UtilityCartData(getActivity());
                                    utilityCartData.setProductid(productid);

                                    bundle.putInt("productid", productid);
                                    bundle.putString("nameOfFood", itemname);
                                    bundle.putString("nameofhotel", nameofhotel);
                                    bundle.putString("price", price);
                                    bundle.putString("imageUri", "http://employeelive.com/kwiqmall/SuperAdmin/img/products/"+imagepath);
                                    bundle.putString("description", des);

                                    Intent intent = new Intent(getActivity(), FoodDetailsActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);


                                    Fragment foodDeatailsFragment = new FoodDetailsFragment();
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.mainFrame, foodDeatailsFragment, "foodDeatailsFragment");
                                    fragmentTransaction.addToBackStack("foodDeatailsFragment");
                                    foodDeatailsFragment.setArguments(bundle);
                                    fragmentTransaction.commit();

                                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                                }

                                @Override
                                public void onDelete(View view, int position) {

                                    saveOrdersModels.remove(position);
                                    adapterYourOrders.notifyItemRemoved(position);

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
        restClient.getOrderList(stringStringHashMap).enqueue(new Callback<GetOrderListSuccess>() {
            @Override
            public void onResponse(Call<GetOrderListSuccess> call, Response<GetOrderListSuccess> response) {

                if(response.body().getSuccess()){

                    saveOrdersModels = new ArrayList<>(response.body().getPayload());

                    size = saveOrdersModels.size();

                    tvOrdersCount.setText(String.valueOf(size));


                    adapterYourOrders = new AdapterYourOrders(saveOrdersModels, getActivity());

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerViewCart.setLayoutManager(mLayoutManager);
                    recyclerViewCart.setItemAnimator(new DefaultItemAnimator());

                    recyclerViewCart.setAdapter(adapterYourOrders);


                    adapterYourOrders.setItemClickListenerYourOrder(new AdapterYourOrders.ItemClickListenerYourOrder() {
                        @Override
                        public void ItemClick(int productid, String itemname, String price, String imagepath, String des, String nameofhotel) {
                            Bundle bundle = new Bundle();
                            bundle.putString("itemname",itemname);

                            UtilityCartData utilityCartData = new UtilityCartData(getActivity());
                            utilityCartData.setProductid(productid);

                            bundle.putInt("productid", productid);
                            bundle.putString("nameOfFood", itemname);
                            bundle.putString("nameofhotel", nameofhotel);
                            bundle.putString("price", price);
                            bundle.putString("imageUri", "http://employeelive.com/kwiqmall/SuperAdmin/img/products/"+imagepath);
                            bundle.putString("description", des);

                            Intent intent = new Intent(getActivity(), FoodDetailsActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);


                            Fragment foodDeatailsFragment = new FoodDetailsFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.mainFrame, foodDeatailsFragment, "foodDeatailsFragment");
                            fragmentTransaction.addToBackStack("foodDeatailsFragment");
                            foodDeatailsFragment.setArguments(bundle);
                            fragmentTransaction.commit();

                            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                        }

                        @Override
                        public void onDelete(View view, int position) {

                            saveOrdersModels.remove(position);
                            adapterYourOrders.notifyItemRemoved(position);

                        }
                    });



                }
                else {


                    recyclerViewCart.setVisibility(View.INVISIBLE);
                    linearStartNew.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<GetOrderListSuccess> call, Throwable t) {
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

    private void clickListeners() {

        imageViewCartBack.setOnClickListener(this);
        layoutStartneworders.setOnClickListener(this);

    }

    private void findViewId() {


        //Relative Layout
        main_layout = view.findViewById(R.id.main_layout);

        //Linear layout
        linearStartNew = view.findViewById(R.id.linearStartNew);


        layoutStartneworders = view.findViewById(R.id.layoutStartneworders);


        imageViewCartBack = view.findViewById(R.id.imageViewCartBack);

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
    }



    @Override
    public void onClick(View v) {

        if(v==imageViewCartBack){

            if ( getFragmentManager().getBackStackEntryCount() > 0)
            {
                getFragmentManager().popBackStack();
                return;
            }
            getActivity().onBackPressed();

        }

        if(v==layoutStartneworders){

            final Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(50);//80 represents the milliseconds (the duration of the vibration)



            startActivity(new Intent(getActivity(),FragmentsActivity.class));
            getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);


        }





    }



}
