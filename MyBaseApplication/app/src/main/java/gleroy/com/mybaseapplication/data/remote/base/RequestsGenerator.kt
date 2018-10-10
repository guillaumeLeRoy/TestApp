package gleroy.com.mybaseapplication.data.remote.base

import java.util.concurrent.TimeUnit

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

import gleroy.com.mybaseapplication.data.remote.api.base.MyApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber

@Singleton
class RequestsGenerator @Inject
constructor(@param:Named("base_url") private val baseUrl: String,
            private val okHttpClient: OkHttpClient) {


    val api: MyApi
        get() = buildAPI(ContentType.CONTENT_TYPE_JSON, baseUrl)

    enum class ContentType private constructor(var content: String) {

        CONTENT_TYPE_MULTIPART("multipart/form-data"),
        CONTENT_TYPE_XML("application/xml"),
        CONTENT_TYPE_JSON("application/json")
    }

    /* -------- private methods ---------*/

    private fun buildAPI(contentType: ContentType, baseUrl: String): MyApi {
        //create a new client specific for this request from the original httpClient
        val client = okHttpClient
                .newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addNetworkInterceptor { chain ->
                    val original = chain.request()
                    //add default headers
                    val builder = original.newBuilder()
                    val request = builder.build()
                    chain.proceed(request)
                }.build()
        return buildRetrofit(client, baseUrl, contentType)
                .create(MyApi::class.java)
    }

    private fun buildRetrofit(client: OkHttpClient, baseUrl: String, contentType: RequestsGenerator.ContentType): Retrofit {
        val builder = Retrofit.Builder()

        builder.baseUrl(baseUrl)
                .client(client)

        // todo : to remove
        if (contentType == RequestsGenerator.ContentType.CONTENT_TYPE_MULTIPART || contentType == RequestsGenerator.ContentType.CONTENT_TYPE_XML) {
            Timber.d("createApi, add xml")
            builder.addConverterFactory(SimpleXmlConverterFactory.create())
        } else {
            Timber.d("createApi, add gson")
            builder.addConverterFactory(GsonConverterFactory.create())
        }

        return builder.build()
    }


}
