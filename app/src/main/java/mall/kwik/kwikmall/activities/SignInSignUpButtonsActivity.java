package mall.kwik.kwikmall.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.R;
import qiu.niorgai.StatusBarCompat;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignInSignUpButtonsActivity extends AppCompatActivity implements
        View.OnClickListener{

    private Button btnSignIn,btnSignUp;

    private Context context;
    private NoInternetDialog noInternetDialog;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        activity = SignInSignUpButtonsActivity.this;

        //translucent status bar
        StatusBarCompat.translucentStatusBar(activity);

        //should hide status bar background (default black background) when SDK >= 21
        StatusBarCompat.translucentStatusBar(activity, false);


        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();



        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);


        Animation lefttoright = AnimationUtils.loadAnimation(this, R.anim.lefttoright);

        btnSignIn.startAnimation(lefttoright);
        btnSignUp.startAnimation(lefttoright);


        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }



 /*   public static void dialog(boolean value){

        if(value){


            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    cardViewConnected.setVisibility(View.GONE);
                }
            };
            handler.postDelayed(delayrunnable, 3000);

        }
        else {
            cardViewConnected.setVisibility(View.GONE);
            tvDisconnected.setTextColor(Color.WHITE);
            cardViewDisconnected.setVisibility(View.VISIBLE);
            disconnectedLayout.setBackgroundColor(Color.RED);
        }
    }*/



    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(View v) {

        if(v==btnSignIn){

            btnSignUp.setBackgroundResource(R.drawable.roundbuttonrightwhite);
            btnSignUp.setTextColor(Color.GREEN);
            btnSignUp.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.rightarrowgreen,0);


            btnSignIn.setBackgroundResource(R.drawable.roundbuttonrightgreen);
            btnSignIn.setTextColor(Color.WHITE);
            btnSignIn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.rightarrowwhite,0);


            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);

            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
        if(v==btnSignUp){

            btnSignIn.setBackgroundResource(R.drawable.roundbuttonrightwhite);
            btnSignIn.setTextColor(Color.GREEN);
            btnSignIn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.rightarrowgreen,0);

            btnSignUp.setBackgroundResource(R.drawable.roundbuttonrightgreen);
            btnSignUp.setTextColor(Color.WHITE);
            btnSignUp.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.rightarrowwhite,0);


            Intent intent = new Intent(this,SignUpActivity.class);
            startActivity(intent);

            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
