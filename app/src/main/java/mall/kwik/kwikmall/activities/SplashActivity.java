package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.R;


public class SplashActivity extends AppCompatActivity {


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    private ImageView imageLeftTop,imageRightTop,imageLeftBottom,imageRightBottom;

    NoInternetDialog noInternetDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        context =this;

        noInternetDialog = new NoInternetDialog.Builder(context).build();


        imageLeftTop = findViewById(R.id.imageLeftTop);
        imageRightTop = findViewById(R.id.imageRightTop);
        imageLeftBottom = findViewById(R.id.imageLeftBottom);
        imageRightBottom = findViewById(R.id.imageRightBottom);

        Animation pushUpIn = AnimationUtils.loadAnimation(this, R.anim.push_up_in);

        Animation lefttoright = AnimationUtils.loadAnimation(this, R.anim.lefttoright);


        imageLeftTop.startAnimation(lefttoright); // start animation
        imageRightTop.startAnimation(lefttoright);

        imageRightBottom.startAnimation(pushUpIn);
        imageLeftBottom.startAnimation(pushUpIn);




        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                    Intent intent = new Intent(getApplicationContext(), FragmentsActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    return;

            }
        }, SPLASH_TIME_OUT);







    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }




}
