package com.mbicycle.core_utils.recycler_view.recycler_adapters.single_selection

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

data class SingleSelectionAdapterDTO<T>(var selectedPosition: Int = 0, val dataList: MutableList<T> = mutableListOf())
