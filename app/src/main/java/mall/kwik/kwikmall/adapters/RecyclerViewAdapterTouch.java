package mall.kwik.kwikmall.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.CartModel;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;

/**
 * Created by dharamveer on 7/12/17.
 */

public class RecyclerViewAdapterTouch extends RecyclerView.Adapter<RecyclerViewAdapterTouch.ViewCartHolder>{

    List<CartModel> dataModelArrayList = Collections.emptyList();
    Context context;
    View view;
    DBHelper databaseHelper;
    int count;
    private  final Handler mHandler = new Handler(); // globle variable




    public RecyclerViewAdapterTouch(List<CartModel> dataModelArrayList, Context context ) {
        this.dataModelArrayList = dataModelArrayList;
        this.context = context;
    }





    @Override
    public RecyclerViewAdapterTouch.ViewCartHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_viewcart_activity, parent, false);

        return new RecyclerViewAdapterTouch.ViewCartHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterTouch.ViewCartHolder holder, final int position) {


        CartModel viewCartModel = dataModelArrayList.get(position);


        holder.tvpRICE.setText(viewCartModel.getKEY_price());

        holder.tvFoodName.setText(viewCartModel.getKEY_name());

        holder.txtCountViewCart.setText(viewCartModel.getKEY_qty());
        count = Integer.parseInt(viewCartModel.getKEY_qty());


        Picasso.with(context)
                .load(viewCartModel.getKEY_ImageUri())
                .fit()
                .error(R.drawable.errortriangle)
                .into(holder.cartImageView);

        final Integer[] Quantity = {Integer.valueOf(viewCartModel.getKEY_qty())};



        holder.btnIncViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Quantity[0] = Quantity[0] + 1;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.txtCountViewCart.setText(""+ Quantity[0]);

                    }
                });

            }
        });


        holder.btnDecViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if( Quantity[0]>=2) {
                    Quantity[0] = Quantity[0] - 1;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.txtCountViewCart.setText("" + Quantity[0]);

                        }
                    });
                }


            }
        });


    }
    public long getItemId(int position) {
        return position;

    }


    public void removeItem(int position) {
        dataModelArrayList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(CartModel cartModel, int position) {
        dataModelArrayList.add(position, cartModel);
        // notify item added by position
        notifyItemInserted(position);
    }


    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class ViewCartHolder extends RecyclerView.ViewHolder  {

        private TextView tvFoodName,tvSize,tvKgs,tvpRICE,tvDiscountPrice,tvNormalPrice,tvOff,tvOffText,txtCountViewCart;
        private Button btnDecViewCart,btnIncViewCart;
        private ImageView cartImageView;
        public RelativeLayout viewBackground, viewForeground;

        public ViewCartHolder(View itemView) {
            super(itemView);


            cartImageView = itemView.findViewById(R.id.cartImageView);

            //tvQtyItems = itemView.findViewById(R.id.tvQtyItems);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvpRICE = itemView.findViewById(R.id.tvpRICE);
            tvDiscountPrice = itemView.findViewById(R.id.tvDiscountPrice);
            tvNormalPrice = itemView.findViewById(R.id.tvNormalPrice);
            tvOff = itemView.findViewById(R.id.tvOff);
            tvOffText = itemView.findViewById(R.id.tvOffText);
            txtCountViewCart = itemView.findViewById(R.id.txtCountViewCart);

            //tvSize = itemView.findViewById(R.id.tvSize);
            //tvKgs = itemView.findViewById(R.id.tvKgs);

            btnDecViewCart = itemView.findViewById(R.id.btnDecViewCart);
            btnIncViewCart = itemView.findViewById(R.id.btnIncViewCart);


            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);



/*
            btnIncViewCart.setOnClickListener(this);
            btnDecViewCart.setOnClickListener(this);*/


        }

    /*    @Override
        public void onClick(View v) {


            switch (v.getId()){
                case R.id.btnIncViewCart:

                    int i = Integer.parseInt(txtCountViewCart.getText().toString());
                    i++;
                    txtCountViewCart.setText(String.valueOf(i));
                    notifyItemChanged(getAdapterPosition());
                    break;


                case R.id.btnDecViewCart:

                    i = Integer.parseInt(txtCountViewCart.getText().toString());
                    i--;
                    txtCountViewCart.setText(String.valueOf(i));
                    notifyItemChanged(getAdapterPosition());
                    break;

            }*/


    }
}
