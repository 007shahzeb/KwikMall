package mall.kwik.kwikmall.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mall.kwik.kwikmall.apiresponse.GetStoreProducts.StoreProductsPayload;
import mall.kwik.kwikmall.R;

/**
 * Created by dharamveer on 15/12/17.
 */

public class RecyclerViewAdapterSearch extends RecyclerView.Adapter<RecyclerViewAdapterSearch.MyViewHolder>{

    private ArrayList<StoreProductsPayload> storeProductsPayloadArrayList = new ArrayList<>();
    Context context;
    View view;
    String clickedItem;
    private ItemClickListener itemClickListener;


    public interface ItemClickListener{
        void ItemClick(String itemname);
    }


    public void searchItemClickListener(RecyclerViewAdapterSearch.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    public RecyclerViewAdapterSearch(ArrayList<StoreProductsPayload> storeProductsPayloadArrayList, Context context) {
        this.storeProductsPayloadArrayList = storeProductsPayloadArrayList;
        this.context = context;
    }


    public void filterList(ArrayList<StoreProductsPayload> storeProductsPayloads) {
        this.storeProductsPayloadArrayList = storeProductsPayloads;
        notifyDataSetChanged();
    }



    @Override
    public RecyclerViewAdapterSearch.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_explore_fragment, parent, false);

        return new RecyclerViewAdapterSearch.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterSearch.MyViewHolder holder, final int position) {

        StoreProductsPayload storeProductsPayload = storeProductsPayloadArrayList.get(position);

        holder.tvResFoodNames.setText(storeProductsPayload.getName());



        holder.searchItemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    clickedItem = holder.tvResFoodNames.getText().toString();
                    itemClickListener.ItemClick(clickedItem);

            }
        });



    }

    public int getLength(){

        return storeProductsPayloadArrayList.size();

    }



    @Override
    public int getItemCount() {
        return storeProductsPayloadArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvResFoodNames;
        private RelativeLayout searchItemClick;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvResFoodNames = itemView.findViewById(R.id.tvResFoodNames);

            searchItemClick = itemView.findViewById(R.id.searchItemClick);


        }
    }
}