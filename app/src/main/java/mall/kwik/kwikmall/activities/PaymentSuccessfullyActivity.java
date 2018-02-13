package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.R;

public class PaymentSuccessfullyActivity extends AppCompatActivity {


    private TextView txtSuccess,tvAddress,tvPaymentMethod,tvSuccessfully,tvCongra,tvDes,txtonSuccess;
    private ImageView imageSuccessBack;
    private Context context;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment_successfully);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        findViewId();

        clickListeners();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void findViewId() {
        imageSuccessBack = findViewById(R.id.imageSuccessBack);

        txtSuccess = findViewById(R.id.txtSuccess);
        tvAddress = findViewById(R.id.tvAddress);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        tvSuccessfully = findViewById(R.id.tvSuccessfully);
        tvCongra = findViewById(R.id.tvCongra);
        tvDes = findViewById(R.id.tvDes);
        txtonSuccess = findViewById(R.id.txtonSuccess);



    }

    private void clickListeners() {

        imageSuccessBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        txtonSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(PaymentSuccessfullyActivity.this,TrackOrderMapActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in, R.anim.nothing);

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }


}
