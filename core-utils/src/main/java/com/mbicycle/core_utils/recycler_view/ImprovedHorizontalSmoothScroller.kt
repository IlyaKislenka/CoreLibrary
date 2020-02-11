package com.mbicycle.core_utils.recycler_view

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller
import com.mbicycle.core_utils.extensions.scrollBySmoothScroller

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Implementation of [LinearSmoothScroller] with a [LinearSmoothScroller.SNAP_TO_ANY] preference
 * pre-installed
 *
 * @see [scrollBySmoothScroller] extension function
 */
class ImprovedHorizontalSmoothScroller(context: Context) : LinearSmoothScroller(context) {

    /**
     * This means, that layout manager will scroll recycler to the item, and that
     * item will be completely fits and visible on the screen
     */
    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_ANY
    }
}
