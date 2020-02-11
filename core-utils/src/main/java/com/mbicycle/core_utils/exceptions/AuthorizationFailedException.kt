package com.mbicycle.core_utils.exceptions

import java.lang.Exception

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * An exception that should be thrown from a Model's or a Networking
 * layer when refresh token operation was unsuccessful
 */
class AuthorizationFailedException(message: String): Exception(message) 