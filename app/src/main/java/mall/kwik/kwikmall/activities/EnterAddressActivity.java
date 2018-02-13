package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.BaseFragActivity.BaseActivity;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sharedpreferences.MarkerDataPreference;

public class EnterAddressActivity extends BaseActivity implements View.OnClickListener {


    private ImageView imagepaymenyBack;
    private TextView txtPayment,txtPlaceorder;
    private EditText edFullName,edCountryCode,edMobileNo,edEmailAddress,edAdd,edCity,edZipCode;
    private DBHelper dbHelper;
    private String name,countycode,contactno,emailAdd,addre,cityname,zipcode;
    private String totalAmt;

    private Context context;
    private NoInternetDialog noInternetDialog;


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

            //Validation for Blank Field
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

                name = edFullName.getText().toString();
                countycode = edCountryCode.getText().toString();
                contactno = edMobileNo.getText().toString();
                emailAdd = edEmailAddress.getText().toString();
                addre = edAdd.getText().toString();
                cityname = edCity.getText().toString();
                zipcode = edZipCode.getText().toString();

                getLocationFromAddress(this,addre);


                dbHelper.insertAddress(name,countycode,contactno,emailAdd,addre,cityname,zipcode);

                Bundle bundle = new Bundle();

                bundle.putString("totalAmount",totalAmt);

                Intent intent = new Intent(EnterAddressActivity.this,PaymentMethodActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in, R.anim.nothing);

            }




        }


    }
}
