package gleroy.com.mybaseapplication.di.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class NetModule {

    public NetModule() {
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        //configure log level for all our requests
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);

        return clientBuilder.build();
    }

    @Provides
    @Named("base_url")
    String provideBaseUrl() {
        return "https://www.google.com";
    }
}