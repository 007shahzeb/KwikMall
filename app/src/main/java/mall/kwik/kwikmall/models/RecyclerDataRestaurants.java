package mall.kwik.kwikmall.models;

/**
 * Created by dharamveer on 23/11/17.
 */

public class RecyclerDataRestaurants {


    private String tvHotelname,tvItems,tvDiscount,tvRating,tvTime,tvPrice;


    public RecyclerDataRestaurants(String tvHotelname, String tvItems, String tvDiscount, String tvRating, String tvTime, String tvPrice) {
        this.tvHotelname = tvHotelname;
        this.tvItems = tvItems;
        this.tvDiscount = tvDiscount;
        this.tvRating = tvRating;
        this.tvTime = tvTime;
        this.tvPrice = tvPrice;
    }

    public String getTvHotelname() {
        return tvHotelname;
    }

    public void setTvHotelname(String tvHotelname) {
        this.tvHotelname = tvHotelname;
    }

    public String getTvItems() {
        return tvItems;
    }

    public void setTvItems(String tvItems) {
        this.tvItems = tvItems;
    }

    public String getTvDiscount() {
        return tvDiscount;
    }

    public void setTvDiscount(String tvDiscount) {
        this.tvDiscount = tvDiscount;
    }

    public String getTvRating() {
        return tvRating;
    }

    public void setTvRating(String tvRating) {
        this.tvRating = tvRating;
    }

    public String getTvTime() {
        return tvTime;
    }

    public void setTvTime(String tvTime) {
        this.tvTime = tvTime;
    }

    public String getTvPrice() {
        return tvPrice;
    }

    public void setTvPrice(String tvPrice) {
        this.tvPrice = tvPrice;
    }
}
