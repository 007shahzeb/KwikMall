package mall.kwik.kwikmall.apiresponse.GetOrderListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class GetOrderListSuccess {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("payload")
    @Expose
    private List<GetOrderListPayload> payload = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<GetOrderListPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<GetOrderListPayload> payload) {
        this.payload = payload;
    }


}
