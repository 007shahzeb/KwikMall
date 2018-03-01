package mall.kwik.kwikmall.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.ButterKnife;
import mall.kwik.kwikmall.R;

/**
 * Created by dharamveer on 28/2/18.
 */

public class SendFeedbackDialog extends Dialog {


    private EditText edFeedback;
    private TextView tvCancel,tvOk;
    private MaterialDialog dialog;


    public SendFeedbackDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_send_feedback);


        edFeedback = findViewById(R.id.edFeedback);
        tvCancel = findViewById(R.id.tvCancel);
        tvOk = findViewById(R.id.tvOk);


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dismiss();
            }
        });


        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoadingDialog();


                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/html");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"kwiqqmall@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                i.putExtra(Intent.EXTRA_TEXT, "Message : "+edFeedback.getText());
                try {
                    getContext().startActivity(Intent.createChooser(i, "Send feedback..."));
                    dialog.dismiss();

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }

                dismiss();

            }
        });

    }


    public void showLoadingDialog() {
        dialog = new MaterialDialog.Builder(getContext())
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();
    }

}
