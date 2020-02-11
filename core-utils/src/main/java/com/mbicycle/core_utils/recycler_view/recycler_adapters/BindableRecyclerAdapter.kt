package com.mbicycle.core_utils.recycler_view.recycler_adapters

import androidx.recyclerview.widget.RecyclerView
import com.mbicycle.core_utils.recycler_view.recycler_adapters.implementation_example.BindableRecyclerAdapterImpl

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * An abstraction on [RecyclerView.Adapter] to simplify data binding process
 *
 * see Example of implementation here - [BindableRecyclerAdapterImpl]
 *
 * @author Ilya Kislenka
 */
abstract class BindableRecyclerAdapter<DataType : Any, ViewHolderType : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<ViewHolderType>(),
    BindableAdapter<DataType> {

    /**
     * Data list that supposed to be provided by
     * @see [setData] method
     */
    protected lateinit var dataList: MutableList<DataType>

    /**
     * Method that supposed to be called from
     * @see [data] method from BindingExtensions file, which should
     * be declared inside xml file with [RecyclerView] within.
     * Like that:
     *                <androidx.recyclerview.widget.RecyclerView
     *                       data="@{viewModel.anyOfObservableList}"
     *                       .../>
     *
     */
    override fun setData(data: MutableList<DataType>) {
        dataList = data
        applyState()
        notifyDataSetChanged()
    }

    /**
     * Method that will be called as soon as data arrived, but
     * before @see [RecyclerView.Adapter.notifyDataSetChanged], so you
     * could apply your mapping there by using @see [dataList] variable
     */
    protected open fun applyState() {}

    /**
     * Function that returns an item by position
     */
    protected fun getDataItem(adapterPosition: Int) = dataList[adapterPosition]

    /**
     * Function overridden in here to avoid
     * boilerplate code on any concrete classes
     */
    override fun getItemCount() = if (::dataList.isInitialized) dataList.size else 0
}
