package mall.kwik.kwikmall.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import mall.kwik.kwikmall.R;

/**
 * Created by dharamveer on 2/1/18.
 */

public class LogoutConfirmDialog extends Dialog {


    public LogoutConfirmDialog.myOnClickListener myListener;
    Context context;
    private TextView tvCancel,tvLogout;


    public LogoutConfirmDialog(@NonNull Context context,myOnClickListener myOnClickListener) {
        super(context);
        this.context = context;
        this.myListener = myOnClickListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_logout_confirm);

        tvCancel = findViewById(R.id.tvCancel);
        tvLogout = findViewById(R.id.tvLogout);


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myListener.onButtonClick();

            }
        });

    }

    public interface myOnClickListener {
        void onButtonClick();
    }


}

