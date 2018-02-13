package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.BaseFragActivity.BaseActivity;
import mall.kwik.kwikmall.apiresponse.AddFavourites.AddFavouritesSuccess;
import mall.kwik.kwikmall.fragments.ViewCartFragment;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;
import mall.kwik.kwikmall.sharedpreferences.UtilityCartData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mall.kwik.kwikmall.activities.FragmentsActivity.nearby;


public class FoodDetailsActivity extends BaseActivity implements View.OnClickListener {

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
    private Context context;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        findIds();

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();


        userId = sharedPrefsHelper.get(AppConstants.USER_ID,0);

        nameofItem  = getIntent().getExtras().getString("nameOfFood");
        nameofhotel  = getIntent().getExtras().getString("nameofhotel");
        price  = getIntent().getExtras().getString("price");
        imageUri  = getIntent().getExtras().getString("imageUri");
        description  = getIntent().getExtras().getString("description");
        cart_id  = getIntent().getExtras().getInt("position");
        product_id  = getIntent().getExtras().getInt("productid");


        Picasso.with(this)
                .load(imageUri)
                .fit()
                .error(R.drawable.errortriangle)
                .into(imageFood);

        databaseHelper = new DBHelper(this);


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

        if(LoadButtonState()==product_id){

            button_favorite.setLiked(true);
        }
        else {
            button_favorite.setLiked(false);
        }

        button_favorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                if(LoadButtonState()==product_id){

                    Toast.makeText(FoodDetailsActivity.this,"Already added",Toast.LENGTH_SHORT).show();

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
        stringStringHashMap.put("imageUrl",imageUri);
        stringStringHashMap.put("bussinessName",nameofhotel);


        compositeDisposable.add(apiService.addFavourites(stringStringHashMap)
                        .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AddFavouritesSuccess>() {
                    @Override
                    public void accept(AddFavouritesSuccess addFavouritesSuccess) throws Exception {

                        if(addFavouritesSuccess.getSuccess()){


                            SaveButtonState(product_id);
                            Toast.makeText(FoodDetailsActivity.this,"Added to favourites",Toast.LENGTH_SHORT).show();
                        }
                        else {

                        showAlertDialog("Retry",addFavouritesSuccess.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showAlertDialog("Retry",throwable.getMessage());

                    }
                }));


/*
        restClient.addFavourites(stringStringHashMap).enqueue(new Callback<AddFavouritesSuccess>() {
            @Override
            public void onResponse(Call<AddFavouritesSuccess> call, Response<AddFavouritesSuccess> response) {

                if(response.body().getSuccess()){


                    SaveButtonState(product_id);
                    Toast.makeText(FoodDetailsActivity.this,"Added to favourites",Toast.LENGTH_SHORT).show();
                }
                else {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(FoodDetailsActivity.this);
                    builder1.setMessage(response.body().getMessage());
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

            }

            @Override
            public void onFailure(Call<AddFavouritesSuccess> call, Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(FoodDetailsActivity.this);
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

    public void SaveButtonState(int product_id){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FoodDetailsActivity.this);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("product_id",product_id);
        edit.commit();
    }

    public int LoadButtonState(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int buttonState = preferences.getInt("product_id", 0);
        return buttonState;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
        compositeDisposable.dispose();

    }


    private void findIds() {
        imageBackArrow = findViewById(R.id.imageBackArrow);
        imageFood = findViewById(R.id.imageFood);

        btnAddtoCart = findViewById(R.id.btnAddtoCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        txtCount = findViewById(R.id.txtCount);
        btnInc = findViewById(R.id.btnInc);
        btnDec = findViewById(R.id.btnDec);

        txtCountItems = findViewById(R.id.txtCountItems);
        txtTotatPrice = findViewById(R.id.txtTotatPrice);
        txtViewCart = findViewById(R.id.txtViewCart);

        tvPrice = findViewById(R.id.tvPrice);
        tvHotelNameFoodDetailsTopbar = findViewById(R.id.tvHotelNameFoodDetailsTopbar);

        layoutOverlay = findViewById(R.id.layoutOverlay);
        layoutInvisible = findViewById(R.id.layoutInvisible);

        txtNameofItem = findViewById(R.id.txtNameofItem);
        txtdescription = findViewById(R.id.txtdescription);

        button_favorite =findViewById(R.id.button_favorite);



    }


    @Override
    public void onClick(View v) {

        if(v==txtViewCart){


            Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
            a.reset();
            txtViewCart.clearAnimation();
            txtViewCart.startAnimation(a);

            String nameOfFood = txtNameofItem.getText().toString();

            UtilityCartData utilityCartData = new UtilityCartData(FoodDetailsActivity.this);
            utilityCartData.setProductid(product_id);
            utilityCartData.setCart_id(cart_id);
            utilityCartData.setNameOfFood(nameOfFood);
            utilityCartData.setPrice(price);
            utilityCartData.setTotalItems(totalItems);
            utilityCartData.setImageUri(imageUri);
            utilityCartData.setTotalPrice(totalPrice);


            databaseHelper.insert(userId,product_id,nameOfFood,price,totalItems,imageUri,totalPrice);

            int counter = databaseHelper.getProductsCount();

            nearby.setBadgeCount(counter);

            Fragment viewCartFragment = new ViewCartFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.foodDetailsContainer, viewCartFragment, "viewCartFragment");
            fragmentTransaction.addToBackStack("viewCartFragment");
            fragmentTransaction.commit();


            overridePendingTransition(R.anim.slide_in, R.anim.nothing);


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

            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }

        if(v==btnAddtoCart){

            MaterialRippleLayout.on(btnAddtoCart)
                    .rippleColor(Color.parseColor("#006400"))
                    .rippleAlpha(0.5f)
                    .rippleHover(true)
                    .create();

            layoutOverlay.setVisibility(View.VISIBLE);
            Animation pushUpIn = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
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
            Animation pushUpIn = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
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

            UtilityCartData utilityCartData = new UtilityCartData(FoodDetailsActivity.this);
            utilityCartData.setProductid(product_id);
            utilityCartData.setCart_id(cart_id);
            utilityCartData.setNameOfFood(nameOfFood);
            utilityCartData.setPrice(price);
            utilityCartData.setTotalItems(totalItems);
            utilityCartData.setImageUri(imageUri);
            utilityCartData.setTotalPrice(totalPrice);


            databaseHelper.insert(userId,product_id,nameOfFood,price,totalItems,imageUri,totalPrice);


            int counter = databaseHelper.getProductsCount();

            nearby.setBadgeCount(counter);


            Fragment viewCartFragment = new ViewCartFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.foodDetailsContainer, viewCartFragment, "viewCartFragment");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

           /* Fragment mFragment = null;
            mFragment = new ViewCartFragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.mainFrame, mFragment).commit();

*/



        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        layoutOverlay.setVisibility(View.INVISIBLE);

        Animation pushUpIn = AnimationUtils.loadAnimation(this, R.anim.push_up_in);

        layoutInvisible.startAnimation(pushUpIn);


        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


    }


}
