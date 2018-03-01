package mall.kwik.kwikmall.adapters;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import mall.kwik.kwikmall.apiresponse.RestaurantsListSuccess.RestaurantsListPayload;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.sharedpreferences.MarkerDataPreference;

/**
 * Created by dharamveer on 20/12/17.
 */

public class AdapterRestaurantsFragment extends RecyclerView.Adapter<AdapterRestaurantsFragment.MyViewHolder> {

    List<RestaurantsListPayload> restaurantsListPayloadList = Collections.emptyList();
    Context context;
    View view;
    String string_form;
    String string_temp;
    private static AdapterRestaurantsFragment.ClickListener clickListener;
    private static AdapterRestaurantsFragment.PhoneClickLisetener phoneClickLisetener;



    public interface ClickListener {
        void onItemClick(String string_form, String nameOfHotel, String address,int StoreId, View v);
        void onItemLongClick( String nameOfHotel, String address,int StoreId, View v);
    }

    public interface PhoneClickLisetener {
        void onPhoneTextClick( String phone_no);
    }


    public void setOnPhoneClickListener(AdapterRestaurantsFragment.PhoneClickLisetener phoneClickListener){

        AdapterRestaurantsFragment.phoneClickLisetener = phoneClickListener;
    }

    public void setOnItemClickListener(AdapterRestaurantsFragment.ClickListener clickListener) {
        AdapterRestaurantsFragment.clickListener = clickListener;

    }



    public AdapterRestaurantsFragment(List<RestaurantsListPayload> restaurantsListPayloadList, Context context) {
        this.restaurantsListPayloadList = restaurantsListPayloadList;
        this.context = context;

    }

    @Override
    public AdapterRestaurantsFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restaurants_activity, parent, false);

        return new AdapterRestaurantsFragment.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterRestaurantsFragment.MyViewHolder holder, int position) {

        final RestaurantsListPayload restaurantsListPayload = restaurantsListPayloadList.get(position);



        if(restaurantsListPayload.getCatType().equalsIgnoreCase("Non-veg")){
            holder.imageVedNonVeg.setBackgroundResource(R.drawable.nonveg);
        }
        else {
            holder.imageVedNonVeg.setBackgroundResource(R.drawable.veg);

        }


        double lat = Double.parseDouble(restaurantsListPayload.getLattitude());
        double longi = Double.parseDouble(restaurantsListPayload.getLongitude());

        MarkerDataPreference markerData = new MarkerDataPreference(view.getContext());



        markerData.setShopAddLat(String.valueOf(lat));
        markerData.setShopAddLong(String.valueOf(longi));

        Location location1 = new Location("");
        //currentlocation lat long

        location1.setLatitude(lat);
        location1.setLongitude(longi);

        Location location2 = new Location("");
        location2.setLatitude(30.9755037);
        location2.setLongitude(76.5248561);

        float distanceInMeters = location1.distanceTo(location2);

        //For example spead is 10 meters per minute.
        int speedIs10MetersPerMinute = 180;
        float estimatedDriveTimeInMinutes =distanceInMeters / speedIs10MetersPerMinute;

        string_temp = new Double(estimatedDriveTimeInMinutes).toString();
        string_form = string_temp.substring(0,string_temp.indexOf('.'));



        holder.tvDistance.setText(string_form);

        holder.rowClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickListener.onItemClick(holder.tvDistance.getText().toString(),restaurantsListPayload.getName(),restaurantsListPayload.getAddress(),restaurantsListPayload.getId(), v);
            }
        });



        holder.tvHotelname.setText(restaurantsListPayload.getName());
        holder.tvAddressHotel.setText(restaurantsListPayload.getAddress());
        holder.tvPhoneNo.setText(restaurantsListPayload.getPhoneNo());

        final String phone_no= holder.tvPhoneNo.getText().toString().replaceAll("-", "");


        Picasso.with(view.getContext())
                .load("http://employeelive.com/kwiqmall/SuperAdmin/img/stores/" + restaurantsListPayload.getImage())
                .error(R.drawable.errortriangle)
                .into(holder.imageViewRestaurants);


        holder.tvPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneClickLisetener.onPhoneTextClick(phone_no);
            }
        });



        holder.rowClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickListener.onItemLongClick(restaurantsListPayload.getName(),restaurantsListPayload.getAddress(),restaurantsListPayload.getId(), v);

                return false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return restaurantsListPayloadList.size();        }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvHotelname,tvAddressHotel,tvDiscount,tvRating,tvDistance,tvPhoneNo;
        private ImageView imageViewRestaurants;
        private ImageView imageVedNonVeg;
        private LinearLayout rowClick;

        public MyViewHolder(View itemView) {
            super(itemView);



            rowClick = itemView.findViewById(R.id.rowClick);


            tvHotelname = itemView.findViewById(R.id.tvHotelname);
            tvAddressHotel = itemView.findViewById(R.id.tvAddressHotel);
            //  tvDiscount = itemView.findViewById(R.id.tvDiscount);
            // tvRating = itemView.findViewById(R.id.tvRating);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvPhoneNo = itemView.findViewById(R.id.tvPhoneNo);

            //Image views
            imageViewRestaurants = itemView.findViewById(R.id.imageViewRestaurants);
            imageVedNonVeg = itemView.findViewById(R.id.imageVedNonVeg);





        }

    }
}

