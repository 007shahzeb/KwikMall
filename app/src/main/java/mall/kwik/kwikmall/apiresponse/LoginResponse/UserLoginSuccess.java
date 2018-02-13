package mall.kwik.kwikmall.apiresponse.LoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginSuccess {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("payload")
    @Expose
    private LoginPayload payload;

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

    public LoginPayload getPayload() {
        return payload;
    }

    public void setPayload(LoginPayload payload) {
        this.payload = payload;
    }

}