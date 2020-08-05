package learn.htm.projectlearn.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import learn.htm.projectlearn.BuildConfig
import learn.htm.projectlearn.data.remote.ParamsInterceptor
import learn.htm.projectlearn.data.remote.api.CONNECTION_API_TIME_OUT
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.data.remote.api.READ_API_TIME_OUT
import learn.htm.projectlearn.data.remote.api.WRITE_API_TIME_OUT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val LOG_INTERCEPTOR = "LOG_INTERCEPTOR"
const val DEFAULT_SERVICE = "DEFAULT_SERVICE"
const val PARAMS_INTERCEPTOR = "PARAMS_INTERCEPTOR"

val netWorkModule = module {

    /*
    * factory — is used to indicate that a new instance must be created each time it is injected.
    * */
    factory(named(LOG_INTERCEPTOR)) { provideLogInterceptor() }
    factory { provideMoshi() }
    factory(named(PARAMS_INTERCEPTOR)) { provideParamsInterceptor() }
    /*
    * single — indicates that a unique instance will be created at the beginning and shared on every future injection.
    * */
    single {
        provideOkHttpClient(
            get(named(LOG_INTERCEPTOR)),
            get(named(PARAMS_INTERCEPTOR))
        )
    }
    single(qualifier = named(DEFAULT_SERVICE)) {
        provideDefaultRetrofit(get(), get())
    }

    single { provideServerAPIService(get(qualifier = named(DEFAULT_SERVICE))) }
}

fun provideLogInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}

fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    paramsInterceptor: Interceptor
): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(paramsInterceptor)
        .readTimeout(READ_API_TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(CONNECTION_API_TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_API_TIME_OUT, TimeUnit.SECONDS).build()

fun provideParamsInterceptor(): Interceptor = ParamsInterceptor()

private fun provideMoshi(): Moshi =
    Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private fun provideDefaultRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

private fun provideServerAPIService(retrofit: Retrofit): MovieAPI =
    retrofit.create(MovieAPI::class.java)

