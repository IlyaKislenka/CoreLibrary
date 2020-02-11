package com.mbicycle.core_utils.basic_auth

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */
/**
 * Data holder to keep a basic auth credentials
 *
 * @Note I would recommend to place your userName & password to following variables:
 *
 * BuildConfig.OAUTH_HANDSHAKE_USERNAME_PHONE
 * BuildConfig.OAUTH_HANDSHAKE_PASSWORD
 *
 * using your local build.gradle file
 */
data class BasicAuthCredentials(val handshakeUserName: String,
                                val handshakePassword: String)
