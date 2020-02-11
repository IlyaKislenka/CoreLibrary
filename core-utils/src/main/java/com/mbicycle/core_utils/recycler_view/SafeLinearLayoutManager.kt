package com.mbicycle.core_utils.recycler_view

import android.content.Context
import androidx.recyclerview.widget.*
import com.mbicycle.core_utils.static_utils.Logger
import java.lang.Exception

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */
/**
 * This is a workaround to fix a common recycler view bug according to indexing a children positions
 */
class SafeLinearLayoutManager(context: Context,
                              orientation: Int,
                              reverseLayout: Boolean) : LinearLayoutManager(context,
    orientation,
    reverseLayout) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (ignore: Exception) {
            Logger.logInfo(SafeLinearLayoutManager::class.java.simpleName, "Layout manager caught an error, $ignore")
        }
    }
}
