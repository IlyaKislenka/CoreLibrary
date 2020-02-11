@file:JvmName("ParcelUtils")
package com.mbicycle.core_utils.extensions

import android.os.*

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */
/**
 * Function that will read the [String] from
 * [Parcel], and return an empty [String] when
 * there is nothing associated in that order left
 * 
 * @author Ilya Kislenka
 */
fun Parcel.readStringSafe(): String {
    return this.readString() ?: ""
}

/**
 * Function that will read a [Parcelable] instance
 * by following order and will return a strong type
 *
 * @param classLoader it's an instance of [ClassLoader] for a concrete
 * [Parcelable] type
 *
 * @Note use that function only when you're sure that
 * you put a [Parcelable] in that order
 *
 * @author Ilya Kislenka
 */
fun Parcel.readParcelableSafe(classLoader: ClassLoader): Parcelable {
    return this.readParcelable(classLoader)!!
}

/**
 * Function that will real a [List] of specified type
 * from [Parcel] to an @param outputList
 *
 * @param outputList it's a list where an items will be appended
 * @param classLoader it's an instance of [ClassLoader] for specified type `T`
 *
 * @author Ilya Kislenka
 */
fun <T> Parcel.readListSafe(outputList: MutableList<T>,
                            classLoader: ClassLoader): List<T> {
    readList(outputList, classLoader)
    return outputList
}

/**
 * Function that will real a [Map] of specified types
 * from [Parcel] to an @param outputMap
 *
 * @param outputMap it's a map where an items will be appended
 * @param classLoader it's an instance of [ClassLoader] for specific [Map] implementation,
 * could be a HashMap::class.java.classLoader or any others implementations
 *
 * @author Ilya Kislenka
 */
fun <K, V> Parcel.readMapSafe(outputMap: MutableMap<K, V>,
                              classLoader: ClassLoader): Map<K, V> {
    readMap(outputMap, classLoader)
    return outputMap
}
