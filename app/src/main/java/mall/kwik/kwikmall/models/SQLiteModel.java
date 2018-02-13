package mall.kwik.kwikmall.models;

/**
 * Created by dharamveer on 27/11/17.
 */

public class SQLiteModel {

    String nameOfItem;
    String quantity;
    String priceItems;
    String totalPrice;

    public String getNameOfItem() {
        return nameOfItem;
    }

    public void setNameOfItem(String nameOfItem) {
        this.nameOfItem = nameOfItem;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPriceItems() {
        return priceItems;
    }

    public void setPriceItems(String priceItems) {
        this.priceItems = priceItems;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
