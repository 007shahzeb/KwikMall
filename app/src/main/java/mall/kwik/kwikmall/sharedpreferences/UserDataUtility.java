package mall.kwik.kwikmall.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dharamveer on 19/9/17.
 */

public class UserDataUtility {

    Context context;


    private int userId;
    private String userName;
    private String userEmail;
    private String userPhoneno;

    static SharedPreferences sharedPreferences;

    public UserDataUtility(Context context) {
        this.context = context;
        this.userEmail = userEmail;
        this.sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);;
    }

    public static boolean getLogin(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences("lll",Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean("first",false);
    }

    public static void setLogin(boolean login, Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences("lll",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first",login);
        editor.clear();
        editor.apply();
        editor.commit();

    }




    public int getUserId() {
        userId = sharedPreferences.getInt("userId",userId);

        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
        sharedPreferences.edit().putInt("userId",userId).commit();

    }

    public String getUserName() {
        userName = sharedPreferences.getString("userName",userName);

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        sharedPreferences.edit().putString("userName",userName).commit();

    }

    public String getUserEmail() {
        userEmail = sharedPreferences.getString("userEmail",userEmail);

        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        sharedPreferences.edit().putString("userEmail",userEmail).commit();

    }

    public String getUserPhoneno() {
        userPhoneno = sharedPreferences.getString("userPhoneno",userPhoneno);

        return userPhoneno;
    }

    public void setUserPhoneno(String userPhoneno) {
        this.userPhoneno = userPhoneno;
        sharedPreferences.edit().putString("userPhoneno",userPhoneno).commit();

    }






}
