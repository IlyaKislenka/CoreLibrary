package com.mbicycle.core_utils.recycler_view.recycler_adapters.single_selection

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

interface SingleSelectionAdapterListener<T : Any> {
    fun onSingleSelection(adapterPosition: Int, dataItem: T)
}
