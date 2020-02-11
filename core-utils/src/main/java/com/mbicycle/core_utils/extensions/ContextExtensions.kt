@file:JvmName("ContextUtils")
package com.mbicycle.core_utils.extensions

import android.content.Context
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.*
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.*
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.mbicycle.core_utils.basic_auth.*
import java.text.SimpleDateFormat
import java.util.*

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Function that will transform independent pixels to a dependent ones
 */
fun Context.dpToPx(dp: Float): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics)

/**
 * Function to simplify image loading using the [Glide] library
 *
 * @param url is a plain String that should contain a valid path to a web resource
 * @param roundImage if sets as `true` then result image will be rounded
 * @param targetImageView [AppCompatImageView] that will be an image holder
 * @param placeholderRef it's a reference to a drawable resource that will be a placeholder is
 * something went wrong during the image loading
 *
 * @author Ilya Kislenka
 */
fun Context.loadImage(
    url: String,
    roundImage: Boolean = false,
    targetImageView: AppCompatImageView,
    @DrawableRes placeholderRef: Int
) {

    if (url.isEmpty() || url.isBlank() || !url.startsFromAnyOfWebSchemes()) {
        targetImageView.setImageResource(placeholderRef)
        return
    }

    val requestOptions = RequestOptions()
        .priority(Priority.HIGH)
        .error(placeholderRef)
        .placeholder(placeholderRef)
        .format(DecodeFormat.PREFER_RGB_565)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .signature(ObjectKey(SimpleDateFormat("yyyy-MM-dd'T'HH", Locale.getDefault()).format(Date())))


    val glideBuilder = Glide.with(this)
        .setDefaultRequestOptions(requestOptions)
        .load(url)

    if (roundImage)
        glideBuilder.apply(RequestOptions.circleCropTransform())

    glideBuilder.into(targetImageView)
}

/**
 * Function to simplify image loading using the [Glide] library,
 * with an ability to pass [BasicAuthCredentials] to insert them
 * as an HTTP header
 *
 * @param url is a plain String that should contain a valid path to a web resource
 * @param roundImage if sets as `true` then result image will be rounded
 * @param targetImageView [AppCompatImageView] that will be an image holder
 * @param basicAuthCredentials it's a holder for a Basic Auth credentials
 * @param placeholderRef it's a reference to a drawable resource that will be a placeholder is
 * something went wrong during the image loading
 *
 * @author Ilya Kislenka
 */
fun Context.loadImage(
    url: String,
    roundImage: Boolean = false,
    targetImageView: AppCompatImageView,
    basicAuthCredentials: BasicAuthCredentials,
    @DrawableRes placeholderRef: Int
) {

    if (url.isEmpty() || url.isBlank() || !url.startsFromAnyOfWebSchemes()) {
        targetImageView.setImageResource(placeholderRef)
        return
    }

    val requestOptions = RequestOptions()
        .priority(Priority.HIGH)
        .error(placeholderRef)
        .placeholder(placeholderRef)
        .format(DecodeFormat.PREFER_RGB_565)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .signature(ObjectKey(SimpleDateFormat("yyyy-MM-dd'T'HH", Locale.getDefault()).format(Date())))

    val basicAuthorizationEncoder = BasicAuthorizationEncoder()
    val glideBuilder = Glide.with(this)
        .setDefaultRequestOptions(requestOptions)
        .load(
            GlideUrl(
                url,
                LazyHeaders.Builder().addHeader(
                    basicAuthorizationEncoder.headerTitle,
                    basicAuthorizationEncoder.encodeToHeader(basicAuthCredentials)
                ).build()
            )
        )

    if (roundImage)
        glideBuilder.apply(RequestOptions.circleCropTransform())

    glideBuilder.into(targetImageView)
}

/**
 * Function that will erase all the notifications that
 * were created by [NotificationManagerCompat] from
 * the system
 */
fun Context.clearAllAppRelatedNotifications() {
    NotificationManagerCompat.from(applicationContext).cancelAll()
}
