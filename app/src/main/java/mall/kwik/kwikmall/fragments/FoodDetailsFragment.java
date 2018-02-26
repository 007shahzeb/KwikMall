package mall.kwik.kwikmall.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.baseFragActivity.BaseFragment;
import mall.kwik.kwikmall.apiresponse.AddFavourites.AddFavouritesSuccess;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.UtilityCartData;

import static mall.kwik.kwikmall.activities.FragmentsActivity.nearby;

/**
 * Created by dharamveer on 8/1/18.
 */

public class FoodDetailsFragment extends BaseFragment implements View.OnClickListener {

    private View view;

    private ImageView imageFood,imageBackArrow;
    private TextView txtNameofItem,tvPrice,txtCount,txtCountItems,txtTotatPrice,txtViewCart,txtdescription;
    private Button btnAddtoCart,btnBuyNow,btnInc,btnDec;
    private   int count=1;
    private RelativeLayout layoutOverlay,layoutInvisible;

    String imageUri,totalItems,totalPrice,description,nameofhotel;
    int cart_id=0;
    int product_id=0;
    DBHelper databaseHelper;
    int userId;
    private String nameofItem,price;
    private LikeButton button_favorite;
    private TextView tvHotelNameFoodDetailsTopbar;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    String finalImageUri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_food_details, container, false);

        findIds();


        userId = sharedPrefsHelper.get(AppConstants.USER_ID,0);

        Bundle bundle = this.getArguments();

        if(bundle!=null){

            nameofItem = bundle.getString("nameOfFood");
            nameofhotel = bundle.getString("nameofhotel");
            price = bundle.getString("price");
            imageUri = bundle.getString("imageUri");
            description =bundle.getString("description");
            cart_id = bundle.getInt("position");
            product_id = bundle.getInt("productid");


        }


        if(imageUri.contains("http")){

            Picasso.with(getActivity())
                    .load(imageUri)
                    .fit()
                    .error(R.drawable.errortriangle)
                    .into(imageFood);


             finalImageUri = imageUri;


        }
        else {

            Picasso.with(getActivity())
                    .load("http://employeelive.com/kwiqmall/SuperAdmin/img/products/"+imageUri)
                    .fit()
                    .error(R.drawable.errortriangle)
                    .into(imageFood);


            finalImageUri = "http://employeelive.com/kwiqmall/SuperAdmin/img/products/"+imageUri;
        }


        databaseHelper = new DBHelper(getActivity());


        txtNameofItem.setText(nameofItem);
        tvPrice.setText(price);
        txtdescription.setText(description);
        tvHotelNameFoodDetailsTopbar.setText(nameofhotel);


        imageBackArrow.setOnClickListener(this);

        btnAddtoCart.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);

        btnInc.setOnClickListener(this);
        btnDec.setOnClickListener(this);

        txtViewCart.setOnClickListener(this);

        if (LoadButtonState() == product_id) {

            button_favorite.setLiked(true);
        } else {
            button_favorite.setLiked(false);
        }

        button_favorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                if (LoadButtonState() == product_id) {

                    Toast.makeText(getActivity(), "Already added", Toast.LENGTH_SHORT).show();

                }


                AddToFavouritesListApi();


            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
        button_favorite.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {

            }
        });

        return view;
    }


    private void AddToFavouritesListApi() {


        String totalItems1 = txtCount.getText().toString();

        String totalPrice2 = String.valueOf(Integer.parseInt(price) * Integer.parseInt(totalItems1));


        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("userId", String.valueOf(userId));
        stringStringHashMap.put("productId", String.valueOf(product_id));
        stringStringHashMap.put("productName",nameofItem);
        stringStringHashMap.put("productPrice",price);
        stringStringHashMap.put("productQty",totalItems1);
        stringStringHashMap.put("totalPrice",totalPrice2);
        stringStringHashMap.put("imageUrl",finalImageUri);
        stringStringHashMap.put("bussinessName",nameofhotel);


        compositeDisposable.add(apiService.addFavourites(stringStringHashMap)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AddFavouritesSuccess>() {
                    @Override
                    public void accept(AddFavouritesSuccess addFavouritesSuccess) throws Exception {

                        if(addFavouritesSuccess.getSuccess()){

                            SaveButtonState(product_id);
                            Toast.makeText(getActivity(),"Added to favourites",Toast.LENGTH_SHORT).show();
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

    public void SaveButtonState(int product_id){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("product_id",product_id);
        edit.commit();
    }

    public int LoadButtonState(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int buttonState = preferences.getInt("product_id", 0);
        return buttonState;
    }


    private void findIds() {
        imageBackArrow = view.findViewById(R.id.imageBackArrow);
        imageFood = view.findViewById(R.id.imageFood);

        btnAddtoCart = view.findViewById(R.id.btnAddtoCart);
        btnBuyNow = view.findViewById(R.id.btnBuyNow);

        txtCount = view.findViewById(R.id.txtCount);
        btnInc = view.findViewById(R.id.btnInc);
        btnDec = view.findViewById(R.id.btnDec);

        txtCountItems = view.findViewById(R.id.txtCountItems);
        txtTotatPrice = view.findViewById(R.id.txtTotatPrice);
        txtViewCart = view.findViewById(R.id.txtViewCart);

        tvPrice = view.findViewById(R.id.tvPrice);
        tvHotelNameFoodDetailsTopbar = view.findViewById(R.id.tvHotelNameFoodDetailsTopbar);

        layoutOverlay = view.findViewById(R.id.layoutOverlay);
        layoutInvisible = view.findViewById(R.id.layoutInvisible);

        txtNameofItem = view.findViewById(R.id.txtNameofItem);
        txtdescription = view.findViewById(R.id.txtdescription);

        button_favorite =view.findViewById(R.id.button_favorite);



    }


    @Override
    public void onClick(View v) {

        if(v==txtViewCart){


            Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
            a.reset();
            txtViewCart.clearAnimation();
            txtViewCart.startAnimation(a);

            String nameOfFood = txtNameofItem.getText().toString();

            UtilityCartData utilityCartData = new UtilityCartData(getActivity());
            utilityCartData.setProductid(product_id);
            utilityCartData.setCart_id(cart_id);
            utilityCartData.setNameOfFood(nameOfFood);
            utilityCartData.setPrice(price);
            utilityCartData.setTotalItems(totalItems);
            utilityCartData.setImageUri(finalImageUri);
            utilityCartData.setTotalPrice(totalPrice);


            databaseHelper.insert(userId,product_id,nameOfFood,price,totalItems,finalImageUri,totalPrice);

            int counter = databaseHelper.getProductsCount();

            nearby.setBadgeCount(counter);

            Fragment viewCartFragment = new ViewCartFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFrame, viewCartFragment, "viewCartFragment");
            fragmentTransaction.addToBackStack("viewCartFragment");
            fragmentTransaction.commit();


            getActivity().overridePendingTransition(R.anim.slide_in, R.anim.nothing);


        }


        if(v==btnInc){

            count++;
            txtCount.setText(String.valueOf(count));

        }
        if(v==btnDec){

            if(count<1)
            {
                count=1;
                txtCount.setText("");

            }
            if(count>1)
            {
                count--;
                txtCount.setText(String.valueOf(count));
            }

        }

        if(v==imageBackArrow){


            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                fm.popBackStack();
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");
                super.getActivity().onBackPressed();
            }

          //  getActivity().finish();
            layoutOverlay.setVisibility(View.INVISIBLE);

            Animation pushUpIn = AnimationUtils.loadAnimation(getActivity(), R.anim.push_up_in);

            layoutInvisible.startAnimation(pushUpIn);

            getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }

        if(v==btnAddtoCart){

            MaterialRippleLayout.on(btnAddtoCart)
                    .rippleColor(Color.parseColor("#006400"))
                    .rippleAlpha(0.5f)
                    .rippleHover(true)
                    .create();

            layoutOverlay.setVisibility(View.VISIBLE);
            Animation pushUpIn = AnimationUtils.loadAnimation(getActivity(), R.anim.push_up_in);
            layoutOverlay.startAnimation(pushUpIn);

            /*----------------------------------------- */

            price = tvPrice.getText().toString();
            totalItems = txtCount.getText().toString();

            totalPrice = String.valueOf(Integer.parseInt(price) * Integer.parseInt(totalItems));

            txtCountItems.setText(totalItems);

            txtTotatPrice.setText(totalPrice);

            /*----------------------------------------- */

            btnBuyNow.setBackgroundColor(Color.WHITE);
            btnBuyNow.setTextColor(Color.parseColor("#00d048"));
            btnAddtoCart.setTextColor(Color.WHITE);
            btnAddtoCart.setBackgroundColor(Color.parseColor("#00d048"));

        }

        if(v==btnBuyNow){



            layoutOverlay.setVisibility(View.VISIBLE);
            Animation pushUpIn = AnimationUtils.loadAnimation(getActivity(), R.anim.push_up_in);
            layoutOverlay.startAnimation(pushUpIn);

            /*----------------------------------------- */

            price = tvPrice.getText().toString();
            totalItems = txtCount.getText().toString();

            totalPrice = String.valueOf(Integer.parseInt(price) * Integer.parseInt(totalItems));

            txtCountItems.setText(totalItems);

            txtTotatPrice.setText(totalPrice);

            btnAddtoCart.setBackgroundColor(Color.WHITE);
            btnAddtoCart.setTextColor(Color.parseColor("#00d048"));
            btnBuyNow.setTextColor(Color.WHITE);
            btnBuyNow.setBackgroundColor(Color.parseColor("#00d048"));


            String nameOfFood = txtNameofItem.getText().toString();

            UtilityCartData utilityCartData = new UtilityCartData(getActivity());
            utilityCartData.setProductid(product_id);
            utilityCartData.setCart_id(cart_id);
            utilityCartData.setNameOfFood(nameOfFood);
            utilityCartData.setPrice(price);
            utilityCartData.setTotalItems(totalItems);
            utilityCartData.setImageUri(finalImageUri);
            utilityCartData.setTotalPrice(totalPrice);


            databaseHelper.insert(userId,product_id,nameOfFood,price,totalItems,finalImageUri,totalPrice);


            int counter = databaseHelper.getProductsCount();

            nearby.setBadgeCount(counter);


            Fragment viewCartFragment = new ViewCartFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainFrame, viewCartFragment, "viewCartFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();



        }
    }



}
