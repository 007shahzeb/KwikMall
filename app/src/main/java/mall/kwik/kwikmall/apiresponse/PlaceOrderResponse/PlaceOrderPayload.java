package mall.kwik.kwikmall.apiresponse.PlaceOrderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderPayload {
    @Expose
    @SerializedName("OrderNo")
    private String OrderNo;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }
}
