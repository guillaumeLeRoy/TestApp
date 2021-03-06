package gleroy.com.mybaseapplication.di.module;

import android.app.Application;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import gleroy.com.mybaseapplication.utils.react.BaseSchedulerProvider;
import gleroy.com.mybaseapplication.utils.react.SchedulerProvider;

@Module
public class AppModule {

    @NonNull
    private Application mApplication;

    public AppModule(@NonNull Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    BaseSchedulerProvider provideSchedulerProvider(SchedulerProvider schedulerProvider) {
        return schedulerProvider;
    }

}


