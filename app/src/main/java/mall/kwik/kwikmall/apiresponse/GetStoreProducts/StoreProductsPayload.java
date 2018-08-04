package mall.kwik.kwikmall.apiresponse.GetStoreProducts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dharamveer on 30/11/17.
 */

public class StoreProductsPayload {



    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("subId")
    @Expose
    private Integer subId;

    @SerializedName("catagoryType")
    @Expose
    private String catagoryType;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("bussinessId")
    @Expose
    private Integer bussinessId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public String getCatagoryType() {
        return catagoryType;
    }

    public void setCatagoryType(String catagoryType) {
        this.catagoryType = catagoryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(Integer bussinessId) {
        this.bussinessId = bussinessId;
    }


}
