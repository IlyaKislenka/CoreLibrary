package com.mbicycle.core_utils

import androidx.annotation.*
import androidx.lifecycle.*

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Class that improves a default behaviour of [MutableLiveData] class,
 * in that way that after all [Observer]'s received a posted data, the
 * data within the [MutableLiveData] will be erased to prevent sending
 * already posted data to an [Observer]'s
 */
class SingleActionLiveData<T> : MutableLiveData<T>() {

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        super.observe(owner, Observer { data ->
            if (data == null) return@Observer
            observer.onChanged(data)
            value = null
        })
    }

    /**
     * Function to post an update to [MutableLiveData] from a [MainThread]
     *
     * @author Ilya Kislenka
     */
    @MainThread
    fun sendAction(data: T) {
        value = data
    }

    /**
     * Function to post an update to [MutableLiveData] from an IO thread
     *
     * @author Ilya Kislenka
     */
    @WorkerThread
    fun postAction(data: T) {
        postValue(data)
    }
}
