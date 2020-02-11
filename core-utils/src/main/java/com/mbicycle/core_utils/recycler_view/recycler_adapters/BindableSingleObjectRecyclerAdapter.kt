package com.mbicycle.core_utils.recycler_view.recycler_adapters

import androidx.recyclerview.widget.RecyclerView

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * An abstraction on [RecyclerView.Adapter] that simplify data binding process.
 * Supposed to be used when there is no data list to set inside an adapter, but a single
 * object with a data, like an object with a time bounds as timestamps.
 */
abstract class BindableSingleObjectRecyclerAdapter<OperableObject : Any, ViewHolderType : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<ViewHolderType>(), BindableSingleObjectAdapter<OperableObject> {

    /**
     * An object that supposed to be provided by
     * @see [setOperableObject] method
     */
    protected lateinit var operObject: OperableObject

    /**
     * Method that supposed to be called from
     * @see [operableObject] method from BindingExtensions file, which should
     * be declared inside xml file with [RecyclerView] within.
     * Like that:
     *                <androidx.recyclerview.widget.RecyclerView
     *                       operableObject="@{viewModel.anyOfObservableField}"
     *                       .../>
     *
     */
    override fun setOperableObject(operableObject: OperableObject) {
        operObject = operableObject
        applyState()
        notifyDataSetChanged()
    }

    /**
     * Method that will be called as soon as data arrived, but
     * before @see [RecyclerView.Adapter.notifyDataSetChanged], so you
     * could apply your mapping there by using @see [operObject] variable
     */
    protected open fun applyState() {}
}
