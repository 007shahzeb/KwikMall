package mall.kwik.kwikmall.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by dharamveer on 29/1/18.
 */

public class BasePresenter {

    protected ProgressDialog pDialog;

    protected AlertDialog alertDialog;

    public BasePresenter(Context context) {

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(true);


        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton(
                "Ok",
                (dialog, id) -> dialog.cancel());
        alertDialog = alertDialogBuilder.create();


    }

    protected void showDialog(String message) {


    }

}
