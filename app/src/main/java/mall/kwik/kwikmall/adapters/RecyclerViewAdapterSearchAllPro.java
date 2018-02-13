package mall.kwik.kwikmall.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.apiresponse.GetAllProductsResponse.GetAllProductsPayload;


public class RecyclerViewAdapterSearchAllPro extends RecyclerView.Adapter<RecyclerViewAdapterSearchAllPro.MyViewHolder> {

    private List<GetAllProductsPayload> getAllProductsPayloadArrayList = new ArrayList<>();
    private Context context;
    private View view;
    private String clickedItem;
    private ItemClickListener itemClickListener;


    public RecyclerViewAdapterSearchAllPro(List<GetAllProductsPayload> getAllProductsPayloadArrayList, Context context) {
        this.getAllProductsPayloadArrayList = getAllProductsPayloadArrayList;
        this.context = context;
    }


    public interface ItemClickListener{
        void ItemClick(int productid,String itemname,String price,String imagepath, String des);
    }


    public void searchItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }


    public void filterList(List<GetAllProductsPayload> getAllProductsPayloads) {
        this.getAllProductsPayloadArrayList = getAllProductsPayloads;
        notifyDataSetChanged();
    }

    public int getLength(){

        return getAllProductsPayloadArrayList.size();

    }


    @Override
    public RecyclerViewAdapterSearchAllPro.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_explore_all_fragment, parent, false);

        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterSearchAllPro.MyViewHolder holder, final int position) {

        final GetAllProductsPayload getAllProductsPayload = getAllProductsPayloadArrayList.get(position);

        holder.tvFoodNames.setText(getAllProductsPayload.getName());



        holder.itemClickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                clickedItem = holder.tvFoodNames.getText().toString();
                itemClickListener.ItemClick(getAllProductsPayload.getId()
                        ,clickedItem
                        ,getAllProductsPayload.getPrice(),
                        getAllProductsPayload.getImage(),
                        getAllProductsPayload.getDescription());






            }
        });



    }

    @Override
    public int getItemCount() {

        return getAllProductsPayloadArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFoodNames;
        private RelativeLayout itemClickSearch;

        public MyViewHolder(View itemView) {
            super(itemView);


            tvFoodNames = itemView.findViewById(R.id.tvFoodNames);

            itemClickSearch = itemView.findViewById(R.id.itemClickSearch);


        }
    }
}
