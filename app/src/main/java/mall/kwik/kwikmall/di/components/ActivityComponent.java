package mall.kwik.kwikmall.di.components;




import dagger.Component;
import mall.kwik.kwikmall.di.modules.ActivityModule;
import mall.kwik.kwikmall.di.scopes.PerActivity;


@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {


}
