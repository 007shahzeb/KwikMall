package mall.kwik.kwikmall.di.components;




import javax.inject.Singleton;

import dagger.Component;
import mall.kwik.kwikmall.BaseFragActivity.BaseActivity;
import mall.kwik.kwikmall.BaseFragActivity.BaseFragment;
import mall.kwik.kwikmall.adapters.FavouritesAdapter;
import mall.kwik.kwikmall.di.modules.HttpModule;
import mall.kwik.kwikmall.di.modules.SharedPrefsHelper;
import mall.kwik.kwikmall.dialogs.DialogMenu;
import mall.kwik.kwikmall.manager.GitApiInterface;

@Singleton
@Component(modules = {HttpModule.class, SharedPrefsHelper.class})
public interface AppComponent {

    void inject(BaseActivity activity);
    GitApiInterface api();


    void inject(FavouritesAdapter favouritesAdapter);


    void inject(BaseFragment baseFragment);
    void inject(DialogMenu dialogMenu);
}
