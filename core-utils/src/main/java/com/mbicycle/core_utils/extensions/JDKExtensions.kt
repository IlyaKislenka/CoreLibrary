@file:JvmName("JDKUtils")
package com.mbicycle.core_utils.extensions

import android.util.SparseArray
import androidx.annotation.IntRange
import com.mbicycle.core_utils.Empty
import java.lang.StringBuilder
import java.util.*
import java.util.regex.Pattern
import com.mbicycle.core_utils.transmit_state.*

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

fun String.canBeParsedToInt() = try {
    Integer.parseInt(this)
    true
} catch (parseException: Exception) {
    false
}

fun String.canBeParsedToFloat() = try {
    java.lang.Float.parseFloat(this)
    true
} catch (parseException: Exception) {
    false
}

/**
 * Function to determine whether any of array items contains
 * @param text or not
 *
 * @param text bunch of chars that you want to find inside the array
 *
 * @author Ilya Kislenka
 */
fun Array<String>.matchPartially(text: String): Boolean {

    forEach {
        if (text.contains(it))
            return true
    }

    return false
}

/**
 * Function to add a value to the end of [SparseArray]
 *
 * @author Ilya Kislenka
 */
fun <T : Any> SparseArray<T>.add(value: T) = append(size(), value)

/**
 * Function to find a first item that match the
 * @param block predicate
 *
 * @return method will return `null` if no one of items is match to the predicate
 *
 * @author Ilya Kislenka
 */
fun <T : Any> SparseArray<T>.find(block: (T) -> Boolean): T? {

    for (i in 0..size()) {
        val value = get(i)
        if (block(value))
            return value
    }

    return null
}

/**
 * Function to find an index of a first item that match the
 * @param block predicate
 *
 * @return method will return -1 if nothing will be found
 *
 * @author Ilya Kislenka
 */
fun <T : Any> SparseArray<T>.firstIndexOf(block: (T) -> Boolean): Int {

    for (i in 0..size()) {
        val value = get(i)
        if (block(value))
            return i
    }
    return -1
}

/**
 * Function to simplify usage of [StringBuilder] class, that way, that you
 * just need to call this function and describe a lambda with operations on
 * a passed [StringBuilder] which was inlined as `this` instance into @param block
 *
 * @author Ilya Kislenka
 */
fun StringBuilder.builder(block: StringBuilder.() -> StringBuilder): String {
    block()
    return toString()
}

/**
 * Function that checks whether the String is match to a regex from @param pattern
 *
 * @param pattern should be a regex
 *
 * @author Ilya Kislenka
 */
fun String.isMatchTo(pattern: String): Boolean {
    return Pattern.compile(pattern).matcher(this).find()
}

/**
 * Function that checks whether the String starts from any of valid web-schemes
 *
 * @author Ilya Kislenka
 */
fun String.startsFromAnyOfWebSchemes() = trim().let {
    startsWith("http://")
    || startsWith("https://")
    || startsWith("ftp://")
}

/**
 * Function that remove all the chars that does not
 * match to a @param regex and return the result
 *
 * @param regex should be a regex
 *
 * @author Ilya Kislenka
 */
fun String.removeAllCharsThatNotMatches(regex: String): String {

    val matcher = Pattern.compile(regex).matcher(this)

    val result = StringBuilder()
    while (matcher.find())
        result.append(matcher.group())

    return result.toString()
}

/**
 * Function that will try to remove all the items from @param entries
 * that contains within the String
 *
 * @author Ilya Kislenka
 */
fun String.removeAllTheEntries(entries: Array<String>): String {
    var result = this
    entries.forEach {
        result = result.replace(it, "")
    }
    return result
}

/**
 * Function that could be useful when the result of the response should be
 * kept inside ViewModel and affect of a ViewModel state somehow,
 * but for an result subscriber of LiveData (Fragment/Activity) is only important to know
 * whether the result was successful or not, and handle exceptions if they were
 *
 * @author Ilya Kislenka
 */
fun <T> Result<T>.wrapWithAnEmptyResult(): Result<Empty> {
    return Result(this.status, if (null != this.result) Empty.instance else null, this.exception)
}

/**
 * Function that could be useful when we need to change the result type inside the [Result],
 * because we do care about the type inside, but only the [ResultStatus] and it's status
 *
 * @param result wrap that will replace original Type inside the result
 *
 * @author Ilya Kislenka
 */
fun <Type, Wrap> Result<Type>.wrapWithAnotherResult(result: Wrap): Result<Wrap> {
    return Result(this.status, if (null != this.result) result else null, this.exception)
}

/**
 * Function that will check that <A> & <B> isn't null and
 * will call @param block with non-nullable types
 *
 * @param block lambda that will receive a non-nullable types inside as soon
 * as both types passed a null check
 *
 * @author Ilya Kislenka
 */
inline fun <A, B, C> ifBothNotNull(first: A?,
                                   second: B?,
                                   block: (A, B) -> C) {
    if (null != first && null != second)
        block(first, second)
}

/**
 * Function that will check that <A> & <B> & <C> isn't null and
 * will call @param block with non-nullable types
 *
 * @param block lambda that will receive a non-nullable types inside as soon
 * as both types passed a null check
 *
 * @author Ilya Kislenka
 */
inline fun <A, B, C, R> ifNotNull(first: A?,
                                  second: B?,
                                  third: C?,
                                  block: (A, B, C) -> R) {
    if (null != first && null != second && null != third)
        block(first, second, third)
}

/**
 * Function that will transform [Map] to a List<Pair<K,V>>
 * that could help to achieve a better api on the data set
 *
 * @author Ilya Kislenka
 */
fun <K : Any, V : Any> Map<K, V>.toListOfPairs(): List<Pair<K, V>> = map { it.key to it.value }

/**
 * Function that will add an item to the list
 * only if it's not there already
 *
 * @author Ilya Kislenka
 */
fun <T> MutableList<T>.addExclusively(item: T) {
    if (!contains(item))
        add(item)
}

/**
 * Function that will remove an item from the list
 * only if that list has that item already
 *
 * @author Ilya Kislenka
 */
fun <T> MutableList<T>.removeWithCheck(item: T) {
    if (contains(item))
        remove(item)
}

/**
 * Function that will help to calculate percentage from @param from
 *
 * @author Ilya Kislenka
 */
fun Long.calculatePercentage(@IntRange(from = 1, to = 99) from: Int): Float {
    return this.toFloat() * (from.toFloat() / 100)
}

/**
 * Function that will return value by @param key,
 * if there is not value found, the empty String then
 *
 * @author Ilya Kislenka
 */
fun Map<String, String>.getStringSafe(key: String) = get(key) ?: ""

/**
 * Function that will try to convert a String representation of the
 * day of the week to the correlate value from [Calendar.DAY_OF_WEEK]
 *
 * @param dayName name of the day of the week, like: "MONDAY", "TUESDAY", etc.
 *
 * @return if @param dayName isn't convertible String, then -1, proper value from [Calendar.DAY_OF_WEEK] otherwise
 *
 * @author Ilya Kislenka
 */
fun Calendar.parseDayNameToNumber(dayName: String) = when (dayName) {
    "MONDAY" -> Calendar.MONDAY
    "TUESDAY" -> Calendar.TUESDAY
    "WEDNESDAY" -> Calendar.WEDNESDAY
    "THURSDAY" -> Calendar.THURSDAY
    "FRIDAY" -> Calendar.FRIDAY
    "SATURDAY" -> Calendar.SATURDAY
    "SUNDAY" -> Calendar.SUNDAY
    else -> -1
}

/**
 * Function that will try to convert day number from [Calendar.DAY_OF_WEEK]
 * to it's String representation like: "MONDAY", "TUESDAY", etc.
 *
 * @param dayNumber should be one of the values from [Calendar.DAY_OF_WEEK], like: [Calendar.MONDAY], [Calendar.TUESDAY], etc.
 *
 * @return if @param dayNumber isn't convertible number, then empty String-1, proper String otherwise
 *
 * @author Ilya Kislenka
 */
fun Calendar.parseDayNumberToName(dayNumber: Int) = when (dayNumber) {
    Calendar.MONDAY -> "MONDAY"
    Calendar.TUESDAY -> "TUESDAY"
    Calendar.WEDNESDAY -> "WEDNESDAY"
    Calendar.THURSDAY -> "THURSDAY"
    Calendar.FRIDAY -> "FRIDAY"
    Calendar.SATURDAY -> "SATURDAY"
    Calendar.SUNDAY -> "SUNDAY"
    else -> ""
}
