package mall.kwik.kwikmall.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.baseFragActivity.BaseFragment;
import mall.kwik.kwikmall.adapters.RecyclerViewAdapterSearchAllPro;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.apiresponse.GetAllProductsResponse.GetAllProductsPayload;
import mall.kwik.kwikmall.apiresponse.GetAllProductsResponse.GetAllProductsSuccess;
import mall.kwik.kwikmall.SimpleDividerItemDecoration;




public class ExploreFragment extends BaseFragment implements View.OnClickListener {


    private View view;
    private RecyclerView recyclerviewExplore;
    private RecyclerViewAdapterSearchAllPro recyclerViewAdapterAllPro;
    private EditText search_products_ed;
    private RelativeLayout clearable_button_clear_explore;
    private ArrayList<GetAllProductsPayload> getAllProductsPayloads;
    private TextView editextText;
    private LinearLayout linearNoItemsMatchLayout, linearNoMatchFoundImage, linearNoInternetImage;
    private Button btnCross;
    private RelativeLayout btnRetryInternet;
    private ProgressBar exploreProgressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.explore_fragment_footer, container, false);


        findViewId();

        clearText();
        showHideClearButton();

        btnRetryInternet.setOnClickListener(this);

       /* clearable_edit_explore.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {



                clearable_edit_explore.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(clearable_edit_explore, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });

        clearable_edit_explore.requestFocus();*/

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerviewExplore.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerviewExplore.setItemAnimator(new DefaultItemAnimator());
        recyclerviewExplore.setLayoutManager(mLayoutManager);


        if (isConnected()) {


            requestJsonObject();

        } else {

            Snackbar.with(getActivity(), null).type(Type.ERROR).message("Internet Not Connected").duration(Duration.SHORT).show();

        }


        return view;

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

        recyclerviewExplore = view.findViewById(R.id.recyclerviewExplore);
        search_products_ed = view.findViewById(R.id.search_products_ed);

        clearable_button_clear_explore = view.findViewById(R.id.clearable_button_clear_explore);

        //Linear layout
        linearNoItemsMatchLayout = view.findViewById(R.id.linearNoItemsMatchLayout);
        linearNoMatchFoundImage = view.findViewById(R.id.linearNoMatchFoundImage);
        linearNoInternetImage = view.findViewById(R.id.linearNoInternetImage);

        editextText = view.findViewById(R.id.editextText);

        //Button
        btnCross = view.findViewById(R.id.btnCross);
        btnRetryInternet = view.findViewById(R.id.btnRetryInternet);

        //Progress bar
        exploreProgressBar = view.findViewById(R.id.exploreProgressBar);

    }


    private void requestJsonObject() {


        compositeDisposable.add(apiService.getAllProducts()
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetAllProductsSuccess>() {
                    @Override
                    public void accept(GetAllProductsSuccess getAllProductsSuccess) throws Exception {

                        if (getAllProductsSuccess.getSuccess()) {


                            getAllProductsPayloads = new ArrayList<>(getAllProductsSuccess.getPayload());

                            recyclerViewAdapterAllPro = new RecyclerViewAdapterSearchAllPro(getAllProductsPayloads, getActivity());


                            recyclerViewAdapterAllPro.searchItemClickListener(new RecyclerViewAdapterSearchAllPro.ItemClickListener() {
                                @Override
                                public void ItemClick(int productid, String itemname, String price, String imagepath, String des) {

                                    Bundle bundle = new Bundle();
                                    bundle.putString("itemname", itemname);


                                    sharedPrefsHelper.put(AppConstants.PRODUCT_ID,productid);


                                    bundle.putInt("productid", productid);
                                    bundle.putString("nameOfFood", itemname);
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
                            });



                            recyclerviewExplore.setAdapter(recyclerViewAdapterAllPro);
                            recyclerViewAdapterAllPro.notifyDataSetChanged();



                        } else {

                            showAlertDialog("Retry", "Error");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        showAlertDialog("Retry", throwable.getMessage());
                    }
                }));


    }




    void clearText(){

        clearable_button_clear_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                linearNoItemsMatchLayout.setVisibility(View.GONE);
                linearNoMatchFoundImage.setVisibility(View.GONE);
                search_products_ed.setText("");
            }
        });

    }

    void showHideClearButton(){

        search_products_ed.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



                if(isConnected()){

                    exploreProgressBar.setVisibility(View.VISIBLE);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            exploreProgressBar.setVisibility(View.GONE);

                        }
                    }, 1000);


                    if(recyclerViewAdapterAllPro.getLength()<=0){


                        linearNoItemsMatchLayout.setVisibility(View.VISIBLE);
                        linearNoMatchFoundImage.setVisibility(View.VISIBLE);
                        editextText.setText(Html.fromHtml("&ldquo; " + search_products_ed.getText().toString() + " &rdquo;"));

                    }





                    if (s.length() > 0){


                        btnCross.setVisibility(View.VISIBLE);
                        recyclerviewExplore.setVisibility(View.VISIBLE);


                    }

                    else {
                        btnCross.setVisibility(View.INVISIBLE);

                        linearNoItemsMatchLayout.setVisibility(View.GONE);
                        linearNoMatchFoundImage.setVisibility(View.GONE);

                        recyclerviewExplore.setVisibility(View.GONE);

                    }

                }
                else {


                    if(count>2){


                        linearNoInternetImage.setVisibility(View.VISIBLE);
                        recyclerviewExplore.setVisibility(View.GONE);

                    }
                    else {


                    }
                }





            }

            @Override
            public void afterTextChanged(Editable s) {


                if(isConnected()) {

                    filter(s.toString());
                }

            }
        });

    }

    private void filter(String text) {
        //new array list that will hold the filtered data

        List<GetAllProductsPayload> getAllProductsPayloads1 = new ArrayList<>();

        //looping through existing elements
        for (GetAllProductsPayload s : getAllProductsPayloads) {
            //if the existing elements contains the search input
            if (s.getName().toString().toLowerCase().contains(text.toString().toLowerCase())) {
                //adding the element to filtered list
                getAllProductsPayloads1.add(s);
            }

        }

        //tvNoItemsMatch.setVisibility(View.VISIBLE);
        //calling a method of the adapter class and passing the filtered list
        recyclerViewAdapterAllPro.filterList(getAllProductsPayloads1);
        recyclerViewAdapterAllPro.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {


        if(v==btnRetryInternet){

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

            //filter(search_products_ed.getText().toString());

        }


    }
}









