package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.iid.FirebaseInstanceId;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.apiresponse.FirebaseNotification.FCMSUCCESS;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.interfaces.LoginView;
import mall.kwik.kwikmall.presenter.LoginPresenter;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;

public class SignInActivity extends BaseActivity implements View.OnClickListener, LoginView{


    private ImageButton btnArrow;
    private TextView txtCreateNow,txtForgetPassword,txtSignIn,txtDontAcc;
    private EditText edEmailAddressLogin,edPasswordLogin;
    private AwesomeValidation awesomeValidation;

    private Context context;
    private NoInternetDialog noInternetDialog;
    private RelativeLayout mainLoginLayout;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MaterialDialog dialog;
    private LoginPresenter loginPresenter = new LoginPresenter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loginPresenter.bind(this);


        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        findViewId();

     /* edPasswordLogin.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edPasswordLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    return true;

                }
                return false;
            }
        });*/


        awesomeValidation.addValidation(SignInActivity.this, R.id.edEmailAddressLogin, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        //awesomeValidation.addValidation(SignInActivity.this, R.id.edPasswordLogin, "@^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.passworderror);

        clickListeners();

        edEmailAddressLogin.setText("");
        edPasswordLogin.setText("");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
        loginPresenter.unbind();

    }


    private void findViewId() {
        //Buttons
        btnArrow = findViewById(R.id.btnArrow);

        //TextViews
        txtCreateNow = findViewById(R.id.txtCreateNow);
        txtForgetPassword = findViewById(R.id.txtForgetPassword);
        txtSignIn = findViewById(R.id.txtSignIn);
        txtDontAcc = findViewById(R.id.txtDontAcc);

        //EditTexts
        edEmailAddressLogin = findViewById(R.id.edEmailAddressLogin);
        edPasswordLogin = findViewById(R.id.edPasswordLogin);


        //relative layout
        mainLoginLayout = findViewById(R.id.mainLoginLayout);

    }



    @Override
    public void onClick(View v) {

        if(v==btnArrow){



            String refreshedToken = FirebaseInstanceId.getInstance().getToken();


            compositeDisposable.add(apiService.firebaseNotification(refreshedToken,edEmailAddressLogin.getText().toString())
                    .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<FCMSUCCESS>() {
                        @Override
                        public void accept(FCMSUCCESS fcmsuccess) throws Exception {


                           // Toast.makeText(SignInActivity.this,fcmsuccess.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                            compositeDisposable.dispose();

                        }
                    }));

            if (TextUtils.isEmpty(edEmailAddressLogin.getText().toString()) ||  TextUtils.isEmpty(edPasswordLogin.getText().toString())) {

                if (TextUtils.isEmpty(edEmailAddressLogin.getText().toString())) {
                    Snackbar.make(mainLoginLayout, "Email can't be empty", Snackbar.LENGTH_SHORT).show();


                } else if (TextUtils.isEmpty(edPasswordLogin.getText().toString())) {
                    Snackbar.make(mainLoginLayout, "Password can't be empty", Snackbar.LENGTH_SHORT).show();
                }


            }


            else {
                View view1 = this.getCurrentFocus();

                if(view1 !=null){

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(),0);
                }
              //  new LoginPresenter(SignInActivity.this,apiService,sharedPrefsHelper,edEmailAddressLogin.getText().toString().trim(),edPasswordLogin.getText().toString().trim());

                loginPresenter.ok_login(apiService,sharedPrefsHelper,edEmailAddressLogin.getText().toString().trim(),edPasswordLogin.getText().toString().trim());

            }




        }

        if(v==txtCreateNow){

            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);


            overridePendingTransition(R.anim.enter, R.anim.exit);

        }

        if(v==txtForgetPassword){

            Intent intent = new Intent(this, ForgetPasswordActivity.class);
            startActivity(intent);

            overridePendingTransition(R.anim.enter, R.anim.exit);

        }

    }

    private void clickListeners() {
        btnArrow.setOnClickListener(this);
        txtCreateNow.setOnClickListener(this);
        txtForgetPassword.setOnClickListener(this);
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }


    @Override
    public void showError() {
        Toast.makeText(this, "Invalid Password or Email", Toast.LENGTH_LONG) // Invalid Password or number
                .show();
    }

    @Override
    public void startMainActivity() {

        //Snackbar.make(mainLoginLayout, "", Snackbar.LENGTH_SHORT).show();


        DBHelper dbHelper = new DBHelper(SignInActivity.this);

        dbHelper.deleteAllOrders();
        dbHelper.deleteNewAddress();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SignInActivity.this, FragmentsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                UserDataUtility.setLogin(true, getApplicationContext());
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        }, 1000);


    }



    @Override
    public void showLoadingDialog() {
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();
    }

    @Override
    public void dismissLoadingDialog() {
        dialog.dismiss();
    }


}
