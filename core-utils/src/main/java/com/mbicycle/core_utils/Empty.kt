package com.mbicycle.core_utils

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Entity for wrapping an empty request/response body and preventing passing a null into the system
 *
 * @author Ilya Kislenka
 */
class Empty private constructor() {
    companion object {
        val instance
            get() = Empty()
    }
}
