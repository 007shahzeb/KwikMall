package mall.kwik.kwikmall.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;


import java.util.ArrayList;
import java.util.HashMap;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.baseFragActivity.BaseFragment;
import mall.kwik.kwikmall.activities.ManageAddressActivity;
import mall.kwik.kwikmall.activities.PaymentsModeActivity;
import mall.kwik.kwikmall.activities.SignInActivity;
import mall.kwik.kwikmall.apiresponse.GetFavouriteResponse.GetFavouritePayload;
import mall.kwik.kwikmall.apiresponse.GetFavouriteResponse.GetFavouriteSuccess;
import mall.kwik.kwikmall.apiresponse.GetOrderListResponse.GetOrderListPayload;
import mall.kwik.kwikmall.apiresponse.GetOrderListResponse.GetOrderListSuccess;
import mall.kwik.kwikmall.dialogs.LogoutConfirmDialog;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;


public class AccountFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private LinearLayout layoutLogoutRipple,linearYourOrders;
    private LinearLayout linearManageAddress,linearPayment;
    private LinearLayout linearFavorites,linearTrackorder;
    private TextView tvUserName,tvUserEmail;
    public static TextView tvOrdersCount,tvFavouritesCount;
    private String idUser;
    private LinearLayout offersLayout,sendFeedbacklayout;

    public ArrayList<GetOrderListPayload> saveOrdersModels = new ArrayList<>();
    public ArrayList<GetFavouritePayload> getFavouritePayloads = new ArrayList<>();
    LogoutConfirmDialog logoutConfirmDialog = null;
    private LinearLayout loginTrue,loginFalse;
    private TextView txtLoginAgain;
    private Context context;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_account_layout, container, false);

        context = getActivity();
        noInternetDialog = new NoInternetDialog.Builder(context).build();



        findViewId();

        clikListeners();

        idUser = String.valueOf(sharedPrefsHelper.get(AppConstants.USER_ID,0));


        tvUserName.setText(sharedPrefsHelper.get(AppConstants.USER_NAME,"Dharam"));
        tvUserEmail.setText(sharedPrefsHelper.get(AppConstants.EMAIL,"abc"));

        GetFavouritesListApi();

        GetOrderListApi();

        if (UserDataUtility.getLogin(getActivity())) {  //true


            loginTrue.setVisibility(View.VISIBLE);
            loginFalse.setVisibility(View.GONE);

        }

        else {
            loginFalse.setVisibility(View.VISIBLE);
            loginTrue.setVisibility(View.GONE);
        }




        return view;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    private void GetFavouritesListApi() {



        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("userId",idUser);


        compositeDisposable.add(apiService.getfavouriteProducts(stringStringHashMap)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetFavouriteSuccess>() {
                    @Override
                    public void accept(GetFavouriteSuccess getFavouriteSuccess) throws Exception {


                        if(getFavouriteSuccess.getSuccess()){

                            getFavouritePayloads = new ArrayList<>(getFavouriteSuccess.getPayload());

                            int size = getFavouritePayloads.size();

                            tvFavouritesCount.setVisibility(View.VISIBLE);
                            tvFavouritesCount.setText(String.valueOf(size));



                        }
                        else {
                            tvFavouritesCount.setVisibility(View.GONE);


                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));


    }


    private void GetOrderListApi() {


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

                            int size = saveOrdersModels.size();

                            tvOrdersCount.setVisibility(View.VISIBLE);
                            tvOrdersCount.setText(String.valueOf(size));



                        }
                        else {

                            tvOrdersCount.setVisibility(View.GONE);


                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

    }


    private void findViewId() {


        //Text Views
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        txtLoginAgain = view.findViewById(R.id.txtLoginAgain);

        //Linear layouts
        offersLayout = view.findViewById(R.id.offersLayout);
        sendFeedbacklayout = view.findViewById(R.id.sendFeedbacklayout);

        linearManageAddress = view.findViewById(R.id.linearManageAddress);
        layoutLogoutRipple = view.findViewById(R.id.layoutLogoutRipple);
        linearPayment = view.findViewById(R.id.linearPayment);
        linearYourOrders = view.findViewById(R.id.linearYourOrders);
        linearFavorites = view.findViewById(R.id.linearFavorites);
        linearTrackorder = view.findViewById(R.id.linearTrackorder);

        loginTrue = view.findViewById(R.id.loginTrue);
        loginFalse = view.findViewById(R.id.loginFalse);



        //static text views
        tvOrdersCount = view.findViewById(R.id.tvOrdersCount);
        tvFavouritesCount = view.findViewById(R.id.tvFavouritesCount);



        materialRipple(linearManageAddress);
        materialRipple(linearPayment);
        materialRipple(offersLayout);
        materialRipple(linearFavorites);
        materialRipple(sendFeedbacklayout);
        materialRipple(layoutLogoutRipple);
        materialRipple(linearYourOrders);
        materialRipple(linearTrackorder);

    }


    public void materialRipple(View view){

        MaterialRippleLayout.on(view)
                .rippleColor(Color.parseColor("#006400"))
                .rippleAlpha(0.5f)
                .rippleHover(true)
                .create();
    }

    private void clikListeners() {

        linearManageAddress.setOnClickListener(this);
        linearPayment.setOnClickListener(this);
        layoutLogoutRipple.setOnClickListener(this);
        linearTrackorder.setOnClickListener(this);
        linearYourOrders.setOnClickListener(this);
        linearFavorites.setOnClickListener(this);

        offersLayout.setOnClickListener(this);
        sendFeedbacklayout.setOnClickListener(this);


        //text view
        txtLoginAgain.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v==txtLoginAgain){


            Intent intent = new Intent(getActivity(),SignInActivity.class);
            startActivity(intent);
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

        }

        if(v==layoutLogoutRipple){


            LogoutConfirmDialog.myOnClickListener myOnClickListener;

            myOnClickListener = new LogoutConfirmDialog.myOnClickListener() {
                @Override
                public void onButtonClick() {


                    UserDataUtility.setLogin(false,getActivity());

                    loginTrue.setVisibility(View.GONE);
                    loginFalse.setVisibility(View.VISIBLE);

                    logoutConfirmDialog.dismiss();


                }
            };

            logoutConfirmDialog = new LogoutConfirmDialog(getActivity(),myOnClickListener);
            logoutConfirmDialog.show();



        }



        if(v==linearFavorites){


            Fragment favouritesFragment = new FavouritesFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFrame, favouritesFragment, "favouritesFragment");
            fragmentTransaction.addToBackStack("favouritesFragment");
            fragmentTransaction.commit();


            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);


        }

        if(v==linearTrackorder){


            Fragment trackYourOrderFragment = new TrackYourOrderFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFrame, trackYourOrderFragment, "trackYourOrderFragment");
            fragmentTransaction.addToBackStack("trackYourOrderFragment");
            fragmentTransaction.commit();


            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);


           /* Intent intent = new Intent(getActivity(),TrackOrderMapActivity.class);
            startActivity(intent);

            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);*/


        }

        if(v==linearYourOrders){




            Fragment yourOrdersFragment = new YourOrdersFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFrame, yourOrdersFragment, "yourOrdersFragment");
            fragmentTransaction.addToBackStack("yourOrdersFragment");
            fragmentTransaction.commit();


            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);


        }


        if(v==linearPayment){

            Intent intent = new Intent(getActivity(),PaymentsModeActivity.class);
            startActivity(intent);

            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);


        }

        if(v==linearManageAddress){

            Intent intent = new Intent(getActivity(),ManageAddressActivity.class);
            startActivity(intent);

            getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        }


    }



}
