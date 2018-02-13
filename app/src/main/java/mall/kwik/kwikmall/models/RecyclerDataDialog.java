package mall.kwik.kwikmall.models;

/**
 * Created by dharamveer on 24/11/17.
 */

public class RecyclerDataDialog  {


    public String itemName;
    public String quantity;

    public RecyclerDataDialog(String itemName, String quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
