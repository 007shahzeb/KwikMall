package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.R;

public class PaymentsModeActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView imagePayModeBack;
    private Context context;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payments_mode);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();


        imagePayModeBack = findViewById(R.id.imagePayModeBack);

        imagePayModeBack.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }

    @Override
    public void onClick(View v) {


        if(v==imagePayModeBack){

            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }

    }
}
