package mall.kwik.kwikmall.presenter;

import android.content.Intent;
import android.widget.Toast;

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
import mall.kwik.kwikmall.interfaces.LoginView;
import mall.kwik.kwikmall.manager.GitApiInterface;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;

/**
 * Created by dharamveer on 29/1/18.
 */

public class LoginPresenter{


    private CompositeDisposable compositeDisposable;

    private LoginView view;

    public void bind(LoginView view) {
        this.view = view;
    }

    public void unbind() {
        view = null;
    }




    public void ok_login(GitApiInterface apiService, SharedPrefsHelper sharedPrefsHelper, String email, String passw){

        compositeDisposable = new CompositeDisposable();

        view.showLoadingDialog();

        compositeDisposable.add(apiService.LoginUser(email,passw)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserLoginSuccess>() {
                    @Override
                    public void accept(UserLoginSuccess userLoginSuccess) throws Exception {


                        compositeDisposable.dispose();


                        if(userLoginSuccess.getSuccess())
                        {

                            sharedPrefsHelper.put(AppConstants.USER_ID,userLoginSuccess.getPayload().getId());
                            sharedPrefsHelper.put(AppConstants.USER_NAME,userLoginSuccess.getPayload().getName());
                            sharedPrefsHelper.put(AppConstants.EMAIL,userLoginSuccess.getPayload().getEmail());
                            sharedPrefsHelper.put(AppConstants.PHONE_NUMBER,userLoginSuccess.getPayload().getPhoneNo());

                            view.startMainActivity();
                            view.dismissLoadingDialog();


                        }
                        else {
                            view.dismissLoadingDialog();

                            view.showError();

                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {


                        compositeDisposable.dispose();


                    }
                }));



    }




}
