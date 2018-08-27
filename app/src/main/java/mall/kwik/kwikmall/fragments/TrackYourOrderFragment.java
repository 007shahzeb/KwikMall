package mall.kwik.kwikmall.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.apiresponse.GetDeliveryStatus.GetDeliveryStatusSuccess;
import mall.kwik.kwikmall.baseFragActivity.BaseFragment;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.adapters.TimeLineAdapter;
import mall.kwik.kwikmall.models.TimeLineModel;
import mall.kwik.kwikmall.utils.OrderStatus;
import pl.droidsonroids.gif.GifImageView;


public class TrackYourOrderFragment extends BaseFragment {


    // private RecyclerView mRecyclerView;
    // private List<TimeLineModel> mDataList = new ArrayList<>();
    // private TimeLineAdapter mTimeLineAdapter;
    //  private boolean mWithLinePadding = true;

    private Unbinder unbinder;
    @BindView(R.id.backArrowImg)
    ImageView backArrowImg;

    @BindView(R.id.imageOrderGif)
    GifImageView imageOrderGif;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.track_order_frag, container, false);
        unbinder = ButterKnife.bind(this, view);

        context = getActivity();
        noInternetDialog = new NoInternetDialog.Builder(context).build();

//        mRecyclerView =  view.findViewById(R.id.recyclerView);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        mRecyclerView.setHasFixedSize(true)
//        ;

        // initView();

        backArrowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                }
                getActivity().onBackPressed();


            }
        });


        int bussinessId = sharedPrefsHelper.get(AppConstants.STORE_ID, 0);
        String orderNo = sharedPrefsHelper.get(AppConstants.ORDER_NO, "");
        int userId = sharedPrefsHelper.get(AppConstants.USER_ID, 0);


        compositeDisposable.add(apiService.getDeliveryStatus(String.valueOf(bussinessId), orderNo, String.valueOf(userId))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetDeliveryStatusSuccess>() {
                    @Override
                    public void accept(GetDeliveryStatusSuccess getDeliveryStatusSuccess) throws Exception {


                        if (getDeliveryStatusSuccess.getSuccess()) {


                            if (getDeliveryStatusSuccess.getPayload().getDeliveryStatus() == 0) {
                                imageOrderGif.setVisibility(View.VISIBLE);
                            } else if (getDeliveryStatusSuccess.getPayload().getDeliveryStatus() == 1) {
                                imageOrderGif.setBackgroundResource(R.drawable.cashondelivery);
                            } else if (getDeliveryStatusSuccess.getPayload().getDeliveryStatus() == 2) {
                                imageOrderGif.setBackgroundResource(R.drawable.cooking);

                            } else if (getDeliveryStatusSuccess.getPayload().getDeliveryStatus() == 3) {
                                imageOrderGif.setBackgroundResource(R.drawable.deliveryonway);
                            }


//                            if (getDeliveryStatusSuccess.getPayload().equals(0)) {
//
//                                imageOrderGif.setVisibility(View.VISIBLE);
//
//                            } else if (getDeliveryStatusSuccess.getPayload().equals(1)) {
//
//
//                                //order successfully delivered
//
//                                imageOrderGif.setBackgroundResource(R.drawable.cashondelivery);
//                            } else if (getDeliveryStatusSuccess.getPayload().equals(2)) {
//
//                                //order is ready to deliver
//                                imageOrderGif.setBackgroundResource(R.drawable.cooking);
//
//
//                            } else if (getDeliveryStatusSuccess.getPayload().equals(3)) {
//
//                                //order is on the way
//
//                                imageOrderGif.setBackgroundResource(R.drawable.deliveryonway);
//
//                            }


                        } else {

                            showAlertDialog("Retry", "False");

                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        showAlertDialog("Retry", throwable.getMessage());


                    }
                }));


        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    private void initView() {
//        setDataListItems();
//        mTimeLineAdapter = new TimeLineAdapter(mDataList,mWithLinePadding);
//        mRecyclerView.setAdapter(mTimeLineAdapter);
//    }
//
//    private void setDataListItems(){
//        mDataList.add(new TimeLineModel("Item successfully delivered", "", OrderStatus.INACTIVE));
//        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", OrderStatus.ACTIVE));
//        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", OrderStatus.COMPLETED));
//        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", OrderStatus.COMPLETED));
//        mDataList.add(new TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", OrderStatus.COMPLETED));
//        mDataList.add(new TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", OrderStatus.COMPLETED));
//        mDataList.add(new TimeLineModel("Order processing initiated", "2017-02-10 15:00", OrderStatus.COMPLETED));
//        mDataList.add(new TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", OrderStatus.COMPLETED));
//        mDataList.add(new TimeLineModel("Order placed successfully", "2017-02-10 14:00", OrderStatus.COMPLETED));
//    }

}
