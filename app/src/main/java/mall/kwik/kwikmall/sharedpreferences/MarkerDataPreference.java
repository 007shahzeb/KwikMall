package mall.kwik.kwikmall.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dharamveer on 12/12/17.
 */

public class MarkerDataPreference {


    private final SharedPreferences sharedPreferences;

    String deliveryAddLat;
    String deliveryAddLong;
    public static final String PREFERENCE_NAME = "markerData";


    String shopAddLat;
    String shopAddLong;


    public MarkerDataPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        ;

    }

    public String getDeliveryAddLat() {
        deliveryAddLat = sharedPreferences.getString("deliveryAddLat",deliveryAddLat);

        return deliveryAddLat;
    }

    public void setDeliveryAddLat(String deliveryAddLat) {
        this.deliveryAddLat = deliveryAddLat;
        sharedPreferences.edit().putString("deliveryAddLat",deliveryAddLat).commit();

    }

    public String getDeliveryAddLong() {
        deliveryAddLong = sharedPreferences.getString("deliveryAddLong",deliveryAddLong);

        return deliveryAddLong;
    }

    public void setDeliveryAddLong(String deliveryAddLong) {
        this.deliveryAddLong = deliveryAddLong;
        sharedPreferences.edit().putString("deliveryAddLong",deliveryAddLong).commit();

    }

    public String getShopAddLat() {
        shopAddLat = sharedPreferences.getString("shopAddLat",shopAddLat);

        return shopAddLat;
    }

    public void setShopAddLat(String shopAddLat) {
        this.shopAddLat = shopAddLat;
        sharedPreferences.edit().putString("shopAddLat",shopAddLat).commit();

    }

    public String getShopAddLong() {
        shopAddLong = sharedPreferences.getString("shopAddLong",shopAddLong);

        return shopAddLong;
    }

    public void setShopAddLong(String shopAddLong) {
        this.shopAddLong = shopAddLong;
        sharedPreferences.edit().putString("shopAddLong",shopAddLong).commit();

    }
}