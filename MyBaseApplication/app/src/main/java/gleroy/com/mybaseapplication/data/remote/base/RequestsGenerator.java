package gleroy.com.mybaseapplication.data.remote.base;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import gleroy.com.mybaseapplication.data.remote.api.base.MyApi;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import timber.log.Timber;

@Singleton
public class RequestsGenerator {

    @NonNull
    private String mBaseUrl;
    @NonNull
    private OkHttpClient mOkHttpClient;

    @Inject
    public RequestsGenerator(@NonNull String baseUrl,
                             @NonNull OkHttpClient okHttpClient) {
        mBaseUrl = baseUrl;
        mOkHttpClient = okHttpClient;
    }

    public enum ContentType {

        CONTENT_TYPE_MULTIPART("multipart/form-data"),
        CONTENT_TYPE_XML("application/xml"),
        CONTENT_TYPE_JSON("application/json");

        public String content;

        ContentType(String content) {
            this.content = content;
        }
    }


    @NonNull
    public MyApi getApi() {
        return getRetrofitServicePasswordRequested(RequestsGenerator.ContentType.CONTENT_TYPE_XML, mBaseUrl);//todo : XML ??
    }

    /* -------- private methods ---------*/

    @NonNull
    private MyApi getRetrofitServicePasswordRequested(@NonNull ContentType contentType, @NonNull String baseUrl) {
        //create a new client specific for this request from the original httpClient
        OkHttpClient client = mOkHttpClient
                .newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        //add default headers
                        Request.Builder builder = original.newBuilder();
                        Request request = builder.build();
                        return chain.proceed(request);
                    }
                }).build();
        return buildRetrofit(client, baseUrl, contentType).create(MyApi.class);
    }

    @NonNull
    private Retrofit buildRetrofit(@NonNull OkHttpClient client, @NonNull String baseUrl, @NonNull final RequestsGenerator.ContentType contentType) {
        Retrofit.Builder builder = new Retrofit.Builder();

        builder.baseUrl(baseUrl)
                .client(client);

        // todo : to remove
        if (contentType == RequestsGenerator.ContentType.CONTENT_TYPE_MULTIPART || contentType == RequestsGenerator.ContentType.CONTENT_TYPE_XML) {
            Timber.d("createApi, add xml");
            builder.addConverterFactory(SimpleXmlConverterFactory.create());
        } else {
            Timber.d("createApi, add gson");
            builder.addConverterFactory(GsonConverterFactory.create());
        }

        return builder.build();
    }


}
