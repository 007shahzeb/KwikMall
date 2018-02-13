package mall.kwik.kwikmall.presenter;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.activities.SignUpActivity;
import mall.kwik.kwikmall.apiresponse.RegisterResponse.RegisterSuccess;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.manager.GitApiInterface;
import rx.schedulers.Schedulers;


public class RegisterPresenter extends BasePresenter{



    private RegisterPresenter.View view;

    private CompositeDisposable compositeDisposable;



    public RegisterPresenter(final RegisterPresenter.View view, GitApiInterface apiService,
                             SharedPrefsHelper sharedPrefsHelper, String username, String email, String pass, String mobile, String address) {
        super((SignUpActivity) view);
        this.view = view;


        compositeDisposable = new CompositeDisposable();


        compositeDisposable.add(apiService.registerUser(username,email,pass,mobile,address)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterSuccess>() {
                    @Override
                    public void accept(RegisterSuccess registerSuccess) throws Exception {


                        pDialog.dismiss();
                        compositeDisposable.dispose();

                        if(registerSuccess.getSuccess()){


                            view.registerSuccessful(registerSuccess.getMessage());
                        }
                        else {

                            alertDialog.setMessage(registerSuccess.getMessage());
                            alertDialog.show();

                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        view.registerFailed(throwable);
                        compositeDisposable.dispose();

                    }
                }));





    }


    public interface View {
        void registerSuccessful(String message);

        void registerFailed(Throwable t);
    }



}
