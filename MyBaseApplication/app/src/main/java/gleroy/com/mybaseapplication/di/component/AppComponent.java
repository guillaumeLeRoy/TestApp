package gleroy.com.mybaseapplication.di.component;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import gleroy.com.mybaseapplication.MyApplication;
import gleroy.com.mybaseapplication.di.module.AndroidBindingModule;
import gleroy.com.mybaseapplication.di.module.AppModule;
import gleroy.com.mybaseapplication.di.module.NetModule;
import gleroy.com.mybaseapplication.di.module.ViewModelModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        NetModule.class,
        ViewModelModule.class,
        AndroidBindingModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        void appModule(AppModule module);

        void netModule(NetModule module);

        AppComponent build();
    }

    void inject(MyApplication app);

}
