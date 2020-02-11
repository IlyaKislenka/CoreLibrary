@file:JvmName("ViewUtils")
package com.mbicycle.core_utils.extensions

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mbicycle.core_utils.static_utils.Logger
import com.mbicycle.core_utils.recycler_view.ImprovedHorizontalSmoothScroller
import java.lang.Exception

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Function that will apply a [View.VISIBLE] flag
 * to a [View.setVisibility] method
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Function that will apply a [View.GONE] flag
 * to a [View.setVisibility] method
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Function that will apply a [View.INVISIBLE] flag
 * to a [View.setVisibility] method
 */
fun View.hideHoldingAPlace() {
    visibility = View.INVISIBLE
}

/**
 * Function that will change [View.setVisibility] flag
 * to [View.VISIBLE] or [View.GONE] depending on @param toVisible
 *
 * @author Ilya Kislenka
 */
fun View.changeVisibility(toVisible: Boolean) {
    visibility = if (toVisible) View.VISIBLE else View.GONE
}

/**
 *  Function that will animate [View] property,
 *  depending to a @param toValue and @param animationDuration
 *
 *  @param propertyName the name of the [View] property
 *  to animate. Like: [View.getAlpha], [View.getX], etc...
 *  @param toValue the value that will be set at corresponding
 *  view property
 *  @param animationDuration duration of an animation
 *
 *  @author Ilya Kislenka
 */
fun View.animateProperty(propertyName: String,
                         toValue: Int,
                         animationDuration: Long) {
    ObjectAnimator.ofInt(this, propertyName, toValue).apply {
        duration = animationDuration
        interpolator = LinearInterpolator()
        start()
    }
}

/**
 * Function the will measure the [View] by absolute value,
 * right after this method will be called, the [View] will have
 * correct values of [View.getWidth] and [View.getHeight]
 * and all the margins/padding
 *
 * @author Ilya Kislenka
 */
fun View.measureCompletely() {
    measure(
        View.MeasureSpec.makeMeasureSpec(this.measuredWidth, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(this.measuredHeight, View.MeasureSpec.UNSPECIFIED)
    )
}

/**
 * Function that will call a focus to this view
 *
 * @author Ilya Kislenka
 */
fun View.focus() {

    post {
        if (requestFocus()) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

/**
 * Function that will erase the text inside [TextView]
 *
 * @author Ilya Kislenka
 */
fun TextView.clear() {
    text = ""
}

/**
 * Function the simplifies [View] binding process with a [RecyclerView.ViewHolder]
 * Designed to be called from [RecyclerView.Adapter.onCreateViewHolder] with a params from
 * there
 *
 * @param parent the parent view from [RecyclerView.Adapter.onCreateViewHolder] to get a layout params
 * @param layoutRef the reference to an layout resource to inflate
 *
 * @author Ilya Kislenka
 */
fun <VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.inflateView(parent: ViewGroup,
                                                                        @LayoutRes layoutRef: Int): View =
    LayoutInflater.from(parent.context).inflate(layoutRef, parent, false)

/**
 *  Function that simplifies the scrolling process of [RecyclerView]
 *
 *  @sample `recyclerView.scrollBySmoothScroller<LinearLayoutManager>(position)`
 *
 *  @param toPosition position to scroll to
 *  @Note If the position isn't valid or [RecyclerView.LayoutManager] isn't
 *  in the correct state the exception will be handled properly and to scroll will be performed
 *
 *  @author Ilya Kislenka
 */
fun <LayoutManagerType : RecyclerView.LayoutManager> RecyclerView.scrollBySmoothScroller(
    toPosition: Int
) {
    try {
        post {
            val position = if (toPosition >= 0) toPosition else 0
            val smoothScroller = ImprovedHorizontalSmoothScroller(
                this.context).apply {
                targetPosition = position
            }
            (layoutManager as? LayoutManagerType)?.startSmoothScroll(smoothScroller)
        }
    } catch (recyclerNotInTheCorrectState: Exception) {
        Logger.logError(
            "ViewExtensions",
            "Can't scroll recycler, to position: $toPosition, due to: $recyclerNotInTheCorrectState, exception"
        )
    }
}
