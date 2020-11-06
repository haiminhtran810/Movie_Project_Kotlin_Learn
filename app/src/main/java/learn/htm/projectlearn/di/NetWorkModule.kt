package learn.htm.projectlearn.di

import android.content.ComponentName
import android.content.Context
import com.squareup.moshi.Moshi
import learn.htm.projectlearn.BuildConfig
import learn.htm.projectlearn.base.BaseService
import learn.htm.projectlearn.data.remote.api.CONNECTION_API_TIME_OUT
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.data.remote.api.READ_API_TIME_OUT
import learn.htm.projectlearn.data.remote.api.WRITE_API_TIME_OUT
import learn.htm.projectlearn.data.remote.interceptor.HeaderInterceptor
import learn.htm.projectlearn.data.remote.moshi.MoshiArrayListJsonAdapter
import learn.htm.projectlearn.service.MusicServiceConnection
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
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
    factory(named(PARAMS_INTERCEPTOR)) { provideHeaderInterceptor() }
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

    single { provideMusicServiceConnection(androidApplication()) }
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

fun provideHeaderInterceptor(): Interceptor =
    HeaderInterceptor()

private fun provideMoshi(): Moshi =
    Moshi.Builder()
        .add(MoshiArrayListJsonAdapter.FACTORY)
        .build()

private fun provideDefaultRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

private fun provideServerAPIService(retrofit: Retrofit): MovieAPI =
    retrofit.create(MovieAPI::class.java)

private fun provideMusicServiceConnection(context: Context): MusicServiceConnection {
    return MusicServiceConnection.getInstance(
        context,
        ComponentName(context, BaseService::class.java)
    )
}

