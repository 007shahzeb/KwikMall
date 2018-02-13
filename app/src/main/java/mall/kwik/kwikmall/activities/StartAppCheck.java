package mall.kwik.kwikmall.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import am.appwise.components.ni.NoInternetDialog;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.sharedpreferences.UserDataUtility;

/**
 * Created by dharamveer on 16/11/17.
 */




public class StartAppCheck extends Activity {

    private Context context;
    private NoInternetDialog noInternetDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();

        checkForFirst();


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }



    private void createShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(), StartAppCheck.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Kwik Mall");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.logo));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
        getApplicationContext().sendBroadcast(addIntent);
    }

    private void checkForFirst() {

        if (UserDataUtility.getLogin(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), FragmentsActivity.class);
            startActivity(intent);

        } else {


            if (!getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).getBoolean("IS_ICON_CREATED", false)) {
                createShortcut();
                getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).edit().putBoolean("IS_ICON_CREATED", true).commit();
            }

            SharedPreferences settings = getSharedPreferences("prefs", 0);
            boolean firstRun = settings.getBoolean("firstRun", false);

            if (firstRun == false)//if running for first time
            //Splash will load for first time
            {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("firstRun", true);
                editor.commit();
                Intent i = new Intent(StartAppCheck.this, SplashActivity.class);
                startActivity(i);
                finish();
            } else {


                Intent a = new Intent(StartAppCheck.this, SignInSignUpButtonsActivity.class);
                startActivity(a);
            }

        }
    }


}