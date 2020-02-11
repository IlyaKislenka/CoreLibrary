@file:JvmName("BaseArchitectureExtensions")

package com.mbicycle.core_architecture.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mbicycle.core_architecture.BaseDaggerActivity
import com.mbicycle.core_architecture.BaseDaggerActivityImpl

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Function that simplifies a providing a [ViewModel] for
 * a [Fragment] from a Daggers generated AndroidViewModelFactory
 *
 * @Note should be used only with a custom AndroidViewModelFactory when a Dagger
 * created all the arguments for all the [ViewModel]'s
 *
 * @author Ilya Kislenka
 */
fun <VM : ViewModel> Fragment.injectViewModel(clazz: Class<VM>): VM {

    val activity = requireActivity()
    val factory = (activity as BaseDaggerActivity).provideViewModelsFactory()
    return ViewModelProvider(activity, factory)[clazz]
}
