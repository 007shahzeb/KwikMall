package mall.kwik.kwikmall.apiresponse.GetAllProductsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAllProductsPayload {
    @Expose
    @SerializedName("selling_price")
    private float selling_price;
    @Expose
    @SerializedName("phoneNo")
    private String phoneNo;
    @Expose
    @SerializedName("bussinessName")
    private String bussinessName;
    @Expose
    @SerializedName("bussinessId")
    private int bussinessId;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("discount")
    private String discount;
    @Expose
    @SerializedName("quantity")
    private int quantity;
    @Expose
    @SerializedName("price")
    private String price;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("catagoryType")
    private String catagoryType;
    @Expose
    @SerializedName("subId")
    private int subId;
    @Expose
    @SerializedName("id")
    private int id;

    public float getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(float selling_price) {
        this.selling_price = selling_price;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getBussinessName() {
        return bussinessName;
    }

    public void setBussinessName(String bussinessName) {
        this.bussinessName = bussinessName;
    }

    public int getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(int bussinessId) {
        this.bussinessId = bussinessId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatagoryType() {
        return catagoryType;
    }

    public void setCatagoryType(String catagoryType) {
        this.catagoryType = catagoryType;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
