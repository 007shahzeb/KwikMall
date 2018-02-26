package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.expresspaygh.api.ExpressPayApi;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;

public class EnterAddressActivity extends BaseActivity implements View.OnClickListener
                                ,ExpressPayApi.ExpressPayPaymentCompletionListener{


    private ImageView imagepaymenyBack;
    private TextView txtPayment,txtPlaceorder;
    private EditText edFullName,edCountryCode,edMobileNo,edEmailAddress,edAdd,edCity,edZipCode;
    private DBHelper dbHelper;
    private String name,countycode,contactno,emailAdd,addre,cityname,zipcode;
    private String totalAmt;
    private Context context;
    private NoInternetDialog noInternetDialog;
    static ExpressPayApi expressPayApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enteradd);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();


        findViewId();

        dbHelper = new DBHelper(this);


        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        totalAmt = bundle.getString("totalAmount");

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



        clickListeners();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);

/*
            MarkerDataPreference markerData = new MarkerDataPreference(this);
*/

            sharedPrefsHelper.put(AppConstants.DEL_LAT,String.valueOf(location.getLatitude()));
            sharedPrefsHelper.put(AppConstants.DEL_LONG,String.valueOf(location.getLongitude()));

    /*        markerData.setDeliveryAddLat(String.valueOf(location.getLatitude()));
            markerData.setDeliveryAddLong(String.valueOf(location.getLongitude()));*/


            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private void clickListeners() {

        imagepaymenyBack.setOnClickListener(this);
        txtPlaceorder.setOnClickListener(this);
    }

    private void findViewId() {

        imagepaymenyBack = findViewById(R.id.imagepaymenyBack);
        txtPayment = findViewById(R.id.txtPayment);
        txtPlaceorder = findViewById(R.id.txtPlaceorder);

        //Edit Text Views
        edFullName = findViewById(R.id.edFullName);
        edCountryCode = findViewById(R.id.edCountryCode);
        edMobileNo = findViewById(R.id.edMobileNo);
        edEmailAddress = findViewById(R.id.edEmailAddress);
        edAdd = findViewById(R.id.edAdd);
        edCity = findViewById(R.id.edCity);
        edZipCode = findViewById(R.id.edZipCode);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

    }

    @Override
    public void onClick(View v) {

        if(v==imagepaymenyBack){

            Intent intent = new Intent(EnterAddressActivity.this,FragmentsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            finish();


        }

        if(v==txtPlaceorder){


            /*//Validation for Blank Field
            if(edFullName.getText().toString().length()==0)
            {
                edFullName.setError("Enter full name");
                return;
            }
            else if(edCountryCode.getText().toString().length()==0)
            {
                edCountryCode.setError("Enter country code");
                return;
            }
            else if(edMobileNo.getText().toString().length()==0)
            {
                edMobileNo.setError("Enter mobile no");
                return;
            }
            else if(edEmailAddress.getText().toString().length()==0)
            {
                edEmailAddress.setError("Enter Email");
                return;
            }
            else if(edAdd.getText().toString().length()==0)
            {
                edAdd.setError("Enter Address");
                return;
            }
            else if(edCity.getText().toString().length()==0)
            {
                edCity.setError("Enter City Name");
                return;
            }
            else if(edZipCode.getText().toString().length()==0)
            {
                edZipCode.setError("Enter Zip Code");
                return;
            }
            else
            {
*/
            flipProgress();




            name = edFullName.getText().toString();
            countycode = edCountryCode.getText().toString();
            contactno = edMobileNo.getText().toString();
            emailAdd = edEmailAddress.getText().toString();
            addre = edAdd.getText().toString();
            cityname = edCity.getText().toString();
            zipcode = edZipCode.getText().toString();

            getLocationFromAddress(this,addre);


            dbHelper.insertAddress(name,countycode,contactno,emailAdd,addre,cityname,zipcode);


            pay();



              /*  Bundle bundle = new Bundle();

                bundle.putString("totalAmount",totalAmt);


                Intent intent = new Intent(EnterAddressActivity.this,PaymentMethodActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in, R.anim.nothing);*/

            //   }




        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null)
            expressPayApi.onActivityResult(this, requestCode, resultCode, data);

        fpd.dismiss();

    }


    public void pay(){
        /**
         * Make a request to your server to get a token
         * For this demo we have a sample server which we make the request to.
         * url: https://sandbox.expresspaygh.com/api/server.php
         * In Dev: Use amount 1.00 to simulate a failed transaction and greater than or equals 2.00 for a successful transaction
         */

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("request","submit");
        params.put("order_id", "82373");
        params.put("currency", "GHS");
        params.put("amount", totalAmt);
        params.put("order_desc", "Food Items");
        params.put("user_name","testapi@expresspaygh.com");
        params.put("first_name","Test");
        params.put("last_name","Api");
        params.put("email","testapi@expresspaygh.com");
        params.put("phone_number","233244123123");
        params.put("account_number","233244123123");




        expressPayApi.submit(params, EnterAddressActivity.this, new ExpressPayApi.ExpressPaySubmitCompletionListener() {
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



}
