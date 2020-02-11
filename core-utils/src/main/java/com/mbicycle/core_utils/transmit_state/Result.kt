package com.mbicycle.core_utils.transmit_state

import java.lang.Exception
import com.mbicycle.core_utils.extensions.handleResult
import com.mbicycle.core_utils.extensions.handleResultWithError

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */
/**
 * Wrap class that contains a result of any async operation within
 *
 * To handle this result representation object properly on a Fragments level, please use following functions:
 * [handleResult] or [handleResultWithError], because they are supporting a redirection to a login page
 * when the refresh token process was finished with an error.
 *
 * @author Ilya Kislenka
 */
data class Result<out T>(val status: ResultStatus, val result: T? = null, val exception: Exception? = null)
