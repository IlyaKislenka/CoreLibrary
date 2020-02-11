@file:JvmName("DataBindingUtils")
package com.mbicycle.core_utils.extensions

import android.view.View
import androidx.appcompat.widget.*
import androidx.databinding.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.mbicycle.core_utils.static_utils.Logger
import com.mbicycle.core_utils.recycler_view.recycler_adapters.*
import com.mbicycle.core_utils.recycler_view.recycler_adapters.single_selection.*

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Method to simplify subscription on changing of the content inside [ObservableField],
 * designed to use from places, where lambda can't carry off any view-related instance,
 * from an entities that won't be living longer that a view
 *
 * @author Ilya Kislenka
 */
inline fun <T> ObservableField<T>.onChanged(
    crossinline lambda: (T) -> Unit
) {

    val callback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            this@onChanged.get()?.let { lambda(it) }
        }
    }

    addOnPropertyChangedCallback(callback)
}

/**
 * Method to safe subscribe on changing of content inside [ObservableField],
 *
 * Designed to  simplify usage of Observable.OnPropertyChangedCallback.
 * The 'onChangedSafe' extension creates and adds OnPropertyChangedCallback to ObservableField.
 *
 * @usage: We would like to use this simplification from Fragments/Activities that subscribed
 * to a property placed in a ViewModel which lives much longer than a screen, and we need to unsubscribe
 * screen from listening the changes of the property. So we add an lifecycle observer to
 * handle subscription, it's safe because all lifecycle subscribers not lives longer than
 * related view.
 *
 * @author Ilya Kislenka
 */
inline fun <T> ObservableField<T>.onChangedSafe(
    lifecycleOwner: LifecycleOwner,
    crossinline lambda: (T) -> Unit
) {

    val callback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            this@onChangedSafe.get()?.let { lambda(it) }
        }
    }

    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        private fun addPropertyChangedCallback() {
            this@onChangedSafe.addOnPropertyChangedCallback(callback)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        private fun removePropertyChangedCallback() {
            this@onChangedSafe.removeOnPropertyChangedCallback(callback)
        }
    })
}

//region Binding adapters
@BindingAdapter("available")
fun AppCompatRadioButton.setAvailability(available: Boolean) {
    isChecked = available
    isEnabled = available
}

@BindingAdapter("drawableLeftResource")
fun AppCompatTextView.setDrawableLeftResource(resourceRef: ObservableInt) {
    try {
        val drawable = context.getDrawable(resourceRef.get())
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    } catch (didNotFindAResource: Exception) {
        Logger.logError(javaClass::class.java.simpleName,
            "Can't parse ${resourceRef.get()} reference as Drawable resource")
    }
}

@BindingAdapter("onCheckedChangeListener")
fun SwitchCompat.onCheckedChangeListener(block: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        block(isChecked)
    }
}

@BindingAdapter("data")
fun <DataType : Any> RecyclerView.data(data: ObservableArrayList<DataType>) {
    (adapter as? BindableAdapter<DataType>)?.setData(data)
}

@BindingAdapter("operableObject")
fun <OperableObjectType : Any> RecyclerView.operableObject(operableObject: ObservableField<OperableObjectType>) {
    operableObject.get()?.let {
        (adapter as? BindableSingleObjectAdapter<OperableObjectType>)?.setOperableObject(it)
    }
}

@BindingAdapter("singleSelectionListener")
fun <DataType : Any, VH : RecyclerView.ViewHolder> RecyclerView.singleSelectionListener(singleSelectionAdapterListener: SingleSelectionAdapterListener<DataType>) {
    (adapter as? BindableSingleSelectionAdapter<DataType, VH>)?.setSelectionListener(singleSelectionAdapterListener)
}

@BindingAdapter("singleSelectionData")
fun <DataType : Any, VH : RecyclerView.ViewHolder> RecyclerView.singleSelectionData(singleSelectionAdapterDTO: ObservableField<SingleSelectionAdapterDTO<DataType>>) {
    val adapterDTO = singleSelectionAdapterDTO.get()
    (adapter as? BindableSingleSelectionAdapter<DataType, VH>)?.setSingleSelectionData(
        adapterDTO!!.selectedPosition,
        adapterDTO.dataList
    )
}

@BindingAdapter("errorHolder")
fun AppCompatTextView.errorHolder(errorHolderValue: ObservableField<String>) {

    errorHolderValue.onChanged { value ->

        if (value.isNotEmpty()) {
            show()
            text = value
        }
    }
}

@BindingAdapter("visibleIfEmpty")
fun <T> View.visibleIfEmpty(items: ObservableArrayList<T>) {
    visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleIfHasItems")
fun <T> View.visibleIfHasItems(items: ObservableArrayList<T>) {
    visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("changeVisibility")
fun <T> View.changeVisibility(toVisible: ObservableBoolean) {
    visibility = if (toVisible.get()) View.VISIBLE else View.GONE
}

@BindingAdapter("hideIfPositive")
fun <T> View.hideIfPositive(positive: ObservableBoolean) {
    visibility = if (positive.get()) View.GONE else View.VISIBLE
}

@BindingAdapter("enabled")
fun <T> View.enabled(toEnabled: ObservableBoolean) {
    isEnabled = toEnabled.get()
}

@BindingAdapter("changeVisibilityIfLessThanOne")
fun <T> View.changeVisibilityIfLessThanOne(itemsAmount: Int) {
    visibility = if (itemsAmount < 1) View.GONE else View.VISIBLE
}

@BindingAdapter("changeAvailability")
fun <T> View.changeAvailability(items: ObservableArrayList<T>) {
    isEnabled = !items.isEmpty()
}
//endregion
