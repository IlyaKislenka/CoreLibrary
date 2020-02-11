package com.mbicycle.core_networking.retrofit_sample

import com.mbicycle.core_networking.retrofit_sample.refresh.*
import com.mbicycle.core_utils.basic_auth.*
import com.mbicycle.core_utils.extensions.*
import dagger.*
import okhttp3.*
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Scope

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */
/**
 * It's a sample of initialization of the Retrofit library
 * in your own project
 */
@Module
class RetrofitModule(private val endPoint: String) {

    @YourScope
    @Provides
    fun provideRetrofit(
        httpClient: OkHttpClient,
        responseBodyConverterFactory: Converter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(responseBodyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(endPoint)
            .build()

    @YourScope
    @Provides
    fun provideHttpClient(
        authenticator: Authenticator,
        interceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .authenticator(authenticator)
            .addNetworkInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

    /**
     * It's an requests interceptor that should be used
     * to append an HTTP headers to the requests in a runtime
     */
    @YourScope
    @Provides
    internal fun provideInterceptor(reader: PreferencesReader,
                                    basicAuthorization: BasicAuthorizationEncoder): Interceptor {

        return Interceptor { chain ->

            val token = reader.getCachedAccessToken()

            var request = chain.request()

            if (HeaderlessRequestsContainer.headerlessRequests.matchPartially(request.url().path()))
            else if (token.isEmpty()) {
                request = request
                    .newBuilder()
                    .header(
                        basicAuthorization.headerTitle,
                        basicAuthorization.encodeToHeader(BasicAuthCredentials("specific user name",
                            "specific password"))
                    )
                    .build()
            } else
                request = request.buildWithAnAuthorizationHeader(token)

            chain.proceed(request)
        }
    }

    private fun HttpUrl.path(): String {

        val cuttingPath = StringBuilder().builder {
            append(scheme())
            append("://")
            append(host())
            append("/api/")
        }

        return toString().replace(cuttingPath, "")
    }

    /**
     * It's a sample of authenticator, that will be
     * called as soon as any requests receives a 401 HTTP status code
     * That will mean that we should refresh the access token
     */
    @YourScope
    @Provides
    internal fun provideAuthenticator(
        preferencesWriter: PreferencesWriter,
        preferencesReader: PreferencesReader,
        refreshTokenModel: RefreshTokenModel
    ): Authenticator {

        return Authenticator { _, response ->

            val refreshTokenResult = refreshTokenModel.refreshToken(preferencesReader.getCachedRefreshToken())

            preferencesWriter.apply {
                cacheAccessToken(refreshTokenResult.accessToken)
                cacheRefreshToken(refreshTokenResult.refreshToken)
                cacheTokenExpirationTime(refreshTokenResult.expiresIn.toLong())
            }

            response.request().buildWithAnAuthorizationHeader(preferencesReader.getCachedAccessToken())
        }
    }

    private fun Request.buildWithAnAuthorizationHeader(accessToken: String): Request {
        return this.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }

    /**
     * Converter factory which allows us to declare Call<OurType>, in case of
     * an empty response body it's not more necessary to declare request like Call<Void>,
     * factory will return a null instead of exception, and it helps us just to check
     * that if request was successful an body is empty and we expected OurType and just
     * return an OurType instead of null into system. Without this factory retrofit
     * handles empty body successfully only if expected type was Void and returning a null,
     * but the problem is that we can't create an instance of Void type to return it into the system as
     * expected result.
     */
    @YourScope
    @Provides
    fun provideANullOrEmptyFactory(): Converter.Factory {

        return object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) =
                object : Converter<ResponseBody, Any?> {
                    val nextResponseBodyConverter =
                        retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)

                    override fun convert(value: ResponseBody) =
                        if (value.contentLength() != 0L) nextResponseBodyConverter.convert(value) else null
                }
        }
    }

    /**
     * This should be your specific APIService providing
     */
    @YourScope
    @Provides
    fun provideAnApiService(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }

    /**
     * Just a mocked Dagger's scope
     */
    @Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class YourScope
}
