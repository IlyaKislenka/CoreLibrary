@file:JvmName("DateExtensions")
package com.mbicycle.core_utils.extensions

import java.text.SimpleDateFormat
import java.util.*

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Function will format a [Date] by @param pattern, and return formatted String
 *
 *  @param pattern any combination of chars from the doc below
 *  @see <a href="https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html</a>
 *  @param locale desired locale to affect the formatting process
 *
 *  @return return a formatted String or an empty String in a case of formatting exception
 *
 *  @author Ilya Kislenka
 */
fun Date.formatByPattern(pattern: String = "MM-dd-yyyy",
                         locale: Locale = Locale.getDefault()): String = try {
    SimpleDateFormat(pattern, locale).format(this)
} catch (formattingException: Exception) {
    ""
}

/**
 * Function for debugging purpose, that will transform a [Date] to
 * a [Triple] with [Calendar.YEAR], [Calendar.MONTH], [Calendar.DAY_OF_MONTH] within
 *
 * @return a [Triple] fulfilled with a values from [Calendar.YEAR], [Calendar.MONTH], [Calendar.DAY_OF_MONTH]
 *
 * @author Ilya Kislenka
 */
fun Date.toYMD(): Triple<Int, Int, Int> = Calendar.getInstance().let {
    it.time = this
    Triple(it.get(Calendar.YEAR),
        it.get(Calendar.MONTH),
        it.get(Calendar.DAY_OF_MONTH))
}

/**
 * Function for debugging purpose, that will transform a [Date] to
 * a proper String representation of a [Date] in a following format `dd.MM.yyyy, hh:mm aaa`
 * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html">https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html</a>
 *
 * @author Ilya Kislenka
 */
fun Date.toDetailDate(): String = SimpleDateFormat("dd.MM.yyyy, hh:mm aaa", Locale.US).format(this)
