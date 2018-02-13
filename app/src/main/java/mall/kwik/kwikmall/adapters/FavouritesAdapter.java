package mall.kwik.kwikmall.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.activities.aplicationclass.AppController;
import mall.kwik.kwikmall.apiresponse.GetFavouriteResponse.GetFavouritePayload;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;

import static mall.kwik.kwikmall.activities.FragmentsActivity.nearby;


/**
 * Created by dharamveer on 21/12/17.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesHolder> {


    private Context context;
    private View view;
    public FavouriteClickListener favouriteClickListener;
    public FavouriteClickListenerToCart favouriteClickListenerToCart;
    private DBHelper databaseHelper;
    public ArrayList<GetFavouritePayload> getFavouritePayloadArrayList = new ArrayList<>();

    @Inject
     SharedPrefsHelper sharedPrefsHelper;



    public FavouritesAdapter(Context context, ArrayList<GetFavouritePayload> getFavouritePayloadArrayList) {
        this.context = context;
        this.getFavouritePayloadArrayList = getFavouritePayloadArrayList;
        ((AppController) context.getApplicationContext()).getComponent().inject(this);
    }

    public interface FavouriteClickListener{

        //void buttonClick(int productid,String nameOfFood, String price, View v,String imageUri,String description);
        void onDelete(View view, int position);

    }

    public interface FavouriteClickListenerToCart{

        void GoToCartActivity(View v);
    }

    public void setOnItemClickListener(FavouritesAdapter.FavouriteClickListener favouriteClickListener) {
        this.favouriteClickListener = favouriteClickListener;

    }
    public void getToCartActivity(FavouritesAdapter.FavouriteClickListenerToCart favouriteClickListenerToCart){

        this.favouriteClickListenerToCart = favouriteClickListenerToCart;
    }

    @Override
    public FavouritesHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_favourites_activity, parent, false);
        return new FavouritesAdapter.FavouritesHolder(view);


    }

    @Override
    public void onBindViewHolder(final FavouritesHolder holder, final int position) {

        GetFavouritePayload getFavouritePayload = getFavouritePayloadArrayList.get(position);

        Picasso.with(context)
                .load(getFavouritePayload.getImageUrl())
                .fit()
                .error(R.drawable.errortriangle)
                .into(holder.cartImageViewFavourites);



        String date = getFavouritePayload.getCreatedOn();


        String[] spliteddate = date.split("\\s+");



        holder.tvStoreName.setText(getFavouritePayload.getBussinessName());
        holder.tvFoodNameFavourites.setText(getFavouritePayload.getProductName());
        holder.tvPriceItemFavourties.setText(getFavouritePayload.getProductPrice());
        holder.tvAddFavouritesDate.setText(spliteddate[0]);


        databaseHelper = new DBHelper(view.getContext());


      /*  UserDataUtility userDataUtility = new UserDataUtility(view.getContext());
        final int userId = userDataUtility.getUserId();*/

       int userId =  sharedPrefsHelper.get(AppConstants.USER_ID,0);

        final int product_id = getFavouritePayload.getProductId();
        final String nameOfFood = getFavouritePayload.getProductName();
        final String price = getFavouritePayload.getProductPrice();
        final String totalItems = String.valueOf(getFavouritePayload.getProductQty());
        final String imageUri = getFavouritePayload.getImageUrl();
        final String totalPrice = getFavouritePayload.getTotalPrice();

        holder.btnAddToCartFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                databaseHelper.insert(userId,product_id,nameOfFood,price,totalItems,imageUri,totalPrice);

                holder.btnAddToCartFavourites.setVisibility(View.GONE);
                holder.linearInvisible.setVisibility(View.VISIBLE);

                int counter = databaseHelper.getProductsCount();

                //tvCounter.setVisibility(View.VISIBLE);

                //tvCounter.setText(String.valueOf(counter));
                nearby.setBadgeCount(counter);

            }
        });


        holder.btnProceedToCheckOutFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                favouriteClickListenerToCart.GoToCartActivity(v);

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteClickListener.onDelete(v,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return getFavouritePayloadArrayList.size();
    }

    public class FavouritesHolder extends RecyclerView.ViewHolder {

        private Button btnAddToCartFavourites,btnProceedToCheckOutFavourites;
        private TextView tvFoodNameFavourites,tvPriceItemFavourties,tvAddFavouritesDate,tvStoreName;
        private ImageView cartImageViewFavourites;
        private LinearLayout linearInvisible;
        private Button btnDelete;


        public FavouritesHolder(View itemView) {
            super(itemView);

            //Button
            btnAddToCartFavourites = itemView.findViewById(R.id.btnAddToCartFavourites);
            btnProceedToCheckOutFavourites = itemView.findViewById(R.id.btnProceedToCheckOutFavourites);


            //Text views
            tvFoodNameFavourites = itemView.findViewById(R.id.tvFoodNameFavourites);
            tvPriceItemFavourties = itemView.findViewById(R.id.tvPriceItemFavourties);
            tvAddFavouritesDate = itemView.findViewById(R.id.tvAddFavouritesDate);
            tvStoreName = itemView.findViewById(R.id.tvStoreName);

            //Image views
            cartImageViewFavourites = itemView.findViewById(R.id.cartImageViewFavourites);


            //Linear layout
            linearInvisible = itemView.findViewById(R.id.linearInvisible);


            //Button
            btnDelete = itemView.findViewById(R.id.btnDelete);




        }
    }
}
