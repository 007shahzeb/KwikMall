package mall.kwik.kwikmall.apiresponse.GetFavouriteResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFavouriteSuccess {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("payload")
    @Expose
    private List<GetFavouritePayload> payload = null;

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

    public List<GetFavouritePayload> getPayload() {
        return payload;
    }

    public void setPayload(List<GetFavouritePayload> payload) {
        this.payload = payload;
    }

}
