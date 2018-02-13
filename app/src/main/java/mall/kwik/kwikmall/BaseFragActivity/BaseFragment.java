package mall.kwik.kwikmall.BaseFragActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.taishi.flipprogressdialog.FlipProgressDialog;

import java.util.ArrayList;
import java.util.List;

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

public class BaseFragment extends Fragment {

    @Inject
    public GitApiInterface apiService;


    protected ProgressDialog pDialog;

    protected android.app.AlertDialog alertDialog;

    @Inject
    public SharedPrefsHelper sharedPrefsHelper;

    public NoInternetDialog noInternetDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppController) getActivity().getApplication()).getComponent().inject(this);
    }


    public void alertLoading() {

        pDialog =new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(true);


        android.app.AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder =new android.app.AlertDialog.Builder(getActivity());

        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(
                "Ok",
                (dialog,id)->dialog.cancel());
        alertDialog =alertDialogBuilder.create();

    }


    public void showSuccessDialog(String title){

        new AwesomeSuccessDialog(getActivity())
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










    public void showAlertDialog(String action,String message){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
