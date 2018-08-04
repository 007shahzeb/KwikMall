package mall.kwik.kwikmall.apiresponse.FilterListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dharamveer on 29/12/17.
 */

public class FilterListPayload {




    private int mType;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;


    public   boolean isSeleted;



    public FilterListPayload(String name) {
        this.name = name;
    }

    public int getType() {
        return mType;
    }
    public void setType(int type) {
        this.mType = type;
    }




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
