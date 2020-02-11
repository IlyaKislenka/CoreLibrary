package com.mbicycle.core_utils.basic_auth

import okhttp3.Credentials

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Class to simplify creating an HTTP headers with an encoded basic authorization credentials within
 */
class BasicAuthorizationEncoder {

    val headerTitle
        get() = "Authorization"

    /**
     * Method to encode passed [BasicAuthCredentials] to an HTTP header
     */
    fun encodeToHeader(credentials: BasicAuthCredentials): String = Credentials.basic(credentials.handshakeUserName, credentials.handshakeUserName)
}
