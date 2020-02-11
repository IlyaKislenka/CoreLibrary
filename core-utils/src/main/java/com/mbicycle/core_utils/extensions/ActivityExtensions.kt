@file:JvmName("ActivityUtils")
package com.mbicycle.core_utils.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(window?.decorView?.windowToken, 0)
}
