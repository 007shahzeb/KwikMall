package mall.kwik.kwikmall.sqlitedatabase;

/**
 * Created by dharamveer on 4/12/17.
 */

public class CartModel {




    public int KEY_ID;
    public int User_id;
    public String KEY_name;
    public String KEY_price ;
    public String KEY_qty ;
    public String KEY_ImageUri ;
    public String KEY_TotalPrice ;


    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    public int getKEY_ID() {
        return KEY_ID;
    }

    public void setKEY_ID(int KEY_ID) {
        this.KEY_ID = KEY_ID;
    }



    public String getKEY_name() {
        return KEY_name;
    }

    public void setKEY_name(String KEY_name) {
        this.KEY_name = KEY_name;
    }

    public String getKEY_price() {
        return KEY_price;
    }

    public void setKEY_price(String KEY_price) {
        this.KEY_price = KEY_price;
    }

    public String getKEY_qty() {
        return KEY_qty;
    }

    public void setKEY_qty(String KEY_qty) {
        this.KEY_qty = KEY_qty;
    }

    public String getKEY_ImageUri() {
        return KEY_ImageUri;
    }

    public void setKEY_ImageUri(String KEY_ImageUri) {
        this.KEY_ImageUri = KEY_ImageUri;
    }

    public String getKEY_TotalPrice() {
        return KEY_TotalPrice;
    }

    public void setKEY_TotalPrice(String KEY_TotalPrice) {
        this.KEY_TotalPrice = KEY_TotalPrice;
    }
}
