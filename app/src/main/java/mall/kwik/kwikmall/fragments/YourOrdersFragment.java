package mall.kwik.kwikmall.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.apiresponse.DeleteOrderSuccess.DeleteOrderSuccess;
import mall.kwik.kwikmall.baseFragActivity.BaseFragment;
import mall.kwik.kwikmall.activities.FoodDetailsActivity;
import mall.kwik.kwikmall.activities.FragmentsActivity;
import mall.kwik.kwikmall.adapters.AdapterYourOrders;
import mall.kwik.kwikmall.apiresponse.GetOrderListResponse.GetOrderListPayload;
import mall.kwik.kwikmall.apiresponse.GetOrderListResponse.GetOrderListSuccess;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.UtilityCartData;

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
    RelativeLayout main_layout;
    public  int size;
    private RelativeLayout layoutStartneworders;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                                    bundle.putString("imageUri", imagepath);
                                    bundle.putString("description", des);

                                    Fragment foodDeatailsFragment = new FoodDetailsFragment();
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.mainFrame, foodDeatailsFragment, "foodDeatailsFragment");
                                    fragmentTransaction.addToBackStack("foodDeatailsFragment");
                                    foodDeatailsFragment.setArguments(bundle);
                                    fragmentTransaction.commit();

                                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                                }

                                @Override
                                public void onDelete(int position,int userId, String orderNo,int productId) {


                                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                                    stringStringHashMap.put("userId",String.valueOf(userId));
                                    stringStringHashMap.put("orderNo",orderNo);
                                    stringStringHashMap.put("productId",String.valueOf(productId));

                                    compositeDisposable.add(apiService.deleteOrderByUser(stringStringHashMap)
                                            .subscribeOn(Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<DeleteOrderSuccess>() {
                                                @Override
                                                public void accept(DeleteOrderSuccess deleteOrderSuccess) throws Exception {


                                                    if(deleteOrderSuccess.getSuccess()){

                                                        saveOrdersModels.remove(position);
                                                        adapterYourOrders.notifyItemRemoved(position);

                                                    }
                                                    else {

                                                        showAlertDialog("Retry",deleteOrderSuccess.getMessage());

                                                    }
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {

                                                    showAlertDialog("Retry",throwable.getMessage());

                                                    compositeDisposable.dispose();

                                                }
                                            }));








                                }
                            });



                        }
                        else {


                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        compositeDisposable.dispose();


                        showAlertDialog("Retry",throwable.getMessage());

                    }
                }));


    }

    private void clickListeners() {

        imageViewCartBack.setOnClickListener(this);
        layoutStartneworders.setOnClickListener(this);

    }

    private void findViewId() {


        //Relative Layout
        main_layout = view.findViewById(R.id.main_layout);

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
