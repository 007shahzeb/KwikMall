package mall.kwik.kwikmall.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import am.appwise.components.ni.NoInternetDialog;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.baseFragActivity.BaseActivity;

public class NewPassActivity extends BaseActivity {


    private NoInternetDialog noInternetDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);

        context = this;
        noInternetDialog = new NoInternetDialog.Builder(context).build();



    }
}
