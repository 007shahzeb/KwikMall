package mall.kwik.kwikmall.presenter;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.activities.FragmentsActivity;
import mall.kwik.kwikmall.activities.SignInActivity;
import mall.kwik.kwikmall.activities.SignUpActivity;
import mall.kwik.kwikmall.apiresponse.FirebaseNotification.FCMSUCCESS;
import mall.kwik.kwikmall.apiresponse.LoginResponse.UserLoginSuccess;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.manager.GitApiInterface;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;

/**
 * Created by dharamveer on 29/1/18.
 */

public class LoginPresenter extends BasePresenter{

    private LoginPresenter.View view;

    private CompositeDisposable compositeDisposable;



    public LoginPresenter(final LoginPresenter.View view , GitApiInterface apiService, SharedPrefsHelper sharedPrefsHelper, String email, String passw) {
        super((SignInActivity) view);
        this.view = view;


        compositeDisposable = new CompositeDisposable();


        compositeDisposable.add(apiService.LoginUser(email,passw)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserLoginSuccess>() {
                    @Override
                    public void accept(UserLoginSuccess userLoginSuccess) throws Exception {


                        pDialog.dismiss();
                        compositeDisposable.dispose();


                        if(userLoginSuccess.getSuccess()){


                            String refreshedToken = FirebaseInstanceId.getInstance().getToken();


                            compositeDisposable.add(apiService.firebaseNotification(refreshedToken,email)
                                            .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<FCMSUCCESS>() {
                                                @Override
                                                public void accept(FCMSUCCESS fcmsuccess) throws Exception {



                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {

                                                }
                                            }));





                            sharedPrefsHelper.put(AppConstants.USER_ID,userLoginSuccess.getPayload().getId());
                            sharedPrefsHelper.put(AppConstants.USER_NAME,userLoginSuccess.getPayload().getName());
                            sharedPrefsHelper.put(AppConstants.EMAIL,userLoginSuccess.getPayload().getEmail());
                            sharedPrefsHelper.put(AppConstants.PHONE_NUMBER,userLoginSuccess.getPayload().getPhoneNo());


                            view.loginSuccessful("Success");


                        }
                        else {
                            alertDialog.setMessage("Invalid email or password");
                            alertDialog.show();
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {



                        view.loginFailed(throwable);
                        compositeDisposable.dispose();


                    }
                }));


    }


    public interface View {
        void loginSuccessful(String message);
        void loginFailed(Throwable t);
    }

}
