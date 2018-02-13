package mall.kwik.kwikmall.apiresponse.FilterListResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterListSuccess {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("payload")
    @Expose
    private List<FilterListPayload> payload = null;

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

    public List<FilterListPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<FilterListPayload> payload) {
        this.payload = payload;
    }

}