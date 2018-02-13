package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sqlitedatabase.AddressesModel;
import mall.kwik.kwikmall.sqlitedatabase.DBHelper;
import mall.kwik.kwikmall.sqlitedatabase.NewAddressModel;

public class ManageAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageManageAddrBack;
    private TextView tvmanageAddress,tvSavedAdd,tvNameOfCity,tvDescripAddr,tvDelete;
    private TextView btnAddNewAddresses;
    private DBHelper dbHelper;
    private AddressesModel addressesModel;
    private NewAddressModel newAddressModel;
    private RelativeLayout relativeLayoutAddress,relativeLayoutNewAddress;
    private TextView tvFlatNo,tvAddressNew,tvDeleteNewAdd;
    private Context context;
    private NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manage_address);

        findViewID();

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();


        dbHelper = new DBHelper(this);


        addressesModel = dbHelper.getAddress();
        newAddressModel = dbHelper.getNewAddress();

        if(newAddressModel==null){
            relativeLayoutNewAddress.setVisibility(View.GONE);

        }
        else if(newAddressModel!=null){
            tvFlatNo.setText(newAddressModel.getFlatno());
            tvAddressNew.setText(newAddressModel.getNewAddress());
        }

        if(addressesModel==null){
            relativeLayoutAddress.setVisibility(View.GONE);

        }

        else if(addressesModel!=null) {

            tvNameOfCity.setText(addressesModel.getCityname());
            tvDescripAddr.setText(addressesModel.getAddre());
        }

        clickListeners();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void clickListeners() {


        imageManageAddrBack.setOnClickListener(this);

        tvDelete.setOnClickListener(this);
        tvDeleteNewAdd.setOnClickListener(this);

        btnAddNewAddresses.setOnClickListener(this);
    }

    private void findViewID() {

        //ImageViews
        imageManageAddrBack = findViewById(R.id.imageManageAddrBack);

        //Text Views
        tvDelete = findViewById(R.id.tvDelete);

        //Buttons
        btnAddNewAddresses = findViewById(R.id.btnAddNewAddresses);

        //Text Views
        tvmanageAddress = findViewById(R.id.tvmanageAddress);
        tvSavedAdd = findViewById(R.id.tvSavedAdd);
        tvNameOfCity = findViewById(R.id.tvNameOfCity);
        tvDescripAddr = findViewById(R.id.tvDescripAddr);

        tvFlatNo = findViewById(R.id.tvFlatNo);
        tvAddressNew = findViewById(R.id.tvAddressNew);
        tvDeleteNewAdd = findViewById(R.id.tvDeleteNewAdd);



        //Relative Layout
        relativeLayoutAddress = findViewById(R.id.relativeLayoutAddress);
        relativeLayoutNewAddress = findViewById(R.id.relativeLayoutNewAddress);

        MaterialRippleLayout.on(btnAddNewAddresses)
                .rippleColor(Color.parseColor("#006400"))
                .rippleAlpha(0.5f)
                .rippleHover(true)
                .create();

        MaterialRippleLayout.on(tvDelete)
                .rippleColor(Color.parseColor("#fd923f"))
                .rippleAlpha(0.5f)
                .rippleHover(true)
                .create();


        MaterialRippleLayout.on(tvDeleteNewAdd)
                .rippleColor(Color.parseColor("#fd923f"))
                .rippleAlpha(0.5f)
                .rippleHover(true)
                .create();

    }

    @Override
    public void onBackPressed() {


        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

        } else {
            super.onBackPressed(); //replaced
        }



    }

    @Override
    public void onClick(View v) {

        if(v==imageManageAddrBack){


            if (getFragmentManager().getBackStackEntryCount() == 0) {
                this.finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

            } else {
                super.onBackPressed(); //replaced
            }
        }



        if(v==tvDelete){

            relativeLayoutAddress.setVisibility(View.GONE);
            dbHelper.deleteAllAddresses();

        }

        if(v==tvDeleteNewAdd){

            relativeLayoutNewAddress.setVisibility(View.GONE);
            dbHelper.deleteNewAddress();

        }

        if(v==btnAddNewAddresses){


            Intent intent = new Intent(ManageAddressActivity.this,SetDeliveryLocationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.nothing);



        }
    }
}
