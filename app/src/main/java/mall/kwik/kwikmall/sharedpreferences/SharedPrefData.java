package mall.kwik.kwikmall.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dharamveer on 15/12/17.
 */

public class SharedPrefData {


    Context context;
    static SharedPreferences sharedPreferences;
    private String nameOfHotel;
    private String address;
    private String string_form;


    public SharedPrefData(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("searchItem", Context.MODE_PRIVATE);;

    }


    public String getNameOfHotel() {
        nameOfHotel = sharedPreferences.getString("nameOfHotel",nameOfHotel);

        return nameOfHotel;
    }

    public void setNameOfHotel(String nameOfHotel) {
        this.nameOfHotel = nameOfHotel;
        sharedPreferences.edit().putString("nameOfHotel",nameOfHotel).commit();

    }

    public String getAddress() {
        address = sharedPreferences.getString("address",address);

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        sharedPreferences.edit().putString("address",address).commit();

    }

    public String getString_form() {
        string_form = sharedPreferences.getString("string_form",string_form);

        return string_form;
    }

    public void setString_form(String string_form) {
        this.string_form = string_form;
        sharedPreferences.edit().putString("string_form",string_form).commit();

    }


}
