package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rilixtech.CountryCodePicker;

import java.util.HashMap;

import am.appwise.components.ni.NoInternetDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import mall.kwik.kwikmall.AppConstants;
import mall.kwik.kwikmall.apiresponse.ForgetPassword.ForgetPasswordSuccess;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.dialogs.EnterOTPDialog;

public class ForgetPasswordActivity extends BaseActivity {


    private EditText edPhoneno;
    private ImageView btnForgetSubmit;
    private String otp;
    private Context context;
    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    ProgressBar dialogProgressBar;
    CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();


        edPhoneno = findViewById(R.id.edPhoneno);
        dialogProgressBar = findViewById(R.id.dialog_progress_bar);
        ccp =  findViewById(R.id.ccp);


        btnForgetSubmit = findViewById(R.id.btnForgetSubmit);

        btnForgetSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialogProgressBar.setVisibility(View.VISIBLE);


                forgetPasswordApi();


            }
        });


    }




    private void forgetPasswordApi() {

        if(edPhoneno.getText().toString()==null){

            edPhoneno.setError("Enter phone no");
        }


        ccp.registerPhoneNumberTextView(edPhoneno);


        String codeWithPh = ccp.getFullNumberWithPlus();



        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("phoneNo",codeWithPh);


        compositeDisposable.add(apiService.forgetPassword(stringStringHashMap)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ForgetPasswordSuccess>() {
                    @Override
                    public void accept(ForgetPasswordSuccess forgetPasswordSuccess) throws Exception {


                        if(forgetPasswordSuccess.getIsSuccess()){


                            dialogProgressBar.setVisibility(View.GONE);


                            sharedPrefsHelper.put(AppConstants.PHONEWITHCCP,codeWithPh);

                            EnterOTPDialog enterOTPDialog = new EnterOTPDialog(ForgetPasswordActivity.this);
                            enterOTPDialog.show();

                        }
                        else {
                            dialogProgressBar.setVisibility(View.GONE);


                            showAlertDialog("Retry",forgetPasswordSuccess.getMessage());

                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dialogProgressBar.setVisibility(View.GONE);

                        compositeDisposable.dispose();
                        showAlertDialog("Retry",throwable.getMessage());



                    }
                }));



    }

}





/*
    private void forgetPasswordApi() {

        if(edEmailID.getText().toString()==null){

            edEmailID.setError("Enter Email Id");
        }


        final String emailId = edEmailID.getText().toString();

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("email",emailId);


        compositeDisposable.add(apiService.forgetPassword(stringStringHashMap)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ForgetPasswordSuccess>() {
                    @Override
                    public void accept(ForgetPasswordSuccess forgetPasswordSuccess) throws Exception {


                        if(forgetPasswordSuccess.getIsSuccess()){

                            // get prompts.xml view
                            LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                            View promptsView = li.inflate(R.layout.prompts, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ForgetPasswordActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text

                                            otp = String.valueOf(userInput.getText());

                                            HashMap<String, String> stringStringHashMap = new HashMap<>();
                                            stringStringHashMap.put("email",emailId);
                                            stringStringHashMap.put("OTP",otp);


                                            compositeDisposable.add(apiService.validateOTP(stringStringHashMap)

                                                    .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<ValidateOTPSuccess>() {
                                                        @Override
                                                        public void accept(ValidateOTPSuccess validateOTPSuccess) throws Exception {

                                                            if(validateOTPSuccess.getIsSuccess()){

                                                                // get prompts.xml view
                                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                        ForgetPasswordActivity.this);

                                                                // set prompts.xml to alertdialog builder
                                                                alertDialogBuilder.setView(promptsView);

                                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                                alertDialogBuilder.setCancelable(false)
                                                                        .setPositiveButton("OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                                        stringStringHashMap.put("password",newpsw.getText().toString());



                                                                                        compositeDisposable.add(apiService.updatePassword(stringStringHashMap)
                                                                                                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                                .subscribe(new Consumer<UpdatePassSuccess>() {
                                                                                                    @Override
                                                                                                    public void accept(UpdatePassSuccess updatePassSuccess) throws Exception {

                                                                                                        if(updatePassSuccess.getSuccess()){


                                                                                                            showSuccessDialog("Password Updated");
                                                                                                            Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                            startActivity(intent);
                                                                                                            overridePendingTransition(R.anim.enter, R.anim.exit);



                                                                                                        }else {

                                                                                                        }

                                                                                                    }
                                                                                                }, new Consumer<Throwable>() {
                                                                                                    @Override
                                                                                                    public void accept(Throwable throwable) throws Exception {

                                                                                                        showAlertDialog("Retry",throwable.getMessage());

                                                                                                    }
                                                                                                }));

                                                                                    }
                                                            else {

                                                                                    }

                                                                                }
                                                            }, new Consumer<Throwable>() {
                                                                @Override
                                                                public void accept(Throwable throwable) throws Exception {

                                                                    showAlertDialog("Retry",throwable.getMessage());


                                                                }
                                                            }));





                        else {


                                                            }
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {


                                                            showAlertDialog("Retry",throwable.getMessage());
                                                        }
                                                    }));




*/
/*
        restClient.forgetPassword(stringStringHashMap).enqueue(new Callback<ForgetPasswordSuccess>() {
            @Override
            public void onResponse(Call<ForgetPasswordSuccess> call, Response<ForgetPasswordSuccess> response) {

                if(response.body().getIsSuccess()){


                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
          /*
    private void forgetPasswordApi() {

        if(edEmailID.getText().toString()==null){

            edEmailID.setError("Enter Email Id");
        }


        final String emailId = edEmailID.getText().toString();

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("email",emailId);


        compositeDisposable.add(apiService.forgetPassword(stringStringHashMap)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ForgetPasswordSuccess>() {
                    @Override
                    public void accept(ForgetPasswordSuccess forgetPasswordSuccess) throws Exception {


                        if(forgetPasswordSuccess.getIsSuccess()){

                            // get prompts.xml view
                            LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                            View promptsView = li.inflate(R.layout.prompts, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ForgetPasswordActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text

                                            otp = String.valueOf(userInput.getText());

                                            HashMap<String, String> stringStringHashMap = new HashMap<>();
                                            stringStringHashMap.put("email",emailId);
                                            stringStringHashMap.put("OTP",otp);


                                            compositeDisposable.add(apiService.validateOTP(stringStringHashMap)

                                                    .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<ValidateOTPSuccess>() {
                                                        @Override
                                                        public void accept(ValidateOTPSuccess validateOTPSuccess) throws Exception {

                                                            if(validateOTPSuccess.getIsSuccess()){

                                                                // get prompts.xml view
                                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                        ForgetPasswordActivity.this);

                                                                // set prompts.xml to alertdialog builder
                                                                alertDialogBuilder.setView(promptsView);

                                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                                alertDialogBuilder.setCancelable(false)
                                                                        .setPositiveButton("OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                                        stringStringHashMap.put("password",newpsw.getText().toString());



                                                                                        compositeDisposable.add(apiService.updatePassword(stringStringHashMap)
                                                                                                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                                .subscribe(new Consumer<UpdatePassSuccess>() {
                                                                                                    @Override
                                                                                                    public void accept(UpdatePassSuccess updatePassSuccess) throws Exception {

                                                                                                        if(updatePassSuccess.getSuccess()){


                                                                                                            showSuccessDialog("Password Updated");
                                                                                                            Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                            startActivity(intent);
                                                                                                            overridePendingTransition(R.anim.enter, R.anim.exit);



                                                                                                        }else {

                                                                                                        }

                                                                                                    }
                                                                                                }, new Consumer<Throwable>() {
                                                                                                    @Override
                                                                                                    public void accept(Throwable throwable) throws Exception {

                                                                                                        showAlertDialog("Retry",throwable.getMessage());

                                                                                                    }
                                                                                                }));

                                                                                    }
                                                            else {

                                                                                    }

                                                                                }
                                                            }, new Consumer<Throwable>() {
                                                                @Override
                                                                public void accept(Throwable throwable) throws Exception {

                                                                    showAlertDialog("Retry",throwable.getMessage());


                                                                }
                                                            }));





                        else {


                                                            }
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {


                                                            showAlertDialog("Retry",throwable.getMessage());
                                                        }
                                                    }));



*/
/*
        restClient.forgetPassword(stringStringHashMap).enqueue(new Callback<ForgetPasswordSuccess>() {
            @Override
            public void onResponse(Call<ForgetPasswordSuccess> call, Response<ForgetPasswordSuccess> response) {

                if(response.body().getIsSuccess()){


                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ForgetPasswordActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text

                                    otp = String.valueOf(userInput.getText());

                                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                                    stringStringHashMap.put("email",emailId);
                                    stringStringHashMap.put("OTP",otp);


                                    restClient.validateOTP(stringStringHashMap).enqueue(new Callback<ValidateOTPSuccess>() {
                                        @Override
                                        public void onResponse(Call<ValidateOTPSuccess> call, Response<ValidateOTPSuccess> response) {

                                            if(response.body().getIsSuccess()){


                                                // get prompts.xml view
                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        ForgetPasswordActivity.this);

                                                // set prompts.xml to alertdialog builder
                                                alertDialogBuilder.setView(promptsView);

                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                alertDialogBuilder.setCancelable(false)
                                                        .setPositiveButton("OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                        stringStringHashMap.put("password",newpsw.getText().toString());


                                                                        restClient.updatePassword(stringStringHashMap).enqueue(new Callback<UpdatePassSuccess>() {
                                                                            @Override
                                                                            public void onResponse(Call<UpdatePassSuccess> call, Response<UpdatePassSuccess> response) {


                                                                                if(response.body().getSuccess()){

                                                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                    builder1.setMessage(response.body().getMessage());
                                                                                    builder1.setCancelable(true);

                                                                                    builder1.setPositiveButton(
                                                                                            "Yes",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {

                                                                                                    Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                    startActivity(intent);
                                                                                                    overridePendingTransition(R.anim.enter, R.anim.exit);

                                                                                                }
                                                                                            });

                                                                                    builder1.setNegativeButton(
                                                                                            "No",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                                    dialog.cancel();
                                                                                                }
                                                                                            });

                                                                                    AlertDialog alert11 = builder1.create();
                                                                                    alert11.show();


                                                                                }

                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<UpdatePassSuccess> call, Throwable t) {
                                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                builder1.setMessage(t.getMessage());
                                                                                builder1.setCancelable(true);

                                                                                builder1.setPositiveButton(
                                                                                        "Cancel",
                                                                                        new DialogInterface.OnClickListener() {
                                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                                dialog.cancel();
                                                                                            }
                                                                                        });



                                                                                AlertDialog alert11 = builder1.create();
                                                                                alert11.show();

                                                                            }
                                                                        });



                                                                    }
                                                                })
                                                        .setNegativeButton("Cancel",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog,int id) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();

                                                // show it
                                                alertDialog.show();




                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ValidateOTPSuccess> call, Throwable t) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                            builder1.setMessage(t.getMessage());
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });



                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();


                                        }
                                    });


                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();







                }

            }

            @Override
            public void onFailure(Call<ForgetPasswordSuccess> call, Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                builder1.setMessage(t.getMessage());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });



                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
*//*
*/
/*



                                        }


                                        @Override
                                        protected void onDestroy() {
                                            super.onDestroy();
                                            noInternetDialog.onDestroy();
                                            compositeDisposable.dispose();
                                        }





                                        @Override
                                        public void onBackPressed() {
                                            super.onBackPressed();

                                            finish();
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                                        }

                                    }
*//*

          View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ForgetPasswordActivity.this);
/*
    private void forgetPasswordApi() {

        if(edEmailID.getText().toString()==null){

            edEmailID.setError("Enter Email Id");
        }


        final String emailId = edEmailID.getText().toString();

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("email",emailId);


        compositeDisposable.add(apiService.forgetPassword(stringStringHashMap)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ForgetPasswordSuccess>() {
                    @Override
                    public void accept(ForgetPasswordSuccess forgetPasswordSuccess) throws Exception {


                        if(forgetPasswordSuccess.getIsSuccess()){

                            // get prompts.xml view
                            LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                            View promptsView = li.inflate(R.layout.prompts, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ForgetPasswordActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text

                                            otp = String.valueOf(userInput.getText());

                                            HashMap<String, String> stringStringHashMap = new HashMap<>();
                                            stringStringHashMap.put("email",emailId);
                                            stringStringHashMap.put("OTP",otp);


                                            compositeDisposable.add(apiService.validateOTP(stringStringHashMap)

                                                    .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<ValidateOTPSuccess>() {
                                                        @Override
                                                        public void accept(ValidateOTPSuccess validateOTPSuccess) throws Exception {

                                                            if(validateOTPSuccess.getIsSuccess()){

                                                                // get prompts.xml view
                                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                        ForgetPasswordActivity.this);

                                                                // set prompts.xml to alertdialog builder
                                                                alertDialogBuilder.setView(promptsView);

                                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                                alertDialogBuilder.setCancelable(false)
                                                                        .setPositiveButton("OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                                        stringStringHashMap.put("password",newpsw.getText().toString());



                                                                                        compositeDisposable.add(apiService.updatePassword(stringStringHashMap)
                                                                                                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                                .subscribe(new Consumer<UpdatePassSuccess>() {
                                                                                                    @Override
                                                                                                    public void accept(UpdatePassSuccess updatePassSuccess) throws Exception {

                                                                                                        if(updatePassSuccess.getSuccess()){


                                                                                                            showSuccessDialog("Password Updated");
                                                                                                            Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                            startActivity(intent);
                                                                                                            overridePendingTransition(R.anim.enter, R.anim.exit);



                                                                                                        }else {

                                                                                                        }

                                                                                                    }
                                                                                                }, new Consumer<Throwable>() {
                                                                                                    @Override
                                                                                                    public void accept(Throwable throwable) throws Exception {

                                                                                                        showAlertDialog("Retry",throwable.getMessage());

                                                                                                    }
                                                                                                }));

                                                                                    }
                                                            else {

                                                                                    }

                                                                                }
                                                            }, new Consumer<Throwable>() {
                                                                @Override
                                                                public void accept(Throwable throwable) throws Exception {

                                                                    showAlertDialog("Retry",throwable.getMessage());


                                                                }
                                                            }));





                        else {


                                                            }
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {


                                                            showAlertDialog("Retry",throwable.getMessage());
                                                        }
                                                    }));



*/
/*
        restClient.forgetPassword(stringStringHashMap).enqueue(new Callback<ForgetPasswordSuccess>() {
            @Override
            public void onResponse(Call<ForgetPasswordSuccess> call, Response<ForgetPasswordSuccess> response) {

                if(response.body().getIsSuccess()){


                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ForgetPasswordActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text

                                    otp = String.valueOf(userInput.getText());

                                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                                    stringStringHashMap.put("email",emailId);
                                    stringStringHashMap.put("OTP",otp);


                                    restClient.validateOTP(stringStringHashMap).enqueue(new Callback<ValidateOTPSuccess>() {
                                        @Override
                                        public void onResponse(Call<ValidateOTPSuccess> call, Response<ValidateOTPSuccess> response) {

                                            if(response.body().getIsSuccess()){


                                                // get prompts.xml view
                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        ForgetPasswordActivity.this);

                                                // set prompts.xml to alertdialog builder
                                                alertDialogBuilder.setView(promptsView);

                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                alertDialogBuilder.setCancelable(false)
                                                        .setPositiveButton("OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                        stringStringHashMap.put("password",newpsw.getText().toString());


                                                                        restClient.updatePassword(stringStringHashMap).enqueue(new Callback<UpdatePassSuccess>() {
                                                                            @Override
                                                                            public void onResponse(Call<UpdatePassSuccess> call, Response<UpdatePassSuccess> response) {


                                                                                if(response.body().getSuccess()){

                                                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                    builder1.setMessage(response.body().getMessage());
                                                                                    builder1.setCancelable(true);

                                                                                    builder1.setPositiveButton(
                                                                                            "Yes",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {

                                                                                                    Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                    startActivity(intent);
                                                                                                    overridePendingTransition(R.anim.enter, R.anim.exit);

                                                                                                }
                                                                                            });

                                                                                    builder1.setNegativeButton(
                                                                                            "No",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                                    dialog.cancel();
                                                                                                }
                                                                                            });

                                                                                    AlertDialog alert11 = builder1.create();
                                                                                    alert11.show();


                                                                                }

                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<UpdatePassSuccess> call, Throwable t) {
                                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                builder1.setMessage(t.getMessage());
                                                                                builder1.setCancelable(true);

                                                                                builder1.setPositiveButton(
                                                                                        "Cancel",
                                                                                        new DialogInterface.OnClickListener() {
                                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                                dialog.cancel();
                                                                                            }
                                                                                        });



                                                                                AlertDialog alert11 = builder1.create();
                                                                                alert11.show();

                                                                            }
                                                                        });



                                                                    }
                                                                })
                                                        .setNegativeButton("Cancel",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog,int id) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();

                                                // show it
                                                alertDialog.show();




                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ValidateOTPSuccess> call, Throwable t) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                            builder1.setMessage(t.getMessage());
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });



                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();


                                        }
                                    });


                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();







                }

            }

            @Override
            public void onFailure(Call<ForgetPasswordSuccess> call, Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                builder1.setMessage(t.getMessage());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });



                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
*//*
*/
/*



                                        }


                                        @Override
                                        protected void onDestroy() {
                                            super.onDestroy();
                                            noInternetDialog.onDestroy();
                                            compositeDisposable.dispose();
                                        }





                                        @Override
                                        public void onBackPressed() {
                                            super.onBackPressed();

                                            finish();
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                                        }

                                    }
*//*


                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text
/*
    private void forgetPasswordApi() {

        if(edEmailID.getText().toString()==null){

            edEmailID.setError("Enter Email Id");
        }


        final String emailId = edEmailID.getText().toString();

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("email",emailId);


        compositeDisposable.add(apiService.forgetPassword(stringStringHashMap)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ForgetPasswordSuccess>() {
                    @Override
                    public void accept(ForgetPasswordSuccess forgetPasswordSuccess) throws Exception {


                        if(forgetPasswordSuccess.getIsSuccess()){

                            // get prompts.xml view
                            LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                            View promptsView = li.inflate(R.layout.prompts, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ForgetPasswordActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text

                                            otp = String.valueOf(userInput.getText());

                                            HashMap<String, String> stringStringHashMap = new HashMap<>();
                                            stringStringHashMap.put("email",emailId);
                                            stringStringHashMap.put("OTP",otp);


                                            compositeDisposable.add(apiService.validateOTP(stringStringHashMap)

                                                    .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<ValidateOTPSuccess>() {
                                                        @Override
                                                        public void accept(ValidateOTPSuccess validateOTPSuccess) throws Exception {

                                                            if(validateOTPSuccess.getIsSuccess()){

                                                                // get prompts.xml view
                                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                        ForgetPasswordActivity.this);

                                                                // set prompts.xml to alertdialog builder
                                                                alertDialogBuilder.setView(promptsView);

                                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                                alertDialogBuilder.setCancelable(false)
                                                                        .setPositiveButton("OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                                        stringStringHashMap.put("password",newpsw.getText().toString());



                                                                                        compositeDisposable.add(apiService.updatePassword(stringStringHashMap)
                                                                                                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                                .subscribe(new Consumer<UpdatePassSuccess>() {
                                                                                                    @Override
                                                                                                    public void accept(UpdatePassSuccess updatePassSuccess) throws Exception {

                                                                                                        if(updatePassSuccess.getSuccess()){


                                                                                                            showSuccessDialog("Password Updated");
                                                                                                            Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                            startActivity(intent);
                                                                                                            overridePendingTransition(R.anim.enter, R.anim.exit);



                                                                                                        }else {

                                                                                                        }

                                                                                                    }
                                                                                                }, new Consumer<Throwable>() {
                                                                                                    @Override
                                                                                                    public void accept(Throwable throwable) throws Exception {

                                                                                                        showAlertDialog("Retry",throwable.getMessage());

                                                                                                    }
                                                                                                }));

                                                                                    }
                                                            else {

                                                                                    }

                                                                                }
                                                            }, new Consumer<Throwable>() {
                                                                @Override
                                                                public void accept(Throwable throwable) throws Exception {

                                                                    showAlertDialog("Retry",throwable.getMessage());


                                                                }
                                                            }));





                        else {


                                                            }
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {


                                                            showAlertDialog("Retry",throwable.getMessage());
                                                        }
                                                    }));



*/
/*
        restClient.forgetPassword(stringStringHashMap).enqueue(new Callback<ForgetPasswordSuccess>() {
            @Override
            public void onResponse(Call<ForgetPasswordSuccess> call, Response<ForgetPasswordSuccess> response) {

                if(response.body().getIsSuccess()){


                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ForgetPasswordActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text

                                    otp = String.valueOf(userInput.getText());

                                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                                    stringStringHashMap.put("email",emailId);
                                    stringStringHashMap.put("OTP",otp);


                                    restClient.validateOTP(stringStringHashMap).enqueue(new Callback<ValidateOTPSuccess>() {
                                        @Override
                                        public void onResponse(Call<ValidateOTPSuccess> call, Response<ValidateOTPSuccess> response) {

                                            if(response.body().getIsSuccess()){


                                                // get prompts.xml view
                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        ForgetPasswordActivity.this);

                                                // set prompts.xml to alertdialog builder
                                                alertDialogBuilder.setView(promptsView);

                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                alertDialogBuilder.setCancelable(false)
                                                        .setPositiveButton("OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                        stringStringHashMap.put("password",newpsw.getText().toString());


                                                                        restClient.updatePassword(stringStringHashMap).enqueue(new Callback<UpdatePassSuccess>() {
                                                                            @Override
                                                                            public void onResponse(Call<UpdatePassSuccess> call, Response<UpdatePassSuccess> response) {


                                                                                if(response.body().getSuccess()){

                                                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                    builder1.setMessage(response.body().getMessage());
                                                                                    builder1.setCancelable(true);

                                                                                    builder1.setPositiveButton(
                                                                                            "Yes",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {

                                                                                                    Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                    startActivity(intent);
                                                                                                    overridePendingTransition(R.anim.enter, R.anim.exit);

                                                                                                }
                                                                                            });

                                                                                    builder1.setNegativeButton(
                                                                                            "No",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                                    dialog.cancel();
                                                                                                }
                                                                                            });

                                                                                    AlertDialog alert11 = builder1.create();
                                                                                    alert11.show();


                                                                                }

                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<UpdatePassSuccess> call, Throwable t) {
                                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                builder1.setMessage(t.getMessage());
                                                                                builder1.setCancelable(true);

                                                                                builder1.setPositiveButton(
                                                                                        "Cancel",
                                                                                        new DialogInterface.OnClickListener() {
                                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                                dialog.cancel();
                                                                                            }
                                                                                        });



                                                                                AlertDialog alert11 = builder1.create();
                                                                                alert11.show();

                                                                            }
                                                                        });



                                                                    }
                                                                })
                                                        .setNegativeButton("Cancel",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog,int id) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();

                                                // show it
                                                alertDialog.show();




                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ValidateOTPSuccess> call, Throwable t) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                            builder1.setMessage(t.getMessage());
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });



                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();


                                        }
                                    });


                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();







                }

            }

            @Override
            public void onFailure(Call<ForgetPasswordSuccess> call, Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                builder1.setMessage(t.getMessage());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });



                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
*//*
*/
/*



                                        }


                                        @Override
                                        protected void onDestroy() {
                                            super.onDestroy();
                                            noInternetDialog.onDestroy();
                                            compositeDisposable.dispose();
                                        }





                                        @Override
                                        public void onBackPressed() {
                                            super.onBackPressed();

                                            finish();
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                                        }

                                    }
*//*


                                    otp = String.valueOf(userInput.getText());

                                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                                    stringStringHashMap.put("email",emailId);
                                    stringStringHashMap.put("OTP",otp);


                                    restClient.validateOTP(stringStringHashMap).enqueue(new Callback<ValidateOTPSuccess>() {
                                        @Override
                                        public void onResponse(Call<ValidateOTPSuccess> call, Response<ValidateOTPSuccess> response) {

                                            if(response.body().getIsSuccess()){
/*
    private void forgetPasswordApi() {

        if(edEmailID.getText().toString()==null){

            edEmailID.setError("Enter Email Id");
        }


        final String emailId = edEmailID.getText().toString();

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("email",emailId);


        compositeDisposable.add(apiService.forgetPassword(stringStringHashMap)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ForgetPasswordSuccess>() {
                    @Override
                    public void accept(ForgetPasswordSuccess forgetPasswordSuccess) throws Exception {


                        if(forgetPasswordSuccess.getIsSuccess()){

                            // get prompts.xml view
                            LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                            View promptsView = li.inflate(R.layout.prompts, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ForgetPasswordActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text

                                            otp = String.valueOf(userInput.getText());

                                            HashMap<String, String> stringStringHashMap = new HashMap<>();
                                            stringStringHashMap.put("email",emailId);
                                            stringStringHashMap.put("OTP",otp);


                                            compositeDisposable.add(apiService.validateOTP(stringStringHashMap)

                                                    .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<ValidateOTPSuccess>() {
                                                        @Override
                                                        public void accept(ValidateOTPSuccess validateOTPSuccess) throws Exception {

                                                            if(validateOTPSuccess.getIsSuccess()){

                                                                // get prompts.xml view
                                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                        ForgetPasswordActivity.this);

                                                                // set prompts.xml to alertdialog builder
                                                                alertDialogBuilder.setView(promptsView);

                                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                                alertDialogBuilder.setCancelable(false)
                                                                        .setPositiveButton("OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                                        stringStringHashMap.put("password",newpsw.getText().toString());



                                                                                        compositeDisposable.add(apiService.updatePassword(stringStringHashMap)
                                                                                                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                                .subscribe(new Consumer<UpdatePassSuccess>() {
                                                                                                    @Override
                                                                                                    public void accept(UpdatePassSuccess updatePassSuccess) throws Exception {

                                                                                                        if(updatePassSuccess.getSuccess()){


                                                                                                            showSuccessDialog("Password Updated");
                                                                                                            Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                            startActivity(intent);
                                                                                                            overridePendingTransition(R.anim.enter, R.anim.exit);



                                                                                                        }else {

                                                                                                        }

                                                                                                    }
                                                                                                }, new Consumer<Throwable>() {
                                                                                                    @Override
                                                                                                    public void accept(Throwable throwable) throws Exception {

                                                                                                        showAlertDialog("Retry",throwable.getMessage());

                                                                                                    }
                                                                                                }));

                                                                                    }
                                                            else {

                                                                                    }

                                                                                }
                                                            }, new Consumer<Throwable>() {
                                                                @Override
                                                                public void accept(Throwable throwable) throws Exception {

                                                                    showAlertDialog("Retry",throwable.getMessage());


                                                                }
                                                            }));





                        else {


                                                            }
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {


                                                            showAlertDialog("Retry",throwable.getMessage());
                                                        }
                                                    }));



*/
/*
        restClient.forgetPassword(stringStringHashMap).enqueue(new Callback<ForgetPasswordSuccess>() {
            @Override
            public void onResponse(Call<ForgetPasswordSuccess> call, Response<ForgetPasswordSuccess> response) {

                if(response.body().getIsSuccess()){


                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ForgetPasswordActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text

                                    otp = String.valueOf(userInput.getText());

                                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                                    stringStringHashMap.put("email",emailId);
                                    stringStringHashMap.put("OTP",otp);


                                    restClient.validateOTP(stringStringHashMap).enqueue(new Callback<ValidateOTPSuccess>() {
                                        @Override
                                        public void onResponse(Call<ValidateOTPSuccess> call, Response<ValidateOTPSuccess> response) {

                                            if(response.body().getIsSuccess()){


                                                // get prompts.xml view
                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        ForgetPasswordActivity.this);

                                                // set prompts.xml to alertdialog builder
                                                alertDialogBuilder.setView(promptsView);

                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                alertDialogBuilder.setCancelable(false)
                                                        .setPositiveButton("OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                        stringStringHashMap.put("password",newpsw.getText().toString());


                                                                        restClient.updatePassword(stringStringHashMap).enqueue(new Callback<UpdatePassSuccess>() {
                                                                            @Override
                                                                            public void onResponse(Call<UpdatePassSuccess> call, Response<UpdatePassSuccess> response) {


                                                                                if(response.body().getSuccess()){

                                                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                    builder1.setMessage(response.body().getMessage());
                                                                                    builder1.setCancelable(true);

                                                                                    builder1.setPositiveButton(
                                                                                            "Yes",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {

                                                                                                    Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                    startActivity(intent);
                                                                                                    overridePendingTransition(R.anim.enter, R.anim.exit);

                                                                                                }
                                                                                            });

                                                                                    builder1.setNegativeButton(
                                                                                            "No",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                                    dialog.cancel();
                                                                                                }
                                                                                            });

                                                                                    AlertDialog alert11 = builder1.create();
                                                                                    alert11.show();


                                                                                }

                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<UpdatePassSuccess> call, Throwable t) {
                                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                builder1.setMessage(t.getMessage());
                                                                                builder1.setCancelable(true);

                                                                                builder1.setPositiveButton(
                                                                                        "Cancel",
                                                                                        new DialogInterface.OnClickListener() {
                                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                                dialog.cancel();
                                                                                            }
                                                                                        });



                                                                                AlertDialog alert11 = builder1.create();
                                                                                alert11.show();

                                                                            }
                                                                        });



                                                                    }
                                                                })
                                                        .setNegativeButton("Cancel",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog,int id) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();

                                                // show it
                                                alertDialog.show();




                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ValidateOTPSuccess> call, Throwable t) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                            builder1.setMessage(t.getMessage());
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });



                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();


                                        }
                                    });


                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();







                }

            }

            @Override
            public void onFailure(Call<ForgetPasswordSuccess> call, Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                builder1.setMessage(t.getMessage());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });



                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
*//*
*/
/*



                                        }


                                        @Override
                                        protected void onDestroy() {
                                            super.onDestroy();
                                            noInternetDialog.onDestroy();
                                            compositeDisposable.dispose();
                                        }





                                        @Override
                                        public void onBackPressed() {
                                            super.onBackPressed();

                                            finish();
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                                        }

                                    }
*//*



                                                // get prompts.xml view
                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        ForgetPasswordActivity.this);

                                                // set prompts.xml to alertdialog builder
                                                alertDialogBuilder.setView(promptsView);

                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                alertDialogBuilder.setCancelable(false)
                                                        .setPositiveButton("OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                        stringStringHashMap.put("password",newpsw.getText().toString());


                                                                        restClient.updatePassword(stringStringHashMap).enqueue(new Callback<UpdatePassSuccess>() {
                                                                            @Override
                                                                            public void onResponse(Call<UpdatePassSuccess> call, Response<UpdatePassSuccess> response) {


                                                                                if(response.body().getSuccess()){

                                                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                    builder1.setMessage(response.body().getMessage());
                                                                                    builder1.setCancelable(true);

                                                                                    builder1.setPositiveButton(
                                                                                            "Yes",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {

                                                                                                    Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                    startActivity(intent);
                                                                                                    overridePendingTransition(R.anim.enter, R.anim.exit);

                                                                                                }
         /*
    private void forgetPasswordApi() {

        if(edEmailID.getText().toString()==null){

            edEmailID.setError("Enter Email Id");
        }


        final String emailId = edEmailID.getText().toString();

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("email",emailId);


        compositeDisposable.add(apiService.forgetPassword(stringStringHashMap)
                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ForgetPasswordSuccess>() {
                    @Override
                    public void accept(ForgetPasswordSuccess forgetPasswordSuccess) throws Exception {


                        if(forgetPasswordSuccess.getIsSuccess()){

                            // get prompts.xml view
                            LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                            View promptsView = li.inflate(R.layout.prompts, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ForgetPasswordActivity.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(promptsView);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text

                                            otp = String.valueOf(userInput.getText());

                                            HashMap<String, String> stringStringHashMap = new HashMap<>();
                                            stringStringHashMap.put("email",emailId);
                                            stringStringHashMap.put("OTP",otp);


                                            compositeDisposable.add(apiService.validateOTP(stringStringHashMap)

                                                    .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Consumer<ValidateOTPSuccess>() {
                                                        @Override
                                                        public void accept(ValidateOTPSuccess validateOTPSuccess) throws Exception {

                                                            if(validateOTPSuccess.getIsSuccess()){

                                                                // get prompts.xml view
                                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                        ForgetPasswordActivity.this);

                                                                // set prompts.xml to alertdialog builder
                                                                alertDialogBuilder.setView(promptsView);

                                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                                alertDialogBuilder.setCancelable(false)
                                                                        .setPositiveButton("OK",
                                                                                new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                                        stringStringHashMap.put("password",newpsw.getText().toString());



                                                                                        compositeDisposable.add(apiService.updatePassword(stringStringHashMap)
                                                                                                .subscribeOn(io.reactivex.schedulers.Schedulers.computation())
                                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                                .subscribe(new Consumer<UpdatePassSuccess>() {
                                                                                                    @Override
                                                                                                    public void accept(UpdatePassSuccess updatePassSuccess) throws Exception {

                                                                                                        if(updatePassSuccess.getSuccess()){


                                                                                                            showSuccessDialog("Password Updated");
                                                                                                            Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                            startActivity(intent);
                                                                                                            overridePendingTransition(R.anim.enter, R.anim.exit);



                                                                                                        }else {

                                                                                                        }

                                                                                                    }
                                                                                                }, new Consumer<Throwable>() {
                                                                                                    @Override
                                                                                                    public void accept(Throwable throwable) throws Exception {

                                                                                                        showAlertDialog("Retry",throwable.getMessage());

                                                                                                    }
                                                                                                }));

                                                                                    }
                                                            else {

                                                                                    }

                                                                                }
                                                            }, new Consumer<Throwable>() {
                                                                @Override
                                                                public void accept(Throwable throwable) throws Exception {

                                                                    showAlertDialog("Retry",throwable.getMessage());


                                                                }
                                                            }));





                        else {


                                                            }
                                                        }
                                                    }, new Consumer<Throwable>() {
                                                        @Override
                                                        public void accept(Throwable throwable) throws Exception {


                                                            showAlertDialog("Retry",throwable.getMessage());
                                                        }
                                                    }));



*/
/*
        restClient.forgetPassword(stringStringHashMap).enqueue(new Callback<ForgetPasswordSuccess>() {
            @Override
            public void onResponse(Call<ForgetPasswordSuccess> call, Response<ForgetPasswordSuccess> response) {

                if(response.body().getIsSuccess()){


                    // get prompts.xml view
                    LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ForgetPasswordActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text

                                    otp = String.valueOf(userInput.getText());

                                    HashMap<String, String> stringStringHashMap = new HashMap<>();
                                    stringStringHashMap.put("email",emailId);
                                    stringStringHashMap.put("OTP",otp);


                                    restClient.validateOTP(stringStringHashMap).enqueue(new Callback<ValidateOTPSuccess>() {
                                        @Override
                                        public void onResponse(Call<ValidateOTPSuccess> call, Response<ValidateOTPSuccess> response) {

                                            if(response.body().getIsSuccess()){


                                                // get prompts.xml view
                                                LayoutInflater li = LayoutInflater.from(ForgetPasswordActivity.this);
                                                View promptsView = li.inflate(R.layout.alert_dialog_updatepsw, null);

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        ForgetPasswordActivity.this);

                                                // set prompts.xml to alertdialog builder
                                                alertDialogBuilder.setView(promptsView);

                                                final EditText userEmail =  promptsView.findViewById(R.id.edEmailId);
                                                final EditText newpsw =  promptsView.findViewById(R.id.edNewPassword);

                                                alertDialogBuilder.setCancelable(false)
                                                        .setPositiveButton("OK",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {



                                                                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                                                                        stringStringHashMap.put("email",userEmail.getText().toString());
                                                                        stringStringHashMap.put("password",newpsw.getText().toString());


                                                                        restClient.updatePassword(stringStringHashMap).enqueue(new Callback<UpdatePassSuccess>() {
                                                                            @Override
                                                                            public void onResponse(Call<UpdatePassSuccess> call, Response<UpdatePassSuccess> response) {


                                                                                if(response.body().getSuccess()){

                                                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                    builder1.setMessage(response.body().getMessage());
                                                                                    builder1.setCancelable(true);

                                                                                    builder1.setPositiveButton(
                                                                                            "Yes",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {

                                                                                                    Intent intent = new Intent(ForgetPasswordActivity.this, SignInActivity.class);
                                                                                                    startActivity(intent);
                                                                                                    overridePendingTransition(R.anim.enter, R.anim.exit);

                                                                                                }
                                                                                            });

                                                                                    builder1.setNegativeButton(
                                                                                            "No",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                                    dialog.cancel();
                                                                                                }
                                                                                            });

                                                                                    AlertDialog alert11 = builder1.create();
                                                                                    alert11.show();


                                                                                }

                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<UpdatePassSuccess> call, Throwable t) {
                                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                builder1.setMessage(t.getMessage());
                                                                                builder1.setCancelable(true);

                                                                                builder1.setPositiveButton(
                                                                                        "Cancel",
                                                                                        new DialogInterface.OnClickListener() {
                                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                                dialog.cancel();
                                                                                            }
                                                                                        });



                                                                                AlertDialog alert11 = builder1.create();
                                                                                alert11.show();

                                                                            }
                                                                        });



                                                                    }
                                                                })
                                                        .setNegativeButton("Cancel",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog,int id) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();

                                                // show it
                                                alertDialog.show();




                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ValidateOTPSuccess> call, Throwable t) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                            builder1.setMessage(t.getMessage());
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });



                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();


                                        }
                                    });


                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();







                }

            }

            @Override
            public void onFailure(Call<ForgetPasswordSuccess> call, Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                builder1.setMessage(t.getMessage());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });



                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
*//*
*/
/*



                                        }


                                        @Override
                                        protected void onDestroy() {
                                            super.onDestroy();
                                            noInternetDialog.onDestroy();
                                            compositeDisposable.dispose();
                                        }





                                        @Override
                                        public void onBackPressed() {
                                            super.onBackPressed();

                                            finish();
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                                        }

                                    }
*//*

                                                                                   });

                                                                                    builder1.setNegativeButton(
                                                                                            "No",
                                                                                            new DialogInterface.OnClickListener() {
                                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                                    dialog.cancel();
                                                                                                }
                                                                                            });

                                                                                    AlertDialog alert11 = builder1.create();
                                                                                    alert11.show();


                                                                                }

                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<UpdatePassSuccess> call, Throwable t) {
                                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                                                                builder1.setMessage(t.getMessage());
                                                                                builder1.setCancelable(true);

                                                                                builder1.setPositiveButton(
                                                                                        "Cancel",
                                                                                        new DialogInterface.OnClickListener() {
                                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                                dialog.cancel();
                                                                                            }
                                                                                        });



                                                                                AlertDialog alert11 = builder1.create();
                                                                                alert11.show();

                                                                            }
                                                                        });



                                                                    }
                                                                })
                                                        .setNegativeButton("Cancel",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog,int id) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();

                                                // show it
                                                alertDialog.show();




                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ValidateOTPSuccess> call, Throwable t) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                            builder1.setMessage(t.getMessage());
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });



                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();


                                        }
                                    });


                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();







                }

            }

            @Override
            public void onFailure(Call<ForgetPasswordSuccess> call, Throwable t) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ForgetPasswordActivity.this);
                builder1.setMessage(t.getMessage());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });



                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
*//*



                                        }


                                        @Override
                                        protected void onDestroy() {
                                            super.onDestroy();
                                            noInternetDialog.onDestroy();
                                            compositeDisposable.dispose();
                                        }





                                        @Override
                                        public void onBackPressed() {
                                            super.onBackPressed();

                                            finish();
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                                        }

                                    }
*/
