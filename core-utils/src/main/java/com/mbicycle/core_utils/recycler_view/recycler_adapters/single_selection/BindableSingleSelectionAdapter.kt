package com.mbicycle.core_utils.recycler_view.recycler_adapters.single_selection

import androidx.recyclerview.widget.RecyclerView
import com.mbicycle.core_utils.recycler_view.recycler_adapters.BindableAdapter

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

abstract class BindableSingleSelectionAdapter<DataType : Any, ViewHolderType : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<ViewHolderType>(), BindableAdapter<DataType> {

    protected var selectedPosition: Int = 0
    protected lateinit var dataList: MutableList<DataType>
    protected lateinit var singleSelectionListener: SingleSelectionAdapterListener<DataType>

    fun hasSelectionListener() = ::singleSelectionListener.isInitialized

    fun setSelectionListener(singleSelectionListener: SingleSelectionAdapterListener<DataType>) {
        this.singleSelectionListener = singleSelectionListener
    }

    fun setSingleSelectionData(selectedPosition: Int, data: MutableList<DataType>) {
        this.selectedPosition = selectedPosition
        setData(data)
    }

    override fun setData(data: MutableList<DataType>) {
        dataList = data
        applyState()
        notifyDataSetChanged()
    }

    /**
     * Callback to modify received data right before adapter starting to bind the views
     */
    protected open fun applyState() {}

    override fun getItemCount() = if (::dataList.isInitialized) dataList.size else 0
}
