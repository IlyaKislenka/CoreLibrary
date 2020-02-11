@file:JvmName("FragmentUtils")
package com.mbicycle.core_utils.extensions

import android.app.*
import android.os.*
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.mbicycle.core_utils.date.DateUtils
import java.lang.Exception
import android.view.WindowManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import com.mbicycle.core_utils.dialogs.GenericAlertDialogFragment
import com.mbicycle.core_utils.exceptions.AuthorizationFailedException
import com.mbicycle.core_utils.static_utils.IntentsController
import com.mbicycle.core_utils.static_utils.Logger
import com.mbicycle.core_utils.transmit_state.*
import java.util.Calendar

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

//region Common tools
/**
 * Function that will check if whether this instance of a [Fragment] could be used or not
 *
 * @author Ilya Kislenka
 */
fun Fragment.isAliveAndAvailable() = isAdded && !isRemoving && null != context && null != view

/**
 * Function that will schedule some work in a Main Thread scheduler,
 * and runs with a delay in UI Thread
 *
 * @param delay delay in milliseconds
 * @param block lambda to execute after delay
 *
 * @author Ilya Kislenka
 */
fun Fragment.runDelayedInMainThread(delay: Long, block: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        block()
    }, delay)
}

/**
 * Function that will return an instance of [WindowManager] from activity
 *
 * @author Ilya Kislenka
 */
fun Fragment.getWindowManager() = activity?.windowManager

/**
 * Function to safe append some arguments to an existing
 * Fragment arguments
 *
 * @param block lambda that will be called with an existing Fragment's [Bundle] to
 * append something
 *
 * @see block won't be called when it's not possible to append an arguments to a [Fragment]
 *
 * @author Ilya Kislenka
 */
fun Fragment.appendToArgumentsSafe(block: (Bundle) -> Unit) {

    if (isAdded && isStateSaved)
        return

    this.let {
        if (it.arguments == null)
            it.arguments = Bundle()
        it.arguments?.let { bundle ->
            block.invoke(bundle)
        }
    }
}
//endregion

//region Navigation
/**
 * Function that simplifies navigation between an actions within
 * NavigationComponent
 *
 * @param actionID it's an actionID ref from `navigation` xml resource
 *
 * @Note if you will pass an actionID that is unknown for this [Fragment]
 * that will be a cause a [RuntimeException]
 *
 * @author Ilya Kislenka
 */
fun Fragment.navigate(@IdRes actionID: Int) {

    if (isAliveAndAvailable())
        view?.let {
            Navigation.findNavController(it).navigate(actionID)
        }
}

/**
 * Function that simplifies navigation between an actions within
 * NavigationComponent
 *
 * @param actionID it's an actionID ref from `navigation` xml resource
 * @param bundle it's an arguments for a desired Fragment to open
 *
 * @Note if you will pass an actionID that is unknown for this [Fragment]
 * that will be a cause a [RuntimeException]
 *
 * @author Ilya Kislenka
 */
fun Fragment.navigate(@IdRes actionID: Int,
                      bundle: Bundle) {

    if (isAliveAndAvailable())
        view?.let {
            Navigation.findNavController(it).navigate(actionID, bundle)
        }
}

//region BackStack handling
/**
 * Function that should be called from a [OnBackPressedCallback], to popUpBackStack() of
 * Fragments in Navigation Controller
 *
 * @author Ilya Kislenka
 */
fun Fragment.navigateUp() {
    try {
        NavHostFragment.findNavController(this).navigateUp()
    } catch (anyRuntimeException: Throwable) {
        Logger.logError("FragmentUtils",
            "An error occurred during the popUpBackStack() operation",
            error = anyRuntimeException)
    }
}
//endregion

/**
 * Function that will popUpBackStack of Fragments inside
 * NavigationComponent
 *
 * @Note We can't use [NavController.popBackStack] here,
 * because then, registered onBackPressedListeners in a
 * previous [Fragment]'s won't be called
 *
 * @Note This function should be called from Fragments,
 * but not from [OnBackPressedCallback], this could be a cause for recursion call
 */
fun Fragment.popBackStack() {

    if (isAliveAndAvailable())
        view?.let {
            try {
                requireActivity().onBackPressed()
            } catch (anyRuntimeException: Throwable) {
                Logger.logError("FragmentUtils",
                    "An error occurred during the popUpBackStack() operation",
                    error = anyRuntimeException)
            }
        }
}

/**
 * Function to simplify handling of a regular back button
 * within the [Fragment]. Designed to popUpBackStack of fragments
 *
 * @param backButtonID it's an ID reference of back button [View] within
 * this [Fragment]
 *
 * @Note We can't use [NavController.popBackStack] here,
 * because then, registered onBackPressedListeners in a
 * previous [Fragment]'s won't be called
 *
 * @Note This function should be called from Fragments,
 * but not from [OnBackPressedCallback], this could be a cause for recursion call
 */
fun Fragment.handleBackButton(@IdRes backButtonID: Int) {

    if (isAliveAndAvailable())
        view?.let { v ->
            val backButton = v.findViewById<View>(backButtonID)
            backButton?.setOnClickListener {
                try {
                    requireActivity().onBackPressed()
                } catch (anyRuntimeException: Throwable) {
                    Logger.logError("FragmentUtils",
                        "An error occurred during the popUpBackStack() operation",
                        error = anyRuntimeException)
                }
            }
        }
}
//endregion

//region Dialogs
/**
 * Function that will show a plain [AlertDialog] fulfilled
 * with a passed params
 *
 * @author Ilya Kislenka
 */
fun Fragment.showDialog(
    title: String = "",
    message: String = "",
    positiveButtonMessage: String = "",
    positiveListener: (() -> Unit)? = null,
    negativeButtonMessage: String = "",
    negativeListener: (() -> Unit)? = null,
    neutralButtonMessage: String = "",
    neutralListener: (() -> Unit)? = null,
    cancelable: Boolean = true
) {

    val dialogTag = GenericAlertDialogFragment::class.java.name
    val alreadyExistWithinBackStack = null != childFragmentManager.findFragmentByTag(dialogTag)

    if (!alreadyExistWithinBackStack && isAliveAndAvailable()) {

        val dialogFragment = GenericAlertDialogFragment.build(
            title, message,
            positiveButtonMessage, positiveListener,
            negativeButtonMessage, negativeListener,
            neutralButtonMessage, neutralListener, cancelable
        )

        dialogFragment.showNow(childFragmentManager, dialogTag)
    }
}

/**
 * Function that will show a plain [DatePickerDialog] and will
 * setup it with a passed params, with an ability to handle picked
 * result
 *
 * @param minDate it's a timestamp of any minimum date that you would like to be within the picker
 * @param maxDate it's a timestamp of any maximum date that you would like to be within the picker
 * @param preselectedYear it's a [Calendar.YEAR] that you would like to be preselected
 * @param preselectedMonth it's a [Calendar.MONTH] that you would like to be preselected
 * @param preselectedDayOfTheMonth it's a [Calendar.DAY_OF_MONTH] that you would like to be preselected
 * @param onDatePicked it's a lambda that will return the picked result as three Ints which represents
 * [Calendar.YEAR], [Calendar.MONTH], [Calendar.DAY_OF_MONTH]
 *
 * @Note By default the [DatePickerDialog] will be preselected with a current YMD,
 * min date will be an actual minimal date in [Calendar] and maximum date will be current date
 *
 * @author Ilya Kislenka
 */
fun Fragment.showDatePickerDialog(
    minDate: Long = DateUtils.getActualMinimumDate().time,
    maxDate: Long = System.currentTimeMillis(),
    preselectedYear: Int = DateUtils.recentYear,
    preselectedMonth: Int = DateUtils.recentMonth,
    preselectedDayOfTheMonth: Int = DateUtils.recentDay,
    onDatePicked: (Int, Int, Int) -> Unit
) {
    if (isAliveAndAvailable()) {
        context?.let {
            val datePickerDialog = DatePickerDialog(it, { _, year, month, dayOfMonth ->
                onDatePicked(year, month, dayOfMonth)
            }, preselectedYear, preselectedMonth, preselectedDayOfTheMonth)

            datePickerDialog.datePicker.apply {
                this.minDate = minDate
                this.maxDate = maxDate
            }
            datePickerDialog.show()
        }
    }
}

/**
 * Function that will show a plain [TimePickerDialog] and will
 * setup it with a passed params, with an ability to handle picked
 * result
 *
 * @param is24HoursView setup a [TimePickerDialog] with a format of displaying by 24-hours or only 12 and AM/PM
 * selector
 * @param preselectedHour it's a [Calendar.HOUR] that you would like to be preselected
 * @param preselectedMinute it's a [Calendar.MINUTE] that you would like to be preselected
 * @param onTimePicked it's a lambda that will return the picked result as two Ints which represents
 * [Calendar.HOUR] or [Calendar.HOUR_OF_DAY] depending on @param is24HoursView,
 * [Calendar.MINUTE]
 *
 * @Note By default the [TimePickerDialog] will be preselected in 12 hours format with a current
 * [Calendar.HOUR] and [Calendar.MINUTE] preselected
 *
 * @author Ilya Kislenka
 */
fun Fragment.showTimePickerDialog(
    is24HoursView: Boolean = false,
    preselectedHour: Int = DateUtils.currentHourIn12Format,
    preselectedMinute: Int = DateUtils.recentMinute,
    onTimePicked: (Int, Int) -> Unit
) {
    if (isAliveAndAvailable()) {
        context?.let {
            val onTimePickerDialog = TimePickerDialog(it, { _, hour, minute ->
                onTimePicked(hour, minute)
            }, preselectedHour, preselectedMinute, is24HoursView)
            onTimePickerDialog.show()
        }
    }
}
//endregion

/**
 * Function to simplify handling of a back pressed action inside the [Fragment]
 *
 * @param callback a lambda that will be called as soon as further/next fragment was
 * removed from back stack
 *
 * @return [OnBackPressedCallback] to the [Fragment] to remove this callback
 * in a [Fragment.onDestroyView] method. Can return `null` if it's not possible to
 * add a [OnBackPressedCallback] at the moment
 *
 * @sample
 *          override fun onDestroyView() {
 *                onBackPressedCallback?.remove()
 *           }
 *
 * @author Ilya Kislenka
 */
fun Fragment.addOnBackPressedCallback(callback: () -> Unit): OnBackPressedCallback? {

    val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            callback.invoke()
        }
    }

    return if (isAliveAndAvailable()) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
        backPressedCallback
    } else
        null
}

/**
 * Return the "target" for this fragment of specified type. By default target is activity that owns
 * current fragment but also could be any fragment.
 *
 * @param clazz requested callback interface
 * @return requested callback or null if no callback of requested type is found
 *
 * @author Ilya Kislenka
 */
fun <T : Any> Fragment.getTarget(clazz: Class<T>): T? {

    val targets = arrayOf(targetFragment, parentFragment, activity)

    targets.forEach {
        if (null != it && clazz.isInstance(it)) {
            return clazz.cast(it)
        }
    }
    return null
}

//region Async result handling
/**
 * Function that simplifies handling of a [Result] of any async operation on
 * a UI layer
 *
 * @param result it's a result of an async operation
 * @param completeIfSuccess it's a lambda that will be called as soon
 * as result was successful and contains an expected type
 * @param activityClassToRedirect it's a class of the [Activity] to
 * redirect use when [Result] contains [AuthorizationFailedException] as an
 * exception within. If you don't need to use that behavior just, don't pass
 * an [Activity] class and don't throw a [AuthorizationFailedException] at all
 *
 * @Note If result wasn't successful, then the [GenericAlertDialogFragment]
 * will be show with an exception message within
 *
 * @author Ilya Kislenka
 */
inline fun <T> Fragment.handleResult(
    result: Result<T>,
    completeIfSuccess: (T) -> Unit,
    activityClassToRedirect: Class<out Activity>? = null
) {

    val (status, requestResult, exception) = result

    if (status == ResultStatus.SUCCESS)
        requestResult?.let {
            completeIfSuccess.invoke(it)
        }
    else {
        exception?.let {
            it.message?.let { message ->
                showDialog(message = message)
            }
            if (exception is AuthorizationFailedException)
                runDelayedInMainThread(1500) {
                    context?.let { context ->
                        activityClassToRedirect?.let { activityClass ->
                            IntentsController.startActivityWithANewStack(context, activityClass)
                        }
                    }
                }
        }
    }
}

/**
 * Function that simplifies handling of a [Result] of any async operation on
 * a UI layer
 *
 * @param result it's a result of an async operation
 * @param completeIfSuccess it's a lambda that will be called as soon
 * as result was successful and contains an expected type
 * @param completeIfError it's a lambda that will be called when
 * operation was unsuccessful, right before [GenericAlertDialogFragment] will be
 * show
 * @param activityClassToRedirect it's a class of the [Activity] to
 * redirect use when [Result] contains [AuthorizationFailedException] as an
 * exception within. If you don't need to use that behavior just, don't pass
 * an [Activity] class and don't throw a [AuthorizationFailedException] at all
 *
 * @Note If result wasn't successful, then the [GenericAlertDialogFragment]
 * will be show with an exception message within
 *
 * @author Ilya Kislenka
 */
inline fun <T> Fragment.handleResultWithError(
    result: Result<T>,
    completeIfError: (Exception) -> Unit,
    completeIfSuccess: (T) -> Unit,
    activityClassToRedirect: Class<out Activity>? = null
) {
    val (status, requestResult, exception) = result

    if (status == ResultStatus.SUCCESS)
        requestResult?.let {
            completeIfSuccess.invoke(it)
        }
    else {
        exception?.let {
            completeIfError.invoke(it)
            it.message?.let { message ->
                showDialog(message = message)
            }
            if (exception is AuthorizationFailedException)
                runDelayedInMainThread(1500) {
                    context?.let { context ->
                        activityClassToRedirect?.let { activityClass ->
                            IntentsController.startActivityWithANewStack(context, activityClass)
                        }
                    }
                }
        }
    }
}
//endregion
