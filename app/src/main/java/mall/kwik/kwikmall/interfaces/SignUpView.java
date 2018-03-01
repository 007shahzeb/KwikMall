package mall.kwik.kwikmall.interfaces;

/**
 * Created by dharamveer on 28/2/18.
 */

public interface SignUpView  {


    void showLoadingDialog();


    void dismissLoadingDialog();



    void showError(String message);

    void startLoginActivity();


}
