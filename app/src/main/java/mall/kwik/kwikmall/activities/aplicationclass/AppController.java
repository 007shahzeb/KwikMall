package mall.kwik.kwikmall.activities.aplicationclass;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;

import mall.kwik.kwikmall.R;
import mall.kwik.kwikmall.di.components.AppComponent;
import mall.kwik.kwikmall.di.components.DaggerAppComponent;
import mall.kwik.kwikmall.di.modules.HttpModule;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.utils.RxBus;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by dharamveer on 3/1/18.
 */

public class AppController extends Application {

    private RequestQueue mRequestQueue;
    private static AppController mInstance;

    private RxBus bus;
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


        Stetho.initializeWithDefaults(this);

        bus = new RxBus();

        component = DaggerAppComponent.builder().sharedPrefsHelper(new SharedPrefsHelper(this))
                .httpModule(new HttpModule(this))
                .build();



        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/HindVadodara-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }



    public RxBus bus() {
        return bus;
    }
    public AppComponent getComponent() {
        return component;
    }




    public RequestQueue getReqQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToReqQueue(Request<T> req, String tag) {

        getReqQueue().add(req);
    }

    public <T> void addToReqQueue(Request<T> req) {

        getReqQueue().add(req);
    }

    public void cancelPendingReq(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
