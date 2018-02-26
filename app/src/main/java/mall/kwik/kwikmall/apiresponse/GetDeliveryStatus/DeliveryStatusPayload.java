package mall.kwik.kwikmall.apiresponse.GetDeliveryStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryStatusPayload {
    @Expose
    @SerializedName("deliveryStatus")
    private int deliveryStatus;

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
