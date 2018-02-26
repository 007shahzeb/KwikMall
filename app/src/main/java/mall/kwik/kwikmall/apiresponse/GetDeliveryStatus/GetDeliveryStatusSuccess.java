package mall.kwik.kwikmall.apiresponse.GetDeliveryStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dharamveer on 19/2/18.
 */

public class GetDeliveryStatusSuccess {


    @Expose
    @SerializedName("payload")
    private DeliveryStatusPayload payload;
    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("success")
    private boolean success;

    public DeliveryStatusPayload getPayload() {
        return payload;
    }

    public void setPayload(DeliveryStatusPayload payload) {
        this.payload = payload;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
