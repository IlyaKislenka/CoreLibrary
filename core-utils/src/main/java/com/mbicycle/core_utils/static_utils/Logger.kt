package com.mbicycle.core_utils.static_utils

import android.util.Log
import java.lang.Exception

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Util class for control logging messages between build types.
 * Mostly for disable the logs on the "release" build type.
 *
 * @author Ilya Kislenka
 */
object Logger {

    /**
     * Function to log out some code,
     * won't print any logs on `release` build type for better security
     *
     * @param tag custom tag
     * @param message custom message
     * @param buildType is a build type from [BuildConfig.BUILD_TYPE]
     *
     * @author Ilya Kislenka
     */
    fun logInfo(tag: String,
                message: String,
                buildType: String = "") {
        if (buildType != "release")
            Log.i(tag, message)
    }

    /**
     * Function to log out some exceptional case,
     * won't print any logs on `release` build type for better security
     *
     * @param tag custom tag
     * @param error cause error
     * @param message custom message
     * @param buildType is a build type from [BuildConfig.BUILD_TYPE]
     *
     * @author Ilya Kislenka
     */
    fun logError(tag: String,
                 message: String,
                 buildType: String = "",
                 error: Throwable? = Exception()) {
        if (buildType != "release")
            Log.e(tag, message, error)
    }
}
