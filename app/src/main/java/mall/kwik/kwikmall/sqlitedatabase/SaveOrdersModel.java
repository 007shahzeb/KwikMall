package mall.kwik.kwikmall.sqlitedatabase;

/**
 * Created by dharamveer on 20/12/17.
 */

public class SaveOrdersModel {

    public int KEY_ID_ORDERS;
    public int User_id_ORDERS;
    public String KEY_name_ORDERS;
    public String KEY_price_ORDERS ;
    public String KEY_qty_ORDERS ;
    public String KEY_ImageUri_ORDERS ;
    public String KEY_TotalPrice_ORDERS ;

    public int getKEY_ID_ORDERS() {
        return KEY_ID_ORDERS;
    }

    public void setKEY_ID_ORDERS(int KEY_ID_ORDERS) {
        this.KEY_ID_ORDERS = KEY_ID_ORDERS;
    }

    public int getUser_id_ORDERS() {
        return User_id_ORDERS;
    }

    public void setUser_id_ORDERS(int user_id_ORDERS) {
        User_id_ORDERS = user_id_ORDERS;
    }

    public String getKEY_name_ORDERS() {
        return KEY_name_ORDERS;
    }

    public void setKEY_name_ORDERS(String KEY_name_ORDERS) {
        this.KEY_name_ORDERS = KEY_name_ORDERS;
    }

    public String getKEY_price_ORDERS() {
        return KEY_price_ORDERS;
    }

    public void setKEY_price_ORDERS(String KEY_price_ORDERS) {
        this.KEY_price_ORDERS = KEY_price_ORDERS;
    }

    public String getKEY_qty_ORDERS() {
        return KEY_qty_ORDERS;
    }

    public void setKEY_qty_ORDERS(String KEY_qty_ORDERS) {
        this.KEY_qty_ORDERS = KEY_qty_ORDERS;
    }

    public String getKEY_ImageUri_ORDERS() {
        return KEY_ImageUri_ORDERS;
    }

    public void setKEY_ImageUri_ORDERS(String KEY_ImageUri_ORDERS) {
        this.KEY_ImageUri_ORDERS = KEY_ImageUri_ORDERS;
    }

    public String getKEY_TotalPrice_ORDERS() {
        return KEY_TotalPrice_ORDERS;
    }

    public void setKEY_TotalPrice_ORDERS(String KEY_TotalPrice_ORDERS) {
        this.KEY_TotalPrice_ORDERS = KEY_TotalPrice_ORDERS;
    }
}
