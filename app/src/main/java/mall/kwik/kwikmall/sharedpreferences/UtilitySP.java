package mall.kwik.kwikmall.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dharamveer on 22/12/17.
 */

public class UtilitySP {

    private SharedPreferences sharedPreferences;

    public static final String PREFERENCE_NAME = "SaveLocAddress";

    private String data;
    private String cityName,address;
    Context context;
    private int checkedId;

    public UtilitySP(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        this.context = context;
    }

    public int getCheckedId() {
        checkedId = sharedPreferences.getInt("checkedId",checkedId);

        return checkedId;
    }

    public void setCheckedId(int checkedId) {
        this.checkedId = checkedId;
        sharedPreferences.edit().putInt("checkedId",checkedId).commit();

    }

    public String getCityName() {
        cityName = sharedPreferences.getString("cityName",cityName);

        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
        sharedPreferences.edit().putString("cityName",cityName).commit();

    }

    public String getAddress() {
        address = sharedPreferences.getString("address",address);

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        sharedPreferences.edit().putString("address",address).commit();

    }



    public String getData() {
        data = sharedPreferences.getString("data",data);

        return data;
    }

    public void setData(String data) {
        this.data = data;
        sharedPreferences.edit().putString("data",data).commit();

    }
}
