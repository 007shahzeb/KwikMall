package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.expresspaygh.api.ExpressPayApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.activities.creditcard.CreditCardExpiryTextWatcher;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;

public class PaymentMethodActivity extends BaseActivity implements View.OnClickListener
                ,ExpressPayApi.ExpressPayPaymentCompletionListener{


    private EditText edCardnumber,edCode;
    private ImageView imagePayMethodBack;
    private CheckBox checkboxCreditCard,checkboxPaypal;
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
    static ExpressPayApi expressPayApi;



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

       // EditTextChangedOnListener();


        /** 
         *    Initialize a expressPayApi instance to communicate with expressPay SDK.  *  *
         * @param context 
         * @param yourServerURL the full path url to the location on your servers where you implement our server side sdk. 
         * *                      if null it defaults to https://sandbox.expresspaygh.com/sdk/server.php 
         * */

        expressPayApi= new ExpressPayApi(this,null);

        /**
         * Set the developnment env
         * Please ensure you set this value to false in your production code
         */
        expressPayApi.setDebugMode(true);





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
       // edCardnumber = findViewById(R.id.edCardnumber);
       // edCode = findViewById(R.id.edCode);
       // edExpiryDate = findViewById(R.id.edExpiryDate);

        imagePayMethodBack = findViewById(R.id.imagePayMethodBack);
        txtonfirmpayment = findViewById(R.id.txtonfirmpayment);
        txtPaymentMethod = findViewById(R.id.txtPaymentMethod);


        //Text view
       // tvTotalAmount = findViewById(R.id.tvTotalAmount);


        //Check Boxes
        checkboxCreditCard = findViewById(R.id.checkboxCreditCard);
       // checkboxDiscover = findViewById(R.id.checkboxDiscover);
        checkboxPaypal = findViewById(R.id.checkboxPaypal);


      //  chkMtn = findViewById(R.id.chkMtn);
      //  chkVodafone = findViewById(R.id.chkVodafone);
      //  chkTigoAirtel = findViewById(R.id.chkTigoAirtel);

        //Layouts
       // creditCardLayout = findViewById(R.id.creditCardLayout);
     //   DiscoverCardLayout = findViewById(R.id.DiscoverCardLayout);
        //CashOnDelivery = findViewById(R.id.CashOnDelivery);

        customCreditLinearLayout = findViewById(R.id.customCreditLinearLayout);
       // customDiscoverLinearLayout = findViewById(R.id.customDiscoverLinearLayout);
        customPaypalLinearLayout = findViewById(R.id.customPaypalLinearLayout);


    }

    public void pay(){
        /**
         * Make a request to your server to get a token
         * For this demo we have a sample server which we make the request to.
         * url: https://sandbox.expresspaygh.com/api/server.php
         * In Dev: Use amount 1.00 to simulate a failed transaction and greater than or equals 2.00 for a successful transaction
         */

        String order_no = sharedPrefsHelper.get(AppConstants.ORDER_NO,"");


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("request","submit");
        params.put("order_id", "82373");
        params.put("currency", "GHS");
        params.put("amount", "55");
        params.put("order_desc", "Food Items");
        params.put("user_name","testapi@expresspaygh.com");
        params.put("first_name","Test");
        params.put("last_name","Api");
        params.put("email","testapi@expresspaygh.com");
        params.put("phone_number","233244123123");
        params.put("account_number","233244123123");


        expressPayApi.submit(params, PaymentMethodActivity.this, new ExpressPayApi.ExpressPaySubmitCompletionListener() {
            @Override
            public void onExpressPaySubmitFinished(JSONObject jsonObject, String message) {
                /**
                 * Once the request is completed this listener is called with the response
                 * if the jsonObject is null then there was an error
                 */

                if (jsonObject!=null){
                    //You can access the returned token
                    try {
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            String token=expressPayApi.getToken();
                            checkout();
                        }else {
                            Log.d("expressPayDemo",message);
                            showDialog(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("expressPayDemo", message);
                        showDialog(message);

                    }

                }else {
                    Log.d("expressPayDemo",message);
                    showDialog(message);
                }
            }
        });

    }

    @Override
    public void onExpressPayPaymentFinished(boolean paymentCompleted, String message) {
        if (paymentCompleted){
            //Payment was completed
            String token=expressPayApi.getToken();
            queryPayment(token);
        }
        else{
            //There was an error
            Log.d("expressPayDemo",message);
            showDialog(message);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null)
            expressPayApi.onActivityResult(this, requestCode, resultCode, data);

        fpd.dismiss();

    }




    public void checkout(){
        /**
         * Displays the payment page to accept the payment method from the user
         *
         * When the payment is complete the ExpressPayPaymentCompletionListener is called
         */

        expressPayApi.checkout(this);
    }


    public  void queryPayment(String token){
        /**
         * After the payment has been completed we query our servers to find out
         * the status of the transaction
         * url: https://sandbox.expresspaygh.com/api/server.php
         */
        expressPayApi.query(token, new ExpressPayApi.ExpressPayQueryCompletionListener() {
            @Override
            public void onExpressPayQueryFinished(Boolean paymentSuccessful, JSONObject jsonObject, String message) {
                if (paymentSuccessful) {
                    showDialog(message);
                } else {
                    //There was an error
                    Log.d("expressPayDemo", message);
                    showDialog(message);
                }
            }
        });
    }



    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }





    private void clickListeners() {

        imagePayMethodBack.setOnClickListener(this);
        txtonfirmpayment.setOnClickListener(this);

        checkboxCreditCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // perform logic

                 /*   creditCardLayout.setVisibility(View.VISIBLE);

                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_selected);

                  //  customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
                    customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);

                  //  checkboxDiscover.setChecked(false);

                  //  DiscoverCardLayout.setVisibility(View.GONE);

                    //CashOnDelivery.setVisibility(View.GONE);


         */

                      pay();


                        checkboxPaypal.setChecked(false);



                }
                else {

                /*    creditCardLayout.setVisibility(View.GONE);
                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
*/



                }
            }
        });

/*
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
*/


        checkboxPaypal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // perform logic

                    // CashOnDelivery.setVisibility(View.VISIBLE);

                   /* customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_selected);

                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
                   // customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);

                   // checkboxDiscover.setChecked(false);

                   // DiscoverCardLayout.setVisibility(View.GONE);

                    creditCardLayout.setVisibility(View.GONE);*/

                    checkboxCreditCard.setChecked(false);

                }

                else {

                    // CashOnDelivery.setVisibility(View.GONE);

                  //  customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);

                }

            }
        });


      /*  chkMtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                   // chkVodafone.setChecked(false);
                  //  chkTigoAirtel.setChecked(false);
                }


            }
        });*/


     /*   chkVodafone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        });*/

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


            Toast.makeText(context,"Order successfully Submitted",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, PaymentSuccessfullyActivity.class));

            overridePendingTransition(R.anim.slide_in, R.anim.nothing);



        }
    }


}
