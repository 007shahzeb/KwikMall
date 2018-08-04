package mall.kwik.kwikmall.apiresponse.getOrderStatusAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseGetOrderStatus {

    @SerializedName("success")
    @Expose
    public Boolean success;

    @SerializedName("error")
    @Expose
    public Boolean error;

    @SerializedName("status")
    @Expose
    public Integer status;

    public String getMessage() {
        return message;
    }

    @SerializedName("message")
    @Expose
    public String message;

    public Boolean getSuccess() {
        return success;
    }

    public Boolean getError() {
        return error;
    }

    public Integer getStatus() {
        return status;
    }






//    @SerializedName("success")
//    @Expose
//    public Boolean success;
//    @SerializedName("error")
//    @Expose
//    public Boolean error;
//    @SerializedName("message")
//    @Expose
//    public String message;
//    @SerializedName("status")
//    @Expose
//    public Integer status;
//




}