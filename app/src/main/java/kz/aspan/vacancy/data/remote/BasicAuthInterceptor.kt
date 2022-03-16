package kz.aspan.vacancy.data.remote

import kz.aspan.vacancy.common.Constants.IGNORE_AUTH_URLS
import okhttp3.Interceptor
import okhttp3.Response

class
BasicAuthInterceptor : Interceptor {
    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath in IGNORE_AUTH_URLS) {
            return chain.proceed(request)
        }


        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(authenticatedRequest)
    }
}