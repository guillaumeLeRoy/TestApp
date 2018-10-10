package gleroy.com.mybaseapplication.di.module;

import android.support.annotation.NonNull;

import dagger.Module;
import gleroy.com.mybaseapplication.MyApplication;

@Module
public class NetModule {

    @NonNull
    private MyApplication mApplication;

    public NetModule(@NonNull MyApplication application) {
        mApplication = application;
    }

}