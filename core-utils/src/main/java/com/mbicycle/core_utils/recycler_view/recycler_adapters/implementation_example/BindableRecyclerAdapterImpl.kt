package com.mbicycle.core_utils.recycler_view.recycler_adapters.implementation_example

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.mbicycle.core_utils.extensions.inflateView
import com.mbicycle.core_utils.recycler_view.recycler_adapters.BindableRecyclerAdapter

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */
/**
 * An example of implementation for [BindableRecyclerAdapter] abstraction
 */
class BindableRecyclerAdapterImpl : BindableRecyclerAdapter<Any, BindableRecyclerAdapterImpl.ExampleViewHolder>() {

    /**
     * Callbacks that represents an Any as a passed value
     * to a listener
     */
    internal lateinit var onItemClicked: (Any) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExampleViewHolder(inflateView(parent, View.NO_ID))

    override fun onBindViewHolder(holder: ExampleViewHolder,
                                  position: Int) {

        val item = getDataItem(position)

        holder.apply {

            //TODO ... bind an item and view holder's views here

            /**
             * An example of proper actions handling with a binding to a position
             */
            anyViewHere.apply {
                tag = position
                setOnClickListener {
                    val tagPos = tag as Int
                    if (::onItemClicked.isInitialized) {
                        val item = getDataItem(tagPos)
                        onItemClicked.invoke(item)
                    }
                }
            }
        }
    }

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val anyViewHere: View = itemView.findViewById(View.NO_ID)
    }
}
