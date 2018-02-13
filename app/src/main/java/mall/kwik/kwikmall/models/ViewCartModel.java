package mall.kwik.kwikmall.models;

/**
 * Created by dharamveer on 27/11/17.
 */

public class ViewCartModel {


    String tvFoodName,tvSize,tvKgs,tvDiscountPrice,tvNormalPrice,tvOff;
    String imagePath,quantity;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ViewCartModel(String quantity,String tvFoodName, String tvSize, String tvKgs, String tvDiscountPrice, String tvNormalPrice, String tvOff,String imagePath) {
        this.tvFoodName = tvFoodName;
        this.tvSize = tvSize;
        this.tvKgs = tvKgs;
        this.tvDiscountPrice = tvDiscountPrice;
        this.tvNormalPrice = tvNormalPrice;
        this.tvOff = tvOff;
        this.imagePath = imagePath;
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTvFoodName() {
        return tvFoodName;
    }

    public void setTvFoodName(String tvFoodName) {
        this.tvFoodName = tvFoodName;
    }

    public String getTvSize() {
        return tvSize;
    }

    public void setTvSize(String tvSize) {
        this.tvSize = tvSize;
    }

    public String getTvKgs() {
        return tvKgs;
    }

    public void setTvKgs(String tvKgs) {
        this.tvKgs = tvKgs;
    }

    public String getTvDiscountPrice() {
        return tvDiscountPrice;
    }

    public void setTvDiscountPrice(String tvDiscountPrice) {
        this.tvDiscountPrice = tvDiscountPrice;
    }

    public String getTvNormalPrice() {
        return tvNormalPrice;
    }

    public void setTvNormalPrice(String tvNormalPrice) {
        this.tvNormalPrice = tvNormalPrice;
    }

    public String getTvOff() {
        return tvOff;
    }

    public void setTvOff(String tvOff) {
        this.tvOff = tvOff;
    }
}
