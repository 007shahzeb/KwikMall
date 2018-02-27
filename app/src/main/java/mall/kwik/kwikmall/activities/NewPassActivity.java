package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import am.appwise.components.ni.NoInternetDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.apiresponse.ForgetPassword.UpdatePassSuccess;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;

public class NewPassActivity extends BaseActivity {


    @BindView(R.id.edEmailAddress)
    EditText edEmailAddress;
    @BindView(R.id.edNewPassword)
    EditText edNewPassword;
    @BindView(R.id.dialog_progress_bar)
    ProgressBar dialogProgressBar;
    @BindView(R.id.btnNewPassSubmit)
    ImageView btnNewPassSubmit;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Context context;
    private String mEmailid,mNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);
        ButterKnife.bind(this);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();


        String userEmail = sharedPrefsHelper.get(AppConstants.EMAIL,"");

        edEmailAddress.setText(userEmail);


    }

    @OnClick(R.id.btnNewPassSubmit)
    public void onViewClicked() {

        dialogProgressBar.setVisibility(View.VISIBLE);


        mEmailid = edEmailAddress.getText().toString().trim();
        mNewPass = edNewPassword.getText().toString().trim();


        if(TextUtils.isEmpty(mEmailid) || TextUtils.isEmpty(mNewPass)){


            if(TextUtils.isEmpty(mEmailid)){

                edEmailAddress.setError("Email Address Can't be empty");

            }else if(TextUtils.isEmpty(mNewPass)){

                edNewPassword.setError("New password can't be empty");

            }
        }
        else {


            compositeDisposable.add(apiService.updatePassword(mEmailid, mNewPass)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<UpdatePassSuccess>() {
                        @Override
                        public void accept(UpdatePassSuccess updatePassSuccess) throws Exception {


                            if (updatePassSuccess.getSuccess()) {


                                dialogProgressBar.setVisibility(View.GONE);


                                Intent intent = new Intent(context, SignInActivity.class);
                                context.startActivity(intent);


                            } else {

                                dialogProgressBar.setVisibility(View.GONE);

                                showAlertDialog("Retry", updatePassSuccess.getMessage());

                            }


                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                            compositeDisposable.dispose();
                            showAlertDialog("Retry", throwable.getMessage());
                            dialogProgressBar.setVisibility(View.GONE);


                        }
                    }));


        }

    }
}
