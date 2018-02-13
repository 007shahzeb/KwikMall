package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.BaseFragActivity.BaseActivity;
import mall.kwik.kwikmall.apiresponse.FirebaseNotification.FCMSUCCESS;
import mall.kwik.kwikmall.apiresponse.LoginResponse.UserLoginSuccess;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.presenter.LoginPresenter;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener, LoginPresenter.View{


    private ImageButton btnArrow;
    private TextView txtCreateNow,txtForgetPassword,txtSignIn,txtDontAcc;
    private EditText edEmailAddressLogin,edPasswordLogin;
    private AwesomeValidation awesomeValidation;

    private Context context;
    private NoInternetDialog noInternetDialog;
    private RelativeLayout mainLoginLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        findViewId();

        edPasswordLogin.setImeOptions(EditorInfo.IME_ACTION_DONE);


        edPasswordLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {


                    //submitForm();

                    return true;


                }
                return false;
            }
        });


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
                new LoginPresenter(SignInActivity.this,apiService,sharedPrefsHelper,edEmailAddressLogin.getText().toString().trim(),edPasswordLogin.getText().toString().trim());


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
    public void loginSuccessful(String message) {



        Snackbar.make(mainLoginLayout, message, Snackbar.LENGTH_SHORT).show();


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
    public void loginFailed(Throwable t) {

        Snackbar.make(mainLoginLayout, t.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();


    }
}
