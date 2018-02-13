package mall.kwik.kwikmall.activities.creditcard;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static mall.kwik.kwikmall.activities.PaymentMethodActivity.edExpiryDate;

/**
 * Created by dharamveer on 28/12/17.
 */

public class CreditCardExpiryTextWatcher implements TextWatcher {

    private EditText etCard;
    private TextView tvCard;
    private boolean isDelete;

    public CreditCardExpiryTextWatcher(EditText etcard, TextView tvcard) {
        this.etCard = etcard;
        this.tvCard = tvcard;
    }

    public CreditCardExpiryTextWatcher(EditText etcard) {
        this.etCard = etcard;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (before == 0)
            isDelete = false;
        else
            isDelete = true;
    }

    boolean isSlash = false; //class level initialization

    private void formatCardExpiringDate(Editable s) {

        String input = s.toString();
        String mLastInput = "";

        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy",     Locale.ENGLISH);
        Calendar expiryDateDate = Calendar.getInstance();

        try {
            expiryDateDate.setTime(formatter.parse(input));
        } catch (java.text.ParseException e) {
            if (s.length() == 2 && !mLastInput.endsWith("/") && isSlash) {
                isSlash = false;
                int month = Integer.parseInt(input);
                if (month <= 12) {
                    edExpiryDate.setText(edExpiryDate.getText().toString().substring(0, 1));
                    edExpiryDate.setSelection(edExpiryDate.getText().toString().length());
                } else {
                    s.clear();
                    edExpiryDate.setText("");
                    edExpiryDate.setSelection(edExpiryDate.getText().toString().length());
                }
            }else if (s.length() == 2 && !mLastInput.endsWith("/") && !isSlash) {
                isSlash = true;
                int month = Integer.parseInt(input);
                if (month <= 12) {
                    edExpiryDate.setText(edExpiryDate.getText().toString() + "/");
                    edExpiryDate.setSelection(edExpiryDate.getText().toString().length());
                }else if(month > 12){
                    edExpiryDate.setText("");
                    edExpiryDate.setSelection(edExpiryDate.getText().toString().length());
                    s.clear();
                }


            } else if (s.length() == 1) {

                int month = Integer.parseInt(input);
                if (month > 1 && month < 12) {
                    isSlash = true;
                    edExpiryDate.setText("0" + edExpiryDate.getText().toString() + "/");
                    edExpiryDate.setSelection(edExpiryDate.getText().toString().length());
                }
            }

            mLastInput = edExpiryDate.getText().toString();
            return;
        }

    }



        @Override
    public void afterTextChanged(Editable s) {

            try{
                formatCardExpiringDate(s);
            }catch(NumberFormatException e){
                String source = s.toString();
                int length = source.length();

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(source);

                if (length > 0 && length == 3) {
                    if (isDelete)
                        stringBuilder.deleteCharAt(length - 1);
                    else
                        stringBuilder.insert(length - 1, "/");

                    etCard.setText(stringBuilder);
                    etCard.setSelection(etCard.getText().length());

                    // Log.d("test"+s.toString(), "afterTextChanged: append "+length);
                }

                if (tvCard != null) {
                    if (length == 0)
                        tvCard.setText("MM/YY");
                    else
                        tvCard.setText(stringBuilder);
                }


            }



    }





}
