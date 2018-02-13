package mall.kwik.kwikmall.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import mall.kwik.kwikmall.R;

/**
 * Created by dharamveer on 20/12/17.
 */

public class DialogInternet extends Dialog {


    public myOnClickListener myListener;
    Context context;
    private TextView tvOpenSettings;

  /*  public DialogInternet(@NonNull Context context,myOnClickListener myOnClickListener) {
        super(context);
        this.context = context;
        this.myListener = myOnClickListener;
    }*/

    public DialogInternet(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.alert_dialog_internet);

        tvOpenSettings = findViewById(R.id.tvOpenSettings);



        tvOpenSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //myListener.onButtonClick();

                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dialogIntent);
            }
        });

    }

    public interface myOnClickListener {
        void onButtonClick();
    }


}
