package com.mbicycle.core_utils.recycler_view.recycler_adapters

import androidx.recyclerview.widget.RecyclerView

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * An interface to simplify data binding process with [RecyclerView.Adapter]
 * @see [BindableSingleObjectRecyclerAdapter]
 */
interface BindableAdapter<T: Any> {
    fun setData(data: MutableList<T>)
}
