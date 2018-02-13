package mall.kwik.kwikmall.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dharamveer on 29/11/17.
 */

public class UtilityCartData {


    Context context;
    static SharedPreferences sharedPreferences;




    private int RestId,noOfOrders;
    private int cart_id;
    private int productid;
    private String nameOfFood;
    private String price;
    private String totalItems;
    private String imageUri;
    private String totalPrice;
    private int sumTotal;


    public UtilityCartData(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("userInfoUtilityRestaurants", Context.MODE_PRIVATE);;
    }

    public int getProductid() {
        productid = sharedPreferences.getInt("productid",productid);

        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
        sharedPreferences.edit().putInt("productid",productid).commit();

    }

    public int getSumTotal() {
        sumTotal = sharedPreferences.getInt("sumTotal",sumTotal);
        return sumTotal;
    }

    public void setSumTotal(int sumTotal) {
        this.sumTotal = sumTotal;
        sharedPreferences.edit().putInt("sumTotal",sumTotal).commit();

    }

    public int getNoOfOrders() {
        noOfOrders = sharedPreferences.getInt("noOfOrders",noOfOrders);

        return noOfOrders;
    }

    public void setNoOfOrders(int noOfOrders) {
        this.noOfOrders = noOfOrders;
        sharedPreferences.edit().putInt("noOfOrders",noOfOrders).commit();

    }



    public int getCart_id() {
        cart_id = sharedPreferences.getInt("userId",cart_id);

        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
        sharedPreferences.edit().putInt("cart_id",cart_id).commit();

    }

    public String getNameOfFood() {
        nameOfFood = sharedPreferences.getString("nameOfFood",nameOfFood);

        return nameOfFood;
    }

    public void setNameOfFood(String nameOfFood) {
        this.nameOfFood = nameOfFood;
        sharedPreferences.edit().putString("nameOfFood",nameOfFood).commit();

    }

    public String getPrice() {
        price = sharedPreferences.getString("price",price);

        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        sharedPreferences.edit().putString("price",price).commit();

    }

    public String getTotalItems() {
        totalItems = sharedPreferences.getString("totalItems",totalItems);

        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
        sharedPreferences.edit().putString("totalItems",totalItems).commit();

    }

    public String getImageUri() {
        imageUri = sharedPreferences.getString("imageUri",imageUri);

        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
        sharedPreferences.edit().putString("imageUri",imageUri).commit();

    }

    public String getTotalPrice() {
        totalPrice = sharedPreferences.getString("userEmail",totalPrice);

        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
        sharedPreferences.edit().putString("totalPrice",totalPrice).commit();

    }
}
