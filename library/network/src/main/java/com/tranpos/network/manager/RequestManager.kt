package com.tranpos.network.manager
import android.content.Context
import com.tranpos.network.cookie.CookieJar
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext

object RequestManager {

    internal lateinit var appContext : Context

    private lateinit var retrofit : Retrofit

    private lateinit var baseUrl : String
    private lateinit var headers : HeaderInterceptor
    private var requestDsl: (RequestDsl.() ->Unit)? = null

    fun init(context: Context,baseUrl : String,requestDsl: (RequestDsl.() ->Unit)?){
        this.appContext = context.applicationContext
        this.baseUrl = baseUrl
        this.headers = HeaderInterceptor()
        this.requestDsl = requestDsl
    }

    private fun init(requestDSL: (RequestDsl.() -> Unit)? = null) {
        initRetrofit(getOkHttp(), requestDSL)
    }

    private fun initRetrofit(builder: OkHttpClient.Builder,requestDSL: (RequestDsl.() -> Unit)? = null){
        val dsl = if(requestDSL != null) RequestDsl().apply(requestDSL) else null
        val okhttpBuilder = dsl?.okhttpBuilder?.invoke(builder) ?: builder
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpBuilder.build())
        val finalRetrofitBuilder = dsl?.retrofitBuilder?.invoke(retrofitBuilder) ?: retrofitBuilder
        this.retrofit = finalRetrofitBuilder.build()
    }

    private fun getOkHttp(): OkHttpClient.Builder{
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(
                null,
                arrayOf(XTrustManager()),
                SecureRandom()
            )
            val sslSocketFactory = sslContext.socketFactory
            builder.sslSocketFactory(sslSocketFactory, XTrustManager())
            builder.hostnameVerifier(HostnameVerifier { hostname, session ->
                true
            })
            builder.addNetworkInterceptor(LoggingInterceptor())
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        return builder
            .cache(Cache(appContext.cacheDir, 10 * 1024 * 1024L))
            .addInterceptor(headers)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .cookieJar(CookieJar.getInstance())
    }

    fun <Service> apiService(service: Class<Service>): Service {
        return retrofit.create(service)
    }

    fun resetBaseUrl(newValue: String): Boolean {
        val isOK = !newValue.isBlank() && (baseUrl.startsWith("http://") || baseUrl.startsWith("https://"))
        check(isOK) { "baseUrl is illegal: $baseUrl" }
        val isChanged = isOK && baseUrl != newValue
        if (isChanged) {
            init(appContext, newValue, requestDsl)
        }
        return isChanged
    }

    fun putHead(key: String, value: String): HeaderInterceptor {
        headers.put(key, value)
        return headers
    }

    fun removeHead(key: String):HeaderInterceptor{
        headers.remove(key)
        return headers
    }

    class RequestDsl{
        internal var okhttpBuilder : ((OkHttpClient.Builder) ->OkHttpClient.Builder)? = null
        internal var retrofitBuilder : ((Retrofit.Builder)->Retrofit.Builder)? = null

        infix fun okHttp(okhttpBuilder : ((OkHttpClient.Builder) ->OkHttpClient.Builder)){
            this.okhttpBuilder = okhttpBuilder
        }

        infix fun retrofit(retrofitBuilder : ((Retrofit.Builder)->Retrofit.Builder)){
            this.retrofitBuilder = retrofitBuilder
        }
    }
}