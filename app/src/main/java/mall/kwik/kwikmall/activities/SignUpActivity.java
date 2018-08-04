package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.rilixtech.CountryCodePicker;
import com.sdsmdg.tastytoast.TastyToast;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.interfaces.SignUpView;
import mall.kwik.kwikmall.presenter.LoginPresenter;
import mall.kwik.kwikmall.presenter.RegisterPresenter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, SignUpView {


    private EditText edUsername, edEmailAddress, edPassword, edMobileNo, edAddressRegister;
    private ImageButton btnSignUp;
    private TextView tvSignIn;
    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;
    private Context context;
    private NoInternetDialog noInternetDialog;
    private RelativeLayout mainRegisterLayout;
    CountryCodePicker ccp;
    private RegisterPresenter registerPresenter = new RegisterPresenter();
    private MaterialDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);


        context = this;
        registerPresenter.bind(this);


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
        ccp = findViewById(R.id.ccp);


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


        switch (v.getId()) {

            case R.id.btnSignUp:

                ccp.registerPhoneNumberTextView(edMobileNo);
                String codeWithPh = ccp.getFullNumberWithPlus();


                if (TextUtils.isEmpty(edUsername.getText().toString()) || !isValidEmail(edEmailAddress.getText().toString()) || TextUtils.isEmpty(edAddressRegister.getText().toString())
                        || TextUtils.isEmpty(edMobileNo.getText().toString()) || TextUtils.isEmpty(edPassword.getText().toString()) || edMobileNo.getText().toString().length() < 4) {


                    if (TextUtils.isEmpty(edUsername.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Username cant be empty", Snackbar.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(edEmailAddress.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Email can't be empty", Snackbar.LENGTH_SHORT).show();

                    } else if (!isValidEmail(edEmailAddress.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Use valid email address", Snackbar.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(edPassword.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Password can't be empty", Snackbar.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(edAddressRegister.getText().toString())) {
                        Snackbar.make(mainRegisterLayout, "Confirm Password can't be empty", Snackbar.LENGTH_SHORT).show();
                    } else if (edMobileNo.getText().toString().isEmpty()) {
                        TastyToast.makeText(getApplicationContext(), "Mobile No. can't be empty", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    } else if (edMobileNo.getText().toString().length() < 4) {
                        TastyToast.makeText(getApplicationContext(), "Minimum number must be four digit", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    }

//                    else if (edMobileNo.getText().toString().length() != 12) {
//                        TastyToast.makeText(getApplicationContext(), "Please enter the maximum number", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//                    }

//
//                            (TextUtils.isEmpty(edMobileNo.getText().toString()) && edMobileNo.getText().toString().length() != 10) {
//                        Snackbar.make(mainRegisterLayout, "Mobile No. can't be empty or Please enter the number", Snackbar.LENGTH_SHORT).show();
//                    } else if (edMobileNo.getText().toString().length() != 10) {
////                        Snackbar.make(mainRegisterLayout, "Please enter the number", Snackbar.LENGTH_SHORT).show();
//                        TastyToast.makeText(getApplicationContext(), "Please enter the number", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
//                    }


                } else {

                    System.out.println("SignUpActivity.onClick - - Else Case ");
                    View view1 = this.getCurrentFocus();

                    if (view1 != null) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                    }
                   /* new RegisterPresenter(apiService,sharedPrefsHelper,edUsername.getText().toString().trim(),edEmailAddress.getText().toString().trim(),
                            edPassword.getText().toString().trim(),codeWithPh,edAddressRegister.getText().toString().trim());
*/

                    registerPresenter.ok_signUp(apiService, edUsername.getText().toString().trim(), edEmailAddress.getText().toString().trim(),
                            edPassword.getText().toString().trim(), codeWithPh, edAddressRegister.getText().toString().trim());


                }

                break;


            case R.id.tvSignIn:

                Intent intent2 = new Intent(this, SignInActivity.class);
                startActivity(intent2);


                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }


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


    @Override
    public void showError(String message) {


        Snackbar.make(mainRegisterLayout, message, Snackbar.LENGTH_SHORT).show();


    }

    @Override
    public void startLoginActivity() {

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
}
