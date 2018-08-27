package mall.kwik.kwikmall.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.expresspaygh.api.ExpressPayApi;
import com.expresspaygh.api.models.Configuration;
import com.google.gson.JsonObject;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.activities.creditcard.CreditCardExpiryTextWatcher;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;

public class PaymentMethodActivity extends BaseActivity implements View.OnClickListener
        , ExpressPayApi.ExpressPayPaymentCompletionListener {


    private EditText edCardnumber, edCode;
    private ImageView imagePayMethodBack;
    private CheckBox checkboxCreditCard, checkboxPaypal;
    private CheckBox chkMtn, chkVodafone, chkTigoAirtel;
    private TextView txtonfirmpayment, txtPaymentMethod, tvTotalAmount;
    private TextView tvAddress, tvPaymentMethod, tvCardNumber, tvSecurityCode, tvExpiryDate;
    private LinearLayout creditCardLayout, DiscoverCardLayout, CashOnDelivery;
    private LinearLayout customCreditLinearLayout, customDiscoverLinearLayout, customPaypalLinearLayout;
    String a, totalAmt;
    int keyDel;
    public static EditText edExpiryDate;
    private Context context;
    private NoInternetDialog noInternetDialog;
    static ExpressPayApi expressPayApi;
    String SUBMIT_TESTING_API_URL = "https://sandbox.expresspaygh.com/api/submit.php";
    String REDIRECT_URL = "http://employeelive.com/Kwiqmall/public/";
    private Button dialog_accept, dialog_reject;
    Dialog dialog;
    private String MerchantID;
    private String API_Key;
    private String order_no;
    //    private String order_no;
//    private String API_Key;
//    private String MerchantID;
//    public static Activity fa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);


//        fa = this;


        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        String order_no = sharedPrefsHelper.get(AppConstants.ORDER_NO, "");

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

        expressPayApi = new ExpressPayApi(this, SUBMIT_TESTING_API_URL);

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

    public void pay() {
        /**
         * Make a request to your server to get a token
         * For this demo we have a sample server which we make the request to.
         * url: https://sandbox.expresspaygh.com/api/server.php
         * In Dev: Use amount 1.00 to simulate a failed transaction and greater than or equals 2.00 for a successful transaction
         */


        order_no = sharedPrefsHelper.get(AppConstants.ORDER_NO, "");
        String user_name = sharedPrefsHelper.get(AppConstants.USER_NAME, "");
        String email = sharedPrefsHelper.get(AppConstants.EMAIL, "");
        Integer user_id = sharedPrefsHelper.get(AppConstants.USER_ID, 0);
        String phonenumber = sharedPrefsHelper.get(AppConstants.PHONE_NUMBER, "");

        Locale current = getResources().getConfiguration().locale;
        String currency = Currency.getInstance(current).getCurrencyCode();

        MerchantID = "36945b03f0395f9685b0";
        API_Key = "wHURN7If4egq4DzlrZm9I-DwRFDaXTWKabz9BA2xd9-LzHws0mrFFg4CGjIJdgu-YgetvegMog4BFJ2NV0M";
        double amountFailed = 1.00;
        double amountSuccess = 2.00;
        String REDIRECT_URL = "https://sandbox.expresspaygh.com/api/checkout.php";    // it called finally when transaction completed


        HashMap<String, String> params = new HashMap<String, String>();


//      params.put("request", "Submit");
        params.put("order-id", order_no);
        params.put("email", email);
        params.put("phone_number", phonenumber);
        params.put("merchant-id", MerchantID);
        params.put("api-key", API_Key);
        params.put("firstname", user_name);
        params.put("lastname", "Ventures");
        params.put("currency", "GHS");
        params.put("amount", String.valueOf(amountSuccess));
        params.put("redirect-url", "http://employeelive.com/Kwiqmall/public/");


        expressPayApi.submit(params, PaymentMethodActivity.this, new ExpressPayApi.ExpressPaySubmitCompletionListener() {
            @Override
            public void onExpressPaySubmitFinished(JSONObject jsonObject, String message) {
                /**
                 * Once the request is completed this listener is called with the response
                 * if the jsonObject is null then there was an error
                 */

                if (jsonObject != null) {
                    //You can access the returned token
                    try {

                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("1")) {
                            String token = expressPayApi.getToken();

                            checkout(token);

                        } else {

                            Log.d("expressPayDemo", message);
                            showDialog(message);
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                        Log.d("expressPayDemo", message);
                        showDialog(message);

                    }

                } else {

                    Log.d("expressPayDemo", message);
                    showDialog(message);
                }


            }
        });

    }

    @Override
    public void onExpressPayPaymentFinished(boolean paymentCompleted, String message) {

        if (paymentCompleted) {
            //Payment was completed
            String token = expressPayApi.getToken();

            queryPayment(token);

        } else {

            //There was an error
            Log.d("expressPayDemo", message);
            showDialog(message);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null)
            expressPayApi.onActivityResult(this, requestCode, resultCode, data);

//        fpd.dismiss(); // Shahzeb commented this code getting crash

    }


    public void checkout(String token) {
        /**
         * Displays the payment page to accept the payment method from the user
         *
         * When the payment is complete the ExpressPayPaymentCompletionListener is called
         */

        expressPayApi.checkout(this, token, "http://employeelive.com/Kwiqmall/public/");

//        Toast.makeText(getApplicationContext(),"Completing your payment",Toast.LENGTH_SHORT).show();

    }


    public void queryPayment(String token) {

        /**
         * After the payment has been completed we query our servers to find out
         * the status of the transaction
         * url: https://sandbox.expresspaygh.com/api/server.php
         */

//        expressPayApi.query(token, new ExpressPayApi.ExpressPayQueryCompletionListener() {
//            @Override
//            public void onExpressPayQueryFinished(Boolean paymentSuccessful, JSONObject jsonObject, String message) {
//                if (paymentSuccessful) {
//
//                    Toast.makeText(getApplicationContext() , "Query payment IF part is working" , 0).show();
//                    showDialog(message);
//                } else {
//                    //There was an error
//                    Log.d("expressPayDemo", message);
//                    showDialog(message);
//
//                    Toast.makeText(getApplicationContext() , "Query payment Else part is working" , 0).show();
//
//                }
//            }
//        });


        String url = "https://sandbox.expresspaygh.com/api/query.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("PaymentMethodActivity.onResponse " + response);

                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.


//                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


                try {
                    JSONObject json = new JSONObject(response);

                    if (json.optInt("result") == 1) {
                        TastyToast.makeText(getApplicationContext(), "Approved", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        callSuccessfully();


                    } else if (json.optInt("result") == 2) {

                        TastyToast.makeText(getApplicationContext(), "Declined", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    } else if (json.optInt("result") == 3) {

                        TastyToast.makeText(getApplicationContext(), "Error in transaction data or system error", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    } else if (json.optInt("result") == 4) {

                        TastyToast.makeText(getApplicationContext(), "Pending", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    } else {

                        TastyToast.makeText(getApplicationContext(), "Retry", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sandbox.expresspaygh.com/api/query.php" + token + order_no));
//                startActivity(browserIntent);


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Log.i("", "onErrorResponse: ");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("token", token); //Add the data you'd like to send to the server.
                MyData.put("merchant-id", MerchantID); //Add the data you'd like to send to the server.
                MyData.put("api-key", API_Key); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        queue.add(MyStringRequest);

    }

    private void callSuccessfully() {

        startActivity(new Intent(this, PaymentSuccessfullyActivity.class));

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
                if (isChecked) {


                    txtonfirmpayment.setClickable(false);


//                    txtonfirmpayment.setTextColor(Color.parseColor("#ffffff"));
//                    txtonfirmpayment.setBackground(getDrawable(R.drawable.false_submit));

                    // perform logic

//                    creditCardLayout.setVisibility(View.VISIBLE);
//
//                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_selected);
//
//                  //  customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
//                    customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
//
//                  //  checkboxDiscover.setChecked(false);
//
//                  //  DiscoverCardLayout.setVisibility(View.GONE);
//
//                    //CashOnDelivery.setVisibility(View.GONE);
//
//

                    pay();


                } else {
                    checkboxPaypal.setChecked(false);

//                    creditCardLayout.setVisibility(View.GONE);
//                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);


                }
            }
        });

//        checkboxDiscover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    // perform logic
//
//                    DiscoverCardLayout.setVisibility(View.VISIBLE);
//
//                    checkboxPaypal.setChecked(false);
//                    checkboxCreditCard.setChecked(false);
//
//                    customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_selected);
//
//                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
//                    customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
//
//
//                    creditCardLayout.setVisibility(View.GONE);
//
//                    //CashOnDelivery.setVisibility(View.GONE);
//
//                }
//                else {
//
//                    DiscoverCardLayout.setVisibility(View.GONE);
//
//                    customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
//
//                }
//
//            }
//        });

        checkboxPaypal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    System.out.println("PaymentMethodActivity.onCheckedChanged - - - Checked case");

                    txtonfirmpayment.setClickable(true);
                    checkboxCreditCard.setChecked(false);
                    // perform logic

                    // CashOnDelivery.setVisibility(View.VISIBLE);
//
//                    customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_selected);
//
//                    customCreditLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
//                   // customDiscoverLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);
//
//                   // checkboxDiscover.setChecked(false);
//
//                   // DiscoverCardLayout.setVisibility(View.GONE);
//
//                    creditCardLayout.setVisibility(View.GONE);
//
//                    AlertDialog.Builder builder;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
//                    } else {
//                        builder = new AlertDialog.Builder(context);
//                    }
//                    builder.setCancelable(false);
//                    builder.setTitle("Choose The Action")
//
////                            .setMessage("Are you sure you want to delete this entry?")
//                            .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // continue with delete
//                                    System.out.println("PaymentMethodActivity.onClick - - - Aceept");
//                                    Toast.makeText(context, "Order successfully Submitted", Toast.LENGTH_SHORT).show();
////
//                                    startActivity(new Intent(context, PaymentSuccessfullyActivity.class));
//                                    finish();
//                                    overridePendingTransition(R.anim.slide_in, R.anim.nothing);
//                                }
//                            })
//                            .setNegativeButton("REJECT", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // do nothing
//
//                                    System.out.println("PaymentMethodActivity.onClick - - - Reject");
//                                    Toast.makeText(getApplicationContext(), "Not in policy", Toast.LENGTH_SHORT).show();
//                                    checkboxPaypal.setChecked(false);
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                    builder.setCancelable(false);


//                    dialog = new Dialog(context); // Context, this, etc.
//                    dialog.setContentView(R.layout.dialog_cash_on_delivery);
//                    dialog_accept = findViewById(R.id.dialog_accept);
//                    dialog_reject = findViewById(R.id.dialog_reject);
////                    dialog.setTitle("Choose The Action");
//                    dialog.show();
//                    checkboxCreditCard.setChecked(false);

                } else {

                    // CashOnDelivery.setVisibility(View.GONE);

                    //  customPaypalLinearLayout.setBackgroundResource(R.drawable.custom_background_unselected);

                }

            }
        });


//        chkMtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//
//                   // chkVodafone.setChecked(false);
//                  //  chkTigoAirtel.setChecked(false);
//                }
//
//
//            }
//        });


//        chkVodafone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if(isChecked) {
//
//                    chkMtn.setChecked(false);
//                    chkTigoAirtel.setChecked(false);
//
//                }
//            }
//        });
//
//
//        chkTigoAirtel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if(isChecked) {
//
//                    chkMtn.setChecked(false);
//                    chkVodafone.setChecked(false);
//
//                }
//
//            }
//        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }

    @Override
    public void onClick(View v) {


        if (v == imagePayMethodBack) {

            finish();

            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        }

        if (v == txtonfirmpayment) {

//            before

//            if (!checkboxPaypal.isChecked() && !checkboxCreditCard.isChecked()) {
//                Toast.makeText(getApplicationContext(), "Please select the payment way", Toast.LENGTH_SHORT).show();
//            }
//
//            Toast.makeText(context, "Order successfully Submitted", Toast.LENGTH_SHORT).show();
//
//            startActivity(new Intent(this, PaymentSuccessfullyActivity.class));
//
//            overridePendingTransition(R.anim.slide_in, R.anim.nothing);


            if (checkboxPaypal.isChecked() && !checkboxCreditCard.isChecked()) {

                TastyToast.makeText(getApplicationContext(), "Order successfully Submitted", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                startActivity(new Intent(this, PaymentSuccessfullyActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);

            } else if (!checkboxPaypal.isChecked() && checkboxCreditCard.isChecked()) {

                TastyToast.makeText(getApplicationContext(), "Order successfully Submitted", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                startActivity(new Intent(this, PaymentSuccessfullyActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.nothing);

            } else {

                TastyToast.makeText(getApplicationContext(), "Please select the payment way", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

            }


        }
//        if (v == dialog_accept) {
//
//            Toast.makeText(context, "Order successfully Submitted", Toast.LENGTH_SHORT).show();
//
//            startActivity(new Intent(context, PaymentSuccessfullyActivity.class));
//
//            overridePendingTransition(R.anim.slide_in, R.anim.nothing);
//
//        }
    }


}
