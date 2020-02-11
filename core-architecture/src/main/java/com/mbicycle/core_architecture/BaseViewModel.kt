package com.mbicycle.core_architecture

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.mbicycle.core_multithreading.ThreadsSeparator
import com.mbicycle.core_utils.SingleActionLiveData
import com.mbicycle.core_utils.transmit_state.Result
import kotlinx.coroutines.coroutineScope

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Class that simplifies a threads and loading state handling for
 * any inherited classes
 */
abstract class BaseViewModel(private val threadsSeparator: ThreadsSeparator) : ViewModel() {

    /**
     * Variable to keep the latest state of loading
     * for particular [Fragment]. The variable
     * will be changing it's state as soon as
     * [asyncLoading] function will be called and
     * finishes the job inside
     *
     * @author Ilya Kislenka
     */
    private val isLoading = SingleActionLiveData<Boolean>()

    /**
     * The subscription that designed to be called
     * only from the [Fragment] or [Activity] (UI layer),
     * that you can hide or show the progress bar specific to the
     * screen depending on a state inside
     *
     * @author Ilya Kislenka
     */
    fun subscribeToLoadingState(): LiveData<Boolean> = isLoading

    /**
     * Function that will help to separate threads and execute
     * async an operation/s block passed as an argument
     * Designed to be called from an existing CoroutineScope,
     * @example `viewModelScope` inside the [AndroidViewModel]
     *
     * @author Ilya Kislenka
     */
    suspend fun <T> asyncLoading(func: suspend () -> T): Result<T> = coroutineScope {

        isLoading.postValue(true)

        val result = threadsSeparator.startRequest { func() }

        isLoading.postValue(false)

        result
    }

    /**
     * This is a placeholder function to clean up view model references,
     * which shouldn't be kept while current fragment is hidden for user, but not
     * destroyed completely. @sample User called a move forward action
     *
     * @Note Designed to be used only when we attaching an [AndroidViewModel]
     * to an [Activity] not to the [Fragment], and we need manually clean some
     * view model's references. The best place to call it is a [Fragment.onDestroyView]
     *
     * @see [BaseDataBindingFragment] as an example
     */
    open fun cleanUp() {}

    /**
     * This is a placeholder function to clean up view model temporary
     * references finally, which shouldn't live more than a fragment (like user selection).
     *
     * @Note Designed to be used only when we attaching an [AndroidViewModel]
     * to an [Activity] not to the [Fragment], and we need manually clean some
     * view model's references. The best place to call it is a [Fragment.onDestroy]
     *
     * @Note Should be called when user move away from the fragment (popUpBackStack action)
     * and [Fragment] will be destroyed finally. @sample [BaseDataBindingFragment]
     *
     * @author Ilya Kislenka
     */
    open fun cleanUpFinally() {}
}
