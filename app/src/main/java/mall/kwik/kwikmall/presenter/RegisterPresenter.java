package mall.kwik.kwikmall.presenter;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.activities.SignUpActivity;
import mall.kwik.kwikmall.apiresponse.RegisterResponse.RegisterSuccess;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.interfaces.LoginView;
import mall.kwik.kwikmall.interfaces.SignUpView;
import mall.kwik.kwikmall.manager.GitApiInterface;
import rx.schedulers.Schedulers;


public class RegisterPresenter {


    private CompositeDisposable compositeDisposable;



    private SignUpView view;

    public void bind(SignUpView view) {
        this.view = view;
    }

    public void unbind() {
        view = null;
    }


    public void ok_signUp(GitApiInterface apiService, String username, String email, String pass, String mobile, String address){

        compositeDisposable = new CompositeDisposable();

        view.showLoadingDialog();





        compositeDisposable.add(apiService.registerUser(username,email,pass,mobile,address)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterSuccess>() {
                    @Override
                    public void accept(RegisterSuccess registerSuccess) throws Exception {


                        compositeDisposable.dispose();

                        if(registerSuccess.getSuccess()){


                            System.out.println("RegisterPresenter.accept");
                            view.dismissLoadingDialog();
                            view.startLoginActivity();


                        }
                        else {
                            view.dismissLoadingDialog();

                            view.showError(registerSuccess.getMessage());

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
