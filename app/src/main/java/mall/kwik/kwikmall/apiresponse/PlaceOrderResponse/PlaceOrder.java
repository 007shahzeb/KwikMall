package mall.kwik.kwikmall.apiresponse.PlaceOrderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dharamveer on 8/12/17.
 */

public class PlaceOrder {


    @Expose
    @SerializedName("payload")
    private PlaceOrderPayload payload;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("success")
    private boolean success;

    public PlaceOrderPayload getPayload() {
        return payload;
    }

    public void setPayload(PlaceOrderPayload payload) {
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

