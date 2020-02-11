package com.mbicycle.core_multithreading

import android.content.Context
import android.net.ConnectivityManager
import com.mbicycle.core_utils.transmit_state.*
import com.mbicycle.core_utils.static_utils.Logger
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.IllegalStateException

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Class to separate the threads while executing any operation described in lambda
 * @Note This class using kotlin coroutines to separate the context inside intercepted
 * coroutine scope
 *
 * @author Ilya Kislenka
 */
class ThreadsSeparator constructor(private val appContext: Context) {

    private fun hasNetworkConnection(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null
    }

    /**
     * This function which checks an access to the network before executing the @param request,
     * and the will be  using kotlin coroutines to separate the threads, so call it inside coroutine
     * launcher or any suspend function.
     *
     * @param request it's a block of the code that should describe any
     * operation that should be executed by IO threads, represents as a lambda
     *
     * @return An abstract [Result] class, that wraps within the status of operation,
     * result POJO or related cause of error inside the @param request block as an Exception
     *
     * @author Ilya Kislenka
     */
    suspend fun <T> startRequest(
        request: suspend () -> T?
    ): Result<T> {

        return withContext(Dispatchers.IO) {

            try {

                if (hasNetworkConnection(appContext)) {
                    val result = request()
                    Result(
                        ResultStatus.SUCCESS,
                        result
                    )
                } else {
                    Result(
                        ResultStatus.FAILED,
                        exception = AbsentNetworkConnectionException(appContext.getString(R.string.no_internet_connection_message))
                    )
                }

            } catch (exception: Exception) {
                Logger.logError(this::class.java.simpleName,
                    appContext.getString(R.string.request_breaks_down_text),
                    error = exception)
                Result<T>(
                    ResultStatus.FAILED,
                    exception = when (exception) {
                        is IOException -> AbsentNetworkConnectionException(appContext.getString(R.string.no_internet_connection_message))
                        else -> exception
                    }
                )
            }
        }
    }

    /**
     * An abstract exception to wrap NO_NETWORK state
     *
     * @author Ilya Kislenka
     */
    class AbsentNetworkConnectionException(message: String) : Exception(message)

    /**
     * This function is using kotlin coroutines to separate the threads, so call it inside coroutine
     * launcher or any suspend function.
     *
     * @param block it's a block of the code that should describe any
     * operation that should be executed by IO threads, represents as a lambda
     *
     * @author Ilya Kislenka
     */
    internal suspend fun <T> asyncIO(block: suspend () -> T) = coroutineScope {
        withContext(Dispatchers.IO) {
            block()
        }
    }

    /**
     * This function is using kotlin coroutines to separate the threads, so call it inside coroutine
     * launcher or any suspend function.
     *
     * @param block it's a block of the code that should describe any
     * operation that should be executed by IO threads, represents as a lambda
     *
     * @return An abstract [Result] class, that wraps within the status of operation,
     * result POJO or related cause of error inside the @param request block as an Exception
     *
     * @author Ilya Kislenka
     */
    internal suspend fun <T> asyncIOWithResult(block: suspend () -> T): Result<T> = coroutineScope {

        withContext(Dispatchers.IO) {
            try {
                Result(ResultStatus.SUCCESS, block())
            } catch (throwable: Throwable) {
                val message = appContext.getString(R.string.async_operation_finished_with_exception_text)
                Logger.logError(ThreadsSeparator::class.java.simpleName, "$message = $throwable")
                Result<T>(
                    ResultStatus.FAILED,
                    exception = if (throwable is Exception) throwable else IllegalStateException(message)
                )
            }
        }
    }
}
