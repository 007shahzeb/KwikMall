package mall.kwik.kwikmall.apiresponse.SendCategoriesResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dharamveer on 1/1/18.
 */

public class SendCatToSuccess {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("payload")
    @Expose
    private List<SendCatToPayload> payload = null;

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

    public List<SendCatToPayload> getPayload() {
        return payload;
    }

    public void setPayload(List<SendCatToPayload> payload) {
        this.payload = payload;
    }


}
