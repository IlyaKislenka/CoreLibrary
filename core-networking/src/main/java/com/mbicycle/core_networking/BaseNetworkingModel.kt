package com.mbicycle.core_networking

import android.content.Context
import com.google.gson.Gson
import com.mbicycle.core_networking.dto.ErrorBodyMessage
import com.mbicycle.core_utils.Empty
import com.mbicycle.core_utils.static_utils.Logger
import retrofit2.Response
import java.io.Reader
import java.lang.Exception

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */
/**
 * Class that could check/parse/mapping the responses
 * from [Response] and [okhttp3.Response]
 *
 * @see [ModelImpl]
 *
 * @author Ilya Kislenka
 */
abstract class BaseNetworkingModel(protected val appContext: Context) {

    /**
     * Should be a string resources specific to your project
     */
    abstract fun emptyResponseBodyStringReference(): Int

    abstract fun serverIsUnavailableStringReference(): Int

    private fun getEmptyBodyMessage() = appContext.getString(emptyResponseBodyStringReference())
    private fun getServerUnavailableMessage() = appContext.getString(serverIsUnavailableStringReference())

    /**
     * Function that simplifies response parsing
     * process and transform Response<T> to just T
     * and will handle all the errors
     *
     * @param response it's a general Retrofit response
     * @param clazz pass an [Empty] as a class in here in the
     * cases when the response supposed be empty
     *
     * @author Ilya Kislenka
     */
    protected fun <T> checkRetrofitResponseBodyToError(
        response: Response<T>,
        clazz: Class<T>? = null
    ): T {

        val responseCode = response.code()

        return if (responseCode == 200) {

            val responseBody = response.body()

            if (null == responseBody && null != clazz && Empty::class.java.isAssignableFrom(clazz))
                return Empty.instance as T
            else
                responseBody ?: throw EmptyResponseBodyException(getEmptyBodyMessage())
        } else if (responseCode in 201 until 400) {
            Gson().fromJson(response.errorBody()?.charStream(), clazz) ?: throw EmptyResponseBodyException(
                getEmptyBodyMessage())
        } else
            throw getMessageForResponseCode(responseCode, getErrorBody(response.errorBody()?.charStream()))
    }

    /**
     * Function that simplifies response parsing
     * process and transform Response to just T
     * and will handle all the errors
     *
     * @param response it's a general Retrofit response
     * @param clazz pass an [Empty] as a class in here in the
     * cases when the response supposed be empty
     *
     * @author Ilya Kislenka
     */
    protected fun <T> checkOkHttpResponseBodyToError(response: okhttp3.Response,
                                                     clazz: Class<T>): T {

        val responseCode = response.code()
        val responseBody = response.body()

        return if (responseCode == 200) {

            val realBody = responseBody?.string()

            if (null == realBody && clazz.isAssignableFrom(Empty::class.java))
                return Empty.instance as T
            else
                Gson().fromJson(realBody, clazz) ?: throw EmptyResponseBodyException(getServerUnavailableMessage())

        } else
            throw getMessageForResponseCode(responseCode, getErrorBody(responseBody?.charStream()))
    }

    private fun getErrorBody(errorBody: Reader?): ErrorBodyMessage {

        var errorBodyMessage = ErrorBodyMessage()

        if (null != errorBody) {

            try {
                errorBodyMessage = Gson().fromJson(errorBody, ErrorBodyMessage::class.java)
            } catch (jsonException: Exception) {
                Logger.logError(BaseNetworkingModel::class.java.name, "Can't read error response body")
            }
        }

        return errorBodyMessage
    }

    /**
     * You could override this function to apply functionality
     * described in [getMessageForErrorBody]
     */
    open fun getMessageForResponseCode(
        httpCode: Int,
        errorBodyMessage: ErrorBodyMessage
    ): Exception = when (httpCode) {
        in 500..505 -> ServerException(getServerUnavailableMessage())
        else -> ServerException(errorBodyMessage.message)
    }

    /**
     * Function that could be used from [getMessageForResponseCode]
     * from a constructor of [ServerException], to proceed a mapping
     * between server error code and your string resources.
     *
     * @Note It's a sample function not for use. If you want to achieve the
     * similar functionality, you need to inherit this class and override
     * [getMessageForResponseCode] and create there your own function
     * similar to this one
     *
     * @Note R.string.app_name is used inside the method only to mock the
     * functionality
     */
    private fun getMessageForErrorBody(errorBody: ErrorBodyMessage): String {

        val exceptionCode = errorBody.exceptionCode
        if (1300 == exceptionCode) {
            val message = errorBody.message
            return if (message.isNotEmpty()) message else appContext.getString(R.string.app_name)
        }

        val messageResID = when (exceptionCode) {
            /* 1000 -> R.string.code_1000
             1001 -> R.string.code_1001
             1002 -> R.string.code_1002
             1003 -> R.string.code_1003
             1004 -> R.string.code_1004
             1005 -> R.string.code_1005
             1006 -> R.string.code_1006
             1007 -> R.string.code_1007
             1008 -> R.string.code_1008
             1009 -> R.string.code_1009
             1010 -> R.string.code_1010
             1011 -> R.string.code_1011
             1012 -> R.string.code_1012
             1013 -> R.string.code_1013
             1014 -> R.string.code_1014
             1015 -> R.string.code_1015
             1016 -> R.string.code_1016
             1017 -> R.string.code_1017
             1018 -> R.string.code_1018
             1019 -> R.string.code_1019
             1020 -> R.string.code_1020
             1021 -> R.string.code_1021
             1022 -> R.string.code_1022
             1023 -> R.string.code_1023
             1024 -> R.string.code_1024
             1025 -> R.string.code_1025
             1026 -> R.string.code_1026
             1027 -> R.string.code_1027
             1028 -> R.string.code_1028
             1029 -> R.string.code_1029
             1030 -> R.string.code_1030
             1031 -> R.string.code_1031
             1032 -> R.string.code_1032
             1033 -> R.string.code_1033
             1034 -> R.string.code_1034
             1035 -> R.string.code_1035
             1036 -> R.string.code_1036
             1037 -> R.string.code_1037
             1038 -> R.string.code_1038
             1039 -> R.string.code_1039
             1040 -> R.string.code_1040
             1041 -> R.string.code_1041
             1042 -> R.string.code_1042
             1043 -> R.string.code_1043
             1044 -> R.string.code_1044
             1045 -> R.string.code_1045
             1046 -> R.string.code_1046
             in 1200..1249 -> R.string.code_1200_1249
             1250 -> R.string.code_1250
             1251 -> R.string.code_1251
             1252 -> R.string.code_1252
             1253 -> R.string.code_1253
             1254 -> R.string.code_1254
             1255 -> R.string.code_1255
             in 1256..1299 -> R.string.code_1256_1299
             else -> R.string.code_1012*/
            else -> R.string.app_name
        }
        return appContext.getString(messageResID)
    }

    class ServerException(message: String) : Exception(message)
    class EmptyResponseBodyException(message: String) : Exception(message)
}
