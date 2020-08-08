package learn.htm.projectlearn.data.remote.interceptor

import learn.htm.projectlearn.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newUrl = original.url.newBuilder()
            .addQueryParameter(API_KEY, BuildConfig.API_KEY)
            .build()

        val newRequest = original.newBuilder()
            .url(newUrl)
            .method(original.method, original.body)
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        const val API_KEY = "api_key"
    }
}