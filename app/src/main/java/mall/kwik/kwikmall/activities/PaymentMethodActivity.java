package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.activities.creditcard.CreditCardExpiryTextWatcher;
import mall.kwik.kwikmall.R;

public class PaymentMethodActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText edCardnumber,edCode;
    private ImageView imagePayMethodBack;
    private CheckBox checkboxCreditCard,checkboxDiscover,checkboxPaypal;
    private CheckBox chkMtn,chkVodafone,chkTigoAirtel;
    private TextView txtonfirmpayment,txtPaymentMethod,tvTotalAmount;
    private TextView tvAddress,tvPaymentMethod,tvCardNumber,tvSecurityCode,tvExpiryDate;
    private LinearLayout creditCardLayout,DiscoverCardLayout,CashOnDelivery;
    private LinearLayout customCreditLinearLayout,customDiscoverLinearLayout,customPaypalLinearLayout;
    String a,totalAmt;
    int keyDel;
    public static EditText edExpiryDate;
    private Context context;
    private NoInternetDialog noInternetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        findViewId();

        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        // totalAmt = bundle.getString("totalAmount");

        //tvTotalAmount.setText("155");

        clickListeners();

        EditTextChangedOnListener();





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }


    private void EditTextChangedOnListener() {

        edCardnumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                boolean flag = true;
                String eachBlock[] = edCardnumber.getText().toString().split("-");
                for (int i = 0; i < eachBlock.length; i++) {
                    if (eachBlock[i].length() > 4) {
                        flag = false;
                    }
                }
                if (flag) {

                    edCardnumber.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        if (((edCardnumber.getText().length() + 1) % 5) == 0) {

                            if (edCardnumber.getText().toString().split("-").length <= 3) {
                                edCardnumber.setText(edCardnumber.getText() + "-");
                                edCardnumber.setSelection(edCardnumber.getText().length());
                            }
                        }
                        a = edCardnumber.getText().toString();
                    } else {
                        a = edCardnumber.getText().toString();
                        keyDel = 0;
                    }

                } else {
                    edCardnumber.setText(a);
                }

            }


            @Override
            public void afterTextChanged(Editable s) {



            }
        });




        edExpiryDate.addTextChangedListener(new CreditCardExpiryTextWatcher(edExpiryDate));



    }


    private void findViewId() {


        //editTexts
        edCardnumber = findViewById(R.id.edCardnumber);
        edCode = findViewById(R.id.edCode);
        edExpiryDate = findViewById(R.id.edExpiryDate);

        imagePayMethodBack = findViewById(R.id.imagePayMethodBack);
        txtonfirmpayment = findViewById(R.id.txtonfirmpayment);
        txtPaymentMethod = findViewById(R.id.txtPaymentMethod);


        //Text view
       // tvTotalAmount = findViewById(R.id.tvTotalAmount);


        //Check Boxes
        checkboxCreditCard = findViewById(R.id.checkboxCreditCard);
        checkboxDiscover = findViewById(R.id.checkboxDiscover);
        checkboxPaypal = findViewById(R.id.checkboxPaypal);


        chkMtn = findViewById(R.id.chkMtn);
        chkVodafone = findViewById(R.id.chkVodafone);
        chkTigoAirtel = findViewById(R.id.chkTigoAirtel);

        //Layouts
        creditCardLayout = findViewById(R.id.creditCardLayout);
        DiscoverCardLayout = findViewById(R.id.DiscoverCardLayout);
        //CashOnDelivery = findViewById(R.id.CashOnDelivery);

        customCreditLinearLayout = findViewById(R.id.customCreditLinearLayout);
        customDiscoverLinearLayout = findViewById(R.id.customDiscoverLinearLayout);
        customPaypalLinearLayout = findViewById(R.id.customPaypalLinearLayout);


    }

    private void clickListeners() {

        imagePayMethodBack.setOnClickListener(this);
        txtonfirmpayment.setOnClickListener(this);

        checkboxCreditCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // perform logic

                    creditCardLayout.setVisibility(View.VISIBLE);

                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_selected);

                    customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
                    customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);

                    checkboxPaypal.setChecked(false);
                    checkboxDiscover.setChecked(false);

                    DiscoverCardLayout.setVisibility(View.GONE);

                    //CashOnDelivery.setVisibility(View.GONE);

                }
                else {

                    creditCardLayout.setVisibility(View.GONE);
                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);


                }
            }
        });

        checkboxDiscover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // perform logic

                    DiscoverCardLayout.setVisibility(View.VISIBLE);

                    checkboxPaypal.setChecked(false);
                    checkboxCreditCard.setChecked(false);

                    customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_selected);

                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
                    customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);


                    creditCardLayout.setVisibility(View.GONE);

                    //CashOnDelivery.setVisibility(View.GONE);

                }
                else {

                    DiscoverCardLayout.setVisibility(View.GONE);

                    customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);

                }

            }
        });


        checkboxPaypal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // perform logic

                    // CashOnDelivery.setVisibility(View.VISIBLE);

                    customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_selected);

                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
                    customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);

                    checkboxDiscover.setChecked(false);
                    checkboxCreditCard.setChecked(false);

                    DiscoverCardLayout.setVisibility(View.GONE);

                    creditCardLayout.setVisibility(View.GONE);


                }

                else {

                    // CashOnDelivery.setVisibility(View.GONE);

                    customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);

                }

            }
        });


        chkMtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    chkVodafone.setChecked(false);
                    chkTigoAirtel.setChecked(false);
                }


            }
        });


        chkVodafone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {

                    chkMtn.setChecked(false);
                    chkTigoAirtel.setChecked(false);

                }
            }
        });


        chkTigoAirtel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {

                    chkMtn.setChecked(false);
                    chkVodafone.setChecked(false);

                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }


    @Override
    public void onClick(View v) {

        if(v==imagePayMethodBack){

            finish();

            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }

        if(v==txtonfirmpayment){



            startActivity(new Intent(this, PaymentSuccessfullyActivity.class));

            overridePendingTransition(R.anim.slide_in, R.anim.nothing);



        }
    }

}
