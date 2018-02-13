package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.HashMap;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.BaseFragActivity.BaseActivity;
import mall.kwik.kwikmall.apiresponse.RegisterResponse.RegisterSuccess;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.presenter.RegisterPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, RegisterPresenter.View  {


    private EditText  edUsername,edEmailAddress,edPassword,edMobileNo,edAddressRegister;
    private ImageButton btnSignUp;
    private TextView tvSignIn;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;
    private Context context;
    private NoInternetDialog noInternetDialog;
    private RelativeLayout mainRegisterLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        findViewId();

        clickListeners();

        edUsername.setText("");
        edEmailAddress.setText("");
        edPassword.setText("");
        edMobileNo.setText("");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void findViewId() {


        //Edit Text
        edUsername = findViewById(R.id.edUsername);
        edEmailAddress = findViewById(R.id.edEmailAddress);
        edPassword = findViewById(R.id.edPassword);
        edMobileNo = findViewById(R.id.edMobileNo);
        edAddressRegister = findViewById(R.id.edAddressRegister);

        //Image button
        btnSignUp = findViewById(R.id.btnSignUp);

        //Text Views
        tvSignIn = findViewById(R.id.tvSignIn);

        //relative layout
        mainRegisterLayout = findViewById(R.id.mainRegisterLayout);


        awesomeValidation.addValidation(SignUpActivity.this, R.id.edUsername, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);

    }


    private void clickListeners() {

        //Button Clikc
        btnSignUp.setOnClickListener(this);


        //Text View Click
        tvSignIn.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.btnSignUp:


                if (TextUtils.isEmpty(edUsername.getText().toString()) || !isValidEmail(edEmailAddress.getText().toString()) || TextUtils.isEmpty(edAddressRegister.getText().toString())
                        || TextUtils.isEmpty(edMobileNo.getText().toString()) || TextUtils.isEmpty(edPassword.getText().toString())) {


                    if (TextUtils.isEmpty(edUsername.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Username cant be empty", Snackbar.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(edEmailAddress.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Email can't be empty", Snackbar.LENGTH_SHORT).show();

                    } else if (!isValidEmail(edEmailAddress.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Use valid email address", Snackbar.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(edPassword.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Password can't be empty", Snackbar.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(edAddressRegister.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Confirm Password can't be empty", Snackbar.LENGTH_SHORT).show();
                    }

                    else if (TextUtils.isEmpty(edMobileNo.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Mobile No can't be empty", Snackbar.LENGTH_SHORT).show();
                    }



                }
                else {
                    View view1 = this.getCurrentFocus();

                    if(view1 !=null){

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(),0);
                    }
                    new RegisterPresenter(SignUpActivity.this,apiService,sharedPrefsHelper,edUsername.getText().toString().trim(),edEmailAddress.getText().toString().trim(),
                            edPassword.getText().toString().trim(),edMobileNo.getText().toString().trim(),edAddressRegister.getText().toString().trim());


                }



                break;


            case R.id.tvSignIn:

                Intent intent2 = new Intent(this, SignInActivity.class);
                startActivity(intent2);


                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }

    }

    @Override
    public void registerSuccessful(String message) {


        Snackbar.make(mainRegisterLayout, message, Snackbar.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        }, 1000);




    }

    @Override
    public void registerFailed(Throwable t) {


        Snackbar.make(mainRegisterLayout, t.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();



    }
}
