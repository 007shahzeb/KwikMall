package mall.kwik.kwikmall.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.activities.aplicationclass.AppController;
import mall.kwik.kwikmall.apiresponse.GetStoreProducts.StoreProductsPayload;
import mall.kwik.kwikmall.apiresponse.GetStoreProducts.StoreProductsSuccess;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.manager.GitApiInterface;
import mall.kwik.kwikmall.sharedpreferences.UtilityCartData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogMenu extends Dialog {


    @Inject
    GitApiInterface gitApiInterface;

    @Inject
    SharedPrefsHelper sharedPrefsHelper;

    private RecyclerView recylerviewDialog;
    private RecyclerViewAdapter recyclerViewAdapter;


    public DialogMenu(@NonNull Context context) {
        super(context);
    }

    private ArrayList<StoreProductsPayload> storeProductsPayloadArrayList = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        (( AppController) getContext().getApplicationContext()).getComponent().inject(DialogMenu.this);

        setContentView(R.layout.dialog_menu);

        recylerviewDialog = findViewById(R.id.recylerviewDialog);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recylerviewDialog.setLayoutManager(mLayoutManager);
        recylerviewDialog.setItemAnimator(new DefaultItemAnimator());

        GetStoreProductsApi();

    }

    private void GetStoreProductsApi() {


        int storeId= sharedPrefsHelper.get(AppConstants.STORE_ID,1);


        compositeDisposable.add(gitApiInterface.getStoreProducts(String.valueOf(storeId))
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoreProductsSuccess>() {
                    @Override
                    public void accept(StoreProductsSuccess storeProductsSuccess) throws Exception {


                        if(storeProductsSuccess.getSuccess()){


                            storeProductsPayloadArrayList = new ArrayList<>(storeProductsSuccess.getPayload());

                            ArrayList<StoreProductsPayload> productsPayloadArrayList = new ArrayList<>();


                            for(int i =0;i<storeProductsPayloadArrayList.size();i++){

                                StoreProductsPayload storeProductsPayload = new StoreProductsPayload();

                                storeProductsPayload.setName(storeProductsPayloadArrayList.get(i).getName());
                                storeProductsPayload.setPrice(storeProductsPayloadArrayList.get(i).getPrice());

                                productsPayloadArrayList.add(storeProductsPayload);

                            }

                            recyclerViewAdapter = new RecyclerViewAdapter(getContext(),productsPayloadArrayList);

                            recylerviewDialog.setAdapter(recyclerViewAdapter);



                        }
                        else {





                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));



/*
        restClient.GetStoreProducts(stringStringHashMap).enqueue(new Callback<StoreProductsSuccess>() {
            @Override
            public void onResponse(Call<StoreProductsSuccess> call, Response<StoreProductsSuccess> response) {

                if(response.body().getSuccess()){

                    storeProductsPayloadArrayList = new ArrayList<>(response.body().getPayload());

                    ArrayList<StoreProductsPayload> productsPayloadArrayList = new ArrayList<>();


                    for(int i =0;i<storeProductsPayloadArrayList.size();i++){

                        StoreProductsPayload storeProductsPayload = new StoreProductsPayload();

                        storeProductsPayload.setName(storeProductsPayloadArrayList.get(i).getName());
                        storeProductsPayload.setPrice(storeProductsPayloadArrayList.get(i).getPrice());

                        productsPayloadArrayList.add(storeProductsPayload);

                    }

                    recyclerViewAdapter = new RecyclerViewAdapter(getContext(),productsPayloadArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recylerviewDialog.setLayoutManager(mLayoutManager);
                    recylerviewDialog.setItemAnimator(new DefaultItemAnimator());
                    recylerviewDialog.setAdapter(recyclerViewAdapter);


                }
                else {

                }
            }

            @Override
            public void onFailure(Call<StoreProductsSuccess> call, Throwable t) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
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


    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

        Context context;
        View view;
        int row_index;
        private ArrayList<StoreProductsPayload> productsPayloadArrayList = new ArrayList<>();


        public interface OnItemClickInterface{

            void itemClickInteface();
        }

        public RecyclerViewAdapter(Context context, ArrayList<StoreProductsPayload> productsPayloadArrayList) {
            this.context = context;
            this.productsPayloadArrayList = productsPayloadArrayList;
        }

        @Override
        public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_menu_row, parent, false);

            return new MyViewHolder(view);        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.MyViewHolder holder, final int position) {

            StoreProductsPayload storeProductsPayload = productsPayloadArrayList.get(position);

            holder.itemName.setText(storeProductsPayload.getName());
            holder.tvQty.setText(storeProductsPayload.getPrice());


            holder.relativeLayoutMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index = position;
                    notifyDataSetChanged();
                }
            });

            if(row_index==position){
                holder.relativeLayoutMenu.setBackgroundColor(Color.parseColor("#567845"));
                holder.itemName.setTextColor(Color.parseColor("#00d048"));
                holder.tvQty.setTextColor(Color.parseColor("#00d048"));
            }
            else
            {
                holder.relativeLayoutMenu.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.itemName.setTextColor(Color.BLACK);
                holder.tvQty.setTextColor(Color.BLACK);
            }



        }

        @Override
        public int getItemCount() {
            return productsPayloadArrayList.size();        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView itemName,tvQty;
            private RelativeLayout relativeLayoutMenu;

            public MyViewHolder(View itemView) {
                super(itemView);

                relativeLayoutMenu = itemView.findViewById(R.id.relativeLayoutMenu);

                itemName = itemView.findViewById(R.id.tvItemName);
                tvQty = itemView.findViewById(R.id.tvQty);





            }
        }
    }

}