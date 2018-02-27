package mall.kwik.kwikmall.manager;

import com.google.gson.JsonArray;

import java.util.HashMap;

import io.reactivex.Observable;
import mall.kwik.kwikmall.apiresponse.AddFavourites.AddFavouritesSuccess;
import mall.kwik.kwikmall.apiresponse.DeleteOrderSuccess.DeleteOrderSuccess;
import mall.kwik.kwikmall.apiresponse.FilterListResponse.FilterListSuccess;
import mall.kwik.kwikmall.apiresponse.FirebaseNotification.FCMSUCCESS;
import mall.kwik.kwikmall.apiresponse.ForgetPassword.ForgetPasswordSuccess;
import mall.kwik.kwikmall.apiresponse.ForgetPassword.UpdatePassSuccess;
import mall.kwik.kwikmall.apiresponse.ForgetPassword.ValidateOTPSuccess;
import mall.kwik.kwikmall.apiresponse.GetAllProductsResponse.GetAllProductsSuccess;
import mall.kwik.kwikmall.apiresponse.GetDeliveryStatus.GetDeliveryStatusSuccess;
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
    @FormUrlEncoded
    @POST("getShopsOrRestaurants")
    Observable<RestaurantsListSuccess> restaurantsList(@Field("lat") String lat,
                                                       @Field("lng") String lng,
                                                       @Field("type") String type,
                                                       @Field("catId") String catId);


    //------------------------4

    @POST("GetAllProducts")
    Observable<GetAllProductsSuccess> getAllProducts();

    //------------------------5
    @FormUrlEncoded
    @POST("GetStoreProducts")
    Observable<StoreProductsSuccess> getStoreProducts(@Field("bussinessId") String businessId);


    //------------------------6
    @POST("orderRegister")
    Observable<PlaceOrder> placeOrder(@Body JsonArray String);


    //------------------------7

    @FormUrlEncoded
    @POST("registerDevice")
    Observable<FCMSUCCESS> firebaseNotification(@Field("token") String Token,
                                                @Field("email") String Email);



    //------------------------8
    @POST("forgotPassword")
    Observable<ForgetPasswordSuccess> forgetPassword(@Body HashMap<String, String> hashMap);


    //------------------------9
    @FormUrlEncoded
    @POST("validateOTP")
    Observable<ValidateOTPSuccess> validateOTP(@Field("phoneNo") String Phone,
                                               @Field("OTP") String OTP);

    //------------------------10
    @FormUrlEncoded
    @POST("updatePassword")
    Observable<UpdatePassSuccess> updatePassword(@Field("email") String email,
                                                 @Field("password") String password);


    //------------------------11
    @POST("GetOrderList")
    Observable<GetOrderListSuccess> getOrderList(@Body HashMap<String, String> hashMap);

    //------------------------12
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

    //------------------------16
    @FormUrlEncoded
    @POST("GetDeliveryStatus")
    Observable<GetDeliveryStatusSuccess> getDeliveryStatus(@Field("bussinessId") String bussinessId,
                                                           @Field("orderNo") String orderNo,
                                                           @Field("userId") String userId);

    //------------------------17
    @POST("deleteOrderByUser")
    Observable<DeleteOrderSuccess> deleteOrderByUser(@Body HashMap<String, String> hashMap);


}
