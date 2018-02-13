package mall.kwik.kwikmall.apiresponse.GetAllProductsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllProductsSuccess {


    @Expose
    @SerializedName("payload")
    private List<GetAllProductsPayload> payload;
    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("success")
    private boolean success;

    public List<GetAllProductsPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<GetAllProductsPayload> payload) {
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