package com.mbicycle.core_networking.retrofit_sample.refresh

import android.content.Context
import androidx.annotation.WorkerThread
import com.mbicycle.core_networking.BaseNetworkingModel
import com.mbicycle.core_networking.R
import com.mbicycle.core_utils.basic_auth.*
import com.mbicycle.core_utils.static_utils.Logger
import okhttp3.*
import java.lang.Exception
import javax.inject.Inject

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */
/**
 * It's an example implementation of the refresh token process
 *
 * @Note only for demonstration purpose, shouldn't be used directly
 */
internal class RefreshTokenModel @Inject constructor(
    appContext: Context,
    private val preferenceWriter: PreferencesWriter,
    private val basicAuthorization: BasicAuthorizationEncoder
) : BaseNetworkingModel(appContext) {

    /**
     * Should be called inside [Authenticator] that
     * was set to Retrofit
     */
    @WorkerThread
    fun refreshToken(refreshToken: String): LoginResponse {

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("grant_type", "refresh_token")
            .addFormDataPart("refresh_token", refreshToken)
            .addFormDataPart("app_device", "ANDROID")
            .addFormDataPart("app_version", "specific application version")
            .build()

        val request = Request.Builder()
            .url("your http address" + "oauth/token")
            .header(
                basicAuthorization.headerTitle,
                basicAuthorization.encodeToHeader(BasicAuthCredentials("specific user name",
                    "specific password"))
            )
            .post(body)
            .build()

        val response = OkHttpClient().newCall(request).execute()

        val responseCode = response.code()
        if (responseCode in 400 until 500) {
            Logger.logError(RefreshTokenModel::class.java.name,
                "Refresh token failed with: http code = $responseCode, message = ${response.message()}, body = ${response.body()?.string()}")
            preferenceWriter.clearLoginCredentials()
            //R.string.app_name it's only for mock purpose, should be something specific
            throw AuthorizationFailedException(appContext.getString(R.string.app_name))
        }

        return checkOkHttpResponseBodyToError(response, LoginResponse::class.java)
    }

    /**
     * The exception for describing a situation when refresh token
     * process has finished with an error and that error was invoked by backend side,
     * and we need to redirect user to a login page.
     */
    class AuthorizationFailedException(message: String) : Exception(message)

    //region Mocks
    override fun emptyResponseBodyStringReference() = R.string.app_name

    override fun serverIsUnavailableStringReference() = R.string.app_name
    //endregion
}
