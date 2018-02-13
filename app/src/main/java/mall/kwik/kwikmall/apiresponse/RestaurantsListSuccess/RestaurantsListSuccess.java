package mall.kwik.kwikmall.apiresponse.RestaurantsListSuccess;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantsListSuccess {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("payload")
    @Expose
    private List<RestaurantsListPayload> payload = null;

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

    public List<RestaurantsListPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<RestaurantsListPayload> payload) {
        this.payload = payload;
    }

}