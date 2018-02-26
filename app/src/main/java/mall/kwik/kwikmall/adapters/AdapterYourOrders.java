package mall.kwik.kwikmall.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mall.kwik.kwikmall.apiresponse.GetOrderListResponse.GetOrderListPayload;
import mall.kwik.kwikmall.R;

/**
 * Created by dharamveer on 20/12/17.
 */

public class AdapterYourOrders extends RecyclerView.Adapter<AdapterYourOrders.MyViewHolder> {

    public ArrayList<GetOrderListPayload> getSaveOrdersModelList = new ArrayList<>();

    Context context;
    View view;
    private ItemClickListenerYourOrder itemClickListenerYourOrder;

    public AdapterYourOrders(ArrayList<GetOrderListPayload> getSaveOrdersModelList, Context context) {
        this.getSaveOrdersModelList = getSaveOrdersModelList;
        this.context = context;
    }

    public interface ItemClickListenerYourOrder{
        void ItemClick(int productid,String itemname,String price,String imagepath, String des,String nameofhotel);
        void onDelete(int pos,int userId, String orderNo,int productId);

    }

    public void setItemClickListenerYourOrder(ItemClickListenerYourOrder itemClickListener) {
        this.itemClickListenerYourOrder = itemClickListener;

    }


    @Override
    public AdapterYourOrders.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_your_orders_activity, parent, false);

        return new AdapterYourOrders.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterYourOrders.MyViewHolder holder, final int position) {

        GetOrderListPayload getOrderListPayload = getSaveOrdersModelList.get(position);

        holder.tvFoodName.setText(getOrderListPayload.getProductName());
        holder.tvPriceItem.setText(getOrderListPayload.getProductPrice());


        String date = getOrderListPayload.getDate();


        String[] spliteddate = date.split("\\s+");


        holder.tvOrderDate.setText(spliteddate[0]);


        Picasso.with(view.getContext())
                .load("http://employeelive.com/kwiqmall/SuperAdmin/img/products/"+getOrderListPayload.getImage())
                .fit()
                .error(R.drawable.errortriangle)
                .into(holder.cartImageView);



        final int productid = getOrderListPayload.getProductId();
        final String itemname = getOrderListPayload.getProductName();
        final String price = getOrderListPayload.getProductPrice();
        final String imagepath = getOrderListPayload.getImage();
        final String des = getOrderListPayload.getDescription();
        final String nameofhotel = getOrderListPayload.getBussinessName();

        holder.relativeBuyitagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            itemClickListenerYourOrder.ItemClick(productid,itemname,price,imagepath,des,nameofhotel);


            }
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                itemClickListenerYourOrder.onDelete(position,getOrderListPayload.getUserId(),
                                    getOrderListPayload.getOrderNo(),getOrderListPayload.getProductId());


            }
        });

    }





    @Override
    public int getItemCount() {
        return getSaveOrdersModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFoodName,tvPriceItem,tvOrderDate;
        private ImageView cartImageView;
        private Button btnDelete;
        public RelativeLayout relativeBuyitagain;

        public MyViewHolder(View itemView) {
            super(itemView);

            //Image Views
            cartImageView = itemView.findViewById(R.id.cartImageView);

            //TextViews
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvPriceItem = itemView.findViewById(R.id.tvPriceItem);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);

            //Relative Layouts
            relativeBuyitagain = itemView.findViewById(R.id.relativeBuyitagain);


            //Button
            btnDelete = itemView.findViewById(R.id.btnDelete);


        }
    }
}