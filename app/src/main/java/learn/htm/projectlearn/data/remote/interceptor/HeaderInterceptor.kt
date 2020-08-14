package learn.htm.projectlearn.data.remote.interceptor

import learn.htm.projectlearn.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
//        val token: Token? = AppPrefs(context, Gson()).getToken()

        var request = chain.request()
        val newUrl = request.url.newBuilder().addQueryParameter(
            "api_key",
            BuildConfig.API_KEY
        ).build()
        request = request?.newBuilder()
            ?.url(newUrl)
            ?.addHeader("Content-Type", "application/json")
            //?.apply { token?.token?.let { addHeader("Authorization", "Bearer $it") } }
            ?.method(request.method, request.body)?.build()
        return chain.proceed(request)
    }

    companion object {
        const val API_KEY = "api_key"
    }
}