package mall.kwik.kwikmall.paypal;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

/**
 * Created by dharamveer on 28/12/17.
 */

public class PayPalConfig {

    public static final String PAYPAL_CLIENT_ID = "AcBh7HUjY9ZrThyyDX5yc8MYwO30cRnyk8U4e66jlqDAbHDRPR-pTDwswe-QK5dZihe4hWdaAgII7qJA";

    public static final String PAYPAL_CLIENT_SECRET = "";

    public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public static final String PAYMENT_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
    public static final String DEFAULT_CURRENCY = "GHS";

    // PayPal server urls
    public static final String URL_PRODUCTS = "http://192.168.0.103/PayPalServer/v1/products";
    public static final String URL_VERIFY_PAYMENT = "http://192.168.0.103/PayPalServer/v1/verifyPayment";

}
