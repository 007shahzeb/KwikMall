package mall.kwik.kwikmall.apiresponse.DeleteOrderSuccess;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dharamveer on 23/2/18.
 */

public class DeleteOrderSuccess {


    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("error")
    private boolean error;
    @Expose
    @SerializedName("success")
    private boolean success;

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
