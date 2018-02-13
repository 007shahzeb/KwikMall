package mall.kwik.kwikmall.BaseFragActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;

import javax.inject.Inject;

import am.appwise.components.ni.NoInternetDialog;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.activities.aplicationclass.AppController;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.manager.GitApiInterface;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by dharamveer on 29/1/18.
 */

public class BaseActivity extends AppCompatActivity {


    @Inject
    public GitApiInterface apiService;

    @Inject
    public SharedPrefsHelper sharedPrefsHelper;

    protected Unbinder unbinder;

    public NoInternetDialog noInternetDialog;

    @Override
    public void setContentView(int layoutRedID) {
        super.setContentView(layoutRedID);
        ((AppController) getApplication()).getComponent().inject(this);
        unbinder = ButterKnife.bind(this);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }



    public void showSuccessDialog(String title){

        new AwesomeSuccessDialog(this)
                .setTitle(title)
                .setMessage("Successfully")
                .setColoredCircle(R.color.green)
                .setDialogIconAndColor(R.drawable.checkwhite, R.color.white)
                .setCancelable(true)
                .setPositiveButtonText(getString(R.string.dialog_yes_button))
                .setPositiveButtonbackgroundColor(R.color.red)
                .setPositiveButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {


                    }
                }).show();

    }




    public void navigateToNextActivity(Class cla){

        startActivity(new Intent(BaseActivity.this,cla));
        overridePendingTransition(R.anim.enter, R.anim.exit);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((AppController) getApplication()).getComponent().inject(this);
        super.onCreate(savedInstanceState);
        noInternetDialog =  new NoInternetDialog.Builder(BaseActivity.this).build();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        noInternetDialog.onDestroy();
    }



    public void showAlertDialog(String action,String message){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                action,


                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();


    }




}

