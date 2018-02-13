package mall.kwik.kwikmall.di.modules;

import android.app.Activity;
import android.content.Context;


import dagger.Module;
import dagger.Provides;
import mall.kwik.kwikmall.di.scopes.ActivityContext;
import mall.kwik.kwikmall.di.scopes.PerActivity;

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    @ActivityContext
    public Context provideContext() {
        return activity;
    }
}
