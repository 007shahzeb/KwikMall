package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;

import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;
import mall.kwik.kwikmall.activities.aplicationclass.AppController;
import mall.kwik.kwikmall.adapters.RecyclerViewAdapterFilter;
import mall.kwik.kwikmall.apiresponse.FilterListResponse.FilterListPayload;
import mall.kwik.kwikmall.apiresponse.FilterListResponse.FilterListSuccess;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.events.FilterEvent;
import mall.kwik.kwikmall.sharedpreferences.UtilitySP;


/**
 * Created by dharamveer on 29/12/17.
 */

public class FilterActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView filterRecyclerview;
    private LinearLayout linearClear,btnCancel;
    private RecyclerViewAdapterFilter recyclerViewAdapterFilter;
    private List<FilterListPayload> filterListPayloadArrayList = new ArrayList<>();
    public  static RelativeLayout footerFilter;
    public static TextView tvClear;
    private String data = "";
    private List<Integer> list = new ArrayList<>();
    private UtilitySP utilitySP;
    private Context context;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filter);


        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        findViewId();

        clikListeners();



        compositeDisposable.add(apiService.GetCatagories()
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FilterListSuccess>() {
                    @Override
                    public void accept(FilterListSuccess filterListSuccess) throws Exception {


                        if(filterListSuccess.getSuccess()){
                            filterListPayloadArrayList = new ArrayList<>(filterListSuccess.getPayload());

                            recyclerViewAdapterFilter = new RecyclerViewAdapterFilter(filterListPayloadArrayList,FilterActivity.this);

                            recyclerViewAdapterFilter.setItemClickListenerCheck(new RecyclerViewAdapterFilter.ItemClickListenerCheck() {
                                @Override
                                public void itemClick(int pos, int catId) {

                                    list.add(catId);


                                    if (list.size() > 0)
                                    {
                                        StringBuilder sb = new StringBuilder();
                                        for (Integer s : list)
                                        {
                                            sb.append(s).append(",");
                                        }
                                        data = sb.deleteCharAt(sb.length() - 1).toString();
                                    }

                                }
                            });


                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FilterActivity.this);
                            filterRecyclerview.setHasFixedSize(true);

                            filterRecyclerview.setLayoutManager(mLayoutManager);
                            filterRecyclerview.smoothScrollToPosition(0);
                            // filterRecyclerview.addItemDecoration(new SimpleDividerItemDecoration(FilterActivity.this));
                            filterRecyclerview.setItemAnimator(new DefaultItemAnimator());
                            filterRecyclerview.setAdapter(recyclerViewAdapterFilter);

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


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
        compositeDisposable.dispose();
    }



    private void clikListeners() {

        btnCancel.setOnClickListener(this);
        linearClear.setOnClickListener(this);
        footerFilter.setOnClickListener(this);
        tvClear.setOnClickListener(this);

    }

    private void findViewId() {


        filterRecyclerview = findViewById(R.id.filterRecyclerview);

        linearClear = findViewById(R.id.linearClear);

        btnCancel = findViewById(R.id.btnCancel);

        //Relative
        footerFilter = findViewById(R.id.footerFilter);
        tvClear = findViewById(R.id.tvClear);

    }

    @Override
    public void onClick(View v) {




        switch (v.getId()){
            case R.id.btnCancel:

                MaterialRippleLayout.on(btnCancel)
                        .rippleColor(Color.parseColor("#006400"))
                        .rippleAlpha(0.5f)
                        .rippleHover(true)
                        .create();
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);



                break;

            case R.id.footerFilter:

                if(data!=null) {

                    ( (AppController)  getApplication()).bus().send(new FilterEvent(data));
                    sharedPrefsHelper.put(AppConstants.FILTER_DATA,"");

                   // imageFilteOn.setVisibility(View.VISIBLE);
                    startActivity(new Intent(context,FragmentsActivity.class));
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                }

                break;



            case R.id.linearClear:

                ( (AppController)  getApplication()).bus().send(new FilterEvent(""));


                recyclerViewAdapterFilter.unselectAll();
                //imageFilteOn.setVisibility(View.GONE);
                footerFilter.setBackgroundColor(Color.parseColor("#00d048")); //darkgreen

                break;


        }




        if(v==btnCancel){




        }

        if(v==footerFilter){



        }


        if(v==tvClear){


        }

        if(v==linearClear){





        }

    }





}
