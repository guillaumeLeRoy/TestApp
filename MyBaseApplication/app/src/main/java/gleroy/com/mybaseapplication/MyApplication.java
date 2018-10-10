package gleroy.com.mybaseapplication;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import gleroy.com.mybaseapplication.di.component.AppComponent;
import gleroy.com.mybaseapplication.di.component.DaggerAppComponent;
import gleroy.com.mybaseapplication.di.module.AppModule;
import gleroy.com.mybaseapplication.di.module.NetModule;
import timber.log.Timber;

public class MyApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent appComponent = createComponent();
        appComponent.inject(this);

        Timber.plant(new Timber.DebugTree());

    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    /**
     * Override this method to load a different component
     *
     * @return
     */
    protected AppComponent createComponent() {
        AppComponent.Builder builder = DaggerAppComponent.builder();
        builder.netModule(new NetModule(this));
        builder.appModule(new AppModule(this));
        return builder.build();
    }
}
