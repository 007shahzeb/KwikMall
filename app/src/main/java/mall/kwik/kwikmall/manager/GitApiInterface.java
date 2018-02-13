package mall.kwik.kwikmall.manager;

import com.google.gson.JsonArray;

import java.util.HashMap;

import io.reactivex.Observable;
import mall.kwik.kwikmall.apiresponse.AddFavourites.AddFavouritesSuccess;
import mall.kwik.kwikmall.apiresponse.FilterListResponse.FilterListSuccess;
import mall.kwik.kwikmall.apiresponse.FirebaseNotification.FCMSUCCESS;
import mall.kwik.kwikmall.apiresponse.ForgetPassword.ForgetPasswordSuccess;
import mall.kwik.kwikmall.apiresponse.ForgetPassword.UpdatePassSuccess;
import mall.kwik.kwikmall.apiresponse.ForgetPassword.ValidateOTPSuccess;
import mall.kwik.kwikmall.apiresponse.GetAllProductsResponse.GetAllProductsSuccess;
import mall.kwik.kwikmall.apiresponse.GetFavouriteResponse.GetFavouriteSuccess;
import mall.kwik.kwikmall.apiresponse.GetOrderListResponse.GetOrderListSuccess;
import mall.kwik.kwikmall.apiresponse.GetStoreProducts.StoreProductsSuccess;
import mall.kwik.kwikmall.apiresponse.LoginResponse.UserLoginSuccess;
import mall.kwik.kwikmall.apiresponse.PlaceOrderResponse.PlaceOrder;
import mall.kwik.kwikmall.apiresponse.RegisterResponse.RegisterSuccess;
import mall.kwik.kwikmall.apiresponse.RestaurantsListSuccess.RestaurantsListSuccess;
import mall.kwik.kwikmall.apiresponse.RestaurantsShopsResponse.ShopsListSuccess;
import mall.kwik.kwikmall.apiresponse.SendCategoriesResponse.SendCatToSuccess;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;



public interface GitApiInterface {

    //------------------------1
    @FormUrlEncoded
    @POST("userRegister")
    Observable<RegisterSuccess> registerUser(@Field("name") String Name,
                                             @Field("email") String Email,
                                             @Field("password") String Password,
                                             @Field("phoneNo") String phone,
                                             @Field("address") String address);

    //------------------------2
    @FormUrlEncoded
    @POST("userlogin")
    Observable<UserLoginSuccess> LoginUser(@Field("email") String Email,
                                           @Field("password") String Password);


    //------------------------3
    @POST("getShopsOrRestaurants")
    Observable<RestaurantsListSuccess> restaurantsList(@Body HashMap<String, String> hashMap);

    //------------------------4
    @POST("getShops")
    Observable<ShopsListSuccess> ShopsList();

    //------------------------5

    @POST("GetAllProducts")
    Observable<GetAllProductsSuccess> getAllProducts();

    //------------------------6
    @FormUrlEncoded
    @POST("GetStoreProducts")
    Observable<StoreProductsSuccess> getStoreProducts(@Field("bussinessId") String businessId);


    //------------------------7
    @POST("orderRegister")
    Observable<PlaceOrder> placeOrder(@Body JsonArray String);

    //------------------------8

    @FormUrlEncoded
    @POST("registerDevice")
    Observable<FCMSUCCESS> firebaseNotification(@Field("token") String Token,
                                                @Field("email") String Email);



    //------------------------9
    @FormUrlEncoded
    @POST("forgotPassword")
    Observable<ForgetPasswordSuccess> forgetPassword(@Body HashMap<String, String> hashMap);

    //------------------------10
    @FormUrlEncoded
    @POST("validateOTP")
    Observable<ValidateOTPSuccess> validateOTP(@Body HashMap<String, String> hashMap);

    //------------------------11
    @FormUrlEncoded
    @POST("updatePassword")
    Observable<UpdatePassSuccess> updatePassword(@Body HashMap<String, String> hashMap);


    //------------------------12
    @POST("GetOrderList")
    Observable<GetOrderListSuccess> getOrderList(@Body HashMap<String, String> hashMap);

    //------------------------13
    @FormUrlEncoded
    @POST("addToFavourites")
    Observable<AddFavouritesSuccess> addFavourites(@Body HashMap<String, String> hashMap);

    //------------------------13
    @POST("getfavouriteProducts")
    Observable<GetFavouriteSuccess> getfavouriteProducts(@Body HashMap<String, String> hashMap);

    //------------------------14
    @POST("GetCatagories")
    Observable<FilterListSuccess> GetCatagories();

    //------------------------15
    @FormUrlEncoded
    @POST("GetCatagories")
    Observable<SendCatToSuccess> sendCategories(@Body HashMap<String, String> hashMap);




}
