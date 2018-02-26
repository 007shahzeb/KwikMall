package mall.kwik.kwikmall.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.baseFragActivity.BaseFragment;
import mall.kwik.kwikmall.adapters.RecyclerViewAdapterSearch;
import mall.kwik.kwikmall.apiresponse.GetStoreProducts.StoreProductsPayload;
import mall.kwik.kwikmall.apiresponse.GetStoreProducts.StoreProductsSuccess;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.SimpleDividerItemDecoration;


public class SearchRestaurantsFragment extends BaseFragment {

    private View view;

    private RecyclerView recyclerviewSearch;
    private RecyclerViewAdapterSearch recyclerViewAdapter;
    private EditText clearable_edit;
    private ImageView imageCancel;
    LayoutInflater inflater = null;
    private ArrayList<StoreProductsPayload> storeProductsPayloadArrayList = new ArrayList<>();
    ArrayList<StoreProductsPayload> productsPayloadArrayList;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_by_store_id, container, false);

        findViewId();

        clickListeners();


        imageCancel.setVisibility(RelativeLayout.INVISIBLE);
        clearText();
        showHideClearButton();

        GetStoreProductsApi();


        return view;

    }



    private void GetStoreProductsApi() {




        int storeId= sharedPrefsHelper.get(AppConstants.STORE_ID,0);




        compositeDisposable.add(apiService.getStoreProducts(String.valueOf(storeId))
                        .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoreProductsSuccess>() {
                    @Override
                    public void accept(StoreProductsSuccess storeProductsSuccess) throws Exception {

                        if(storeProductsSuccess.getSuccess()){

                            storeProductsPayloadArrayList = new ArrayList<>(storeProductsSuccess.getPayload());

                            productsPayloadArrayList = new ArrayList<>();

                            for(int i =0;i<storeProductsPayloadArrayList.size();i++){

                                StoreProductsPayload storeProductsPayload = new StoreProductsPayload();


                                storeProductsPayload.setName(storeProductsPayloadArrayList.get(i).getName());

                                productsPayloadArrayList.add(storeProductsPayload);


                            }

                            recyclerViewAdapter = new RecyclerViewAdapterSearch(productsPayloadArrayList,getActivity());

                            recyclerViewAdapter.searchItemClickListener(new RecyclerViewAdapterSearch.ItemClickListener() {
                                @Override
                                public void ItemClick(String itemname) {


                                    Bundle args = new Bundle();
                                    args.putString("itemname",itemname);

                                    RestaurantsProductsFragment restaurantsProductsFragment = new RestaurantsProductsFragment();
                                    restaurantsProductsFragment.setArguments(args);
                                    getFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.mainFrame, restaurantsProductsFragment,"restaurantsProductsFragment")
                                            .commit();



                                }
                            });

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerviewSearch.setLayoutManager(mLayoutManager);
                            recyclerviewSearch.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                            recyclerviewSearch.setItemAnimator(new DefaultItemAnimator());
                            recyclerviewSearch.setAdapter(recyclerViewAdapter);


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


    void clearText(){

        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearable_edit.setText("");
            }
        });

    }

    void showHideClearButton(){

        clearable_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>0)
                    imageCancel.setVisibility(View.VISIBLE);

                else
                    imageCancel.setVisibility(RelativeLayout.INVISIBLE);



            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });

    }

    private void filter(String text) {
        //new array list that will hold the filtered data

        ArrayList<StoreProductsPayload> searchList = new ArrayList<>();

        //looping through existing elements
        for (StoreProductsPayload s : storeProductsPayloadArrayList) {
            //if the existing elements contains the search input
            if (s.getName().toString().toLowerCase().contains(text.toString().toLowerCase())) {
                //adding the element to filtered list
                searchList.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        recyclerViewAdapter.filterList(searchList);
    }


    private void findViewId() {

        recyclerviewSearch = view.findViewById(R.id.recyclerviewSearch);
        clearable_edit = view.findViewById(R.id.clearable_edit);
        imageCancel = view.findViewById(R.id.imageCancel);
    }

    private void clickListeners() {

    }



}
