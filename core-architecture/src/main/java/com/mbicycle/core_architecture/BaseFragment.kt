package com.mbicycle.core_architecture

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mbicycle.core_architecture.extensions.injectViewModel
import com.mbicycle.core_utils.extensions.*

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Class that simplifies work with a [Fragment]'s and allow you
 * to use a lot of abilities to handle loading indicator process,
 * set a back pressed listener, swipe to refresh layout, etc...
 *
 * @Note your [Activity] should be an implementation of [BaseDaggerActivity]
 */
abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: VM

    /**
     * You need to specify your own R.layout.fragment_layout
     * for particular [Fragment] here
     */
    protected abstract fun getLayoutID(): Int

    /**
     * You need to specify your own implementation
     * of [BaseViewModel] that supposed to be used
     * with your [Fragment]
     */
    protected abstract fun getVMClass(): Class<VM>

    //region Support buttons id's
    /**
     * If your project isn't supposed to use some of this
     * functionality, just create a your own class that inherits
     * from BaseFragment and provide an empty realization for method,
     * and then inherit your fragment from your implementation
     */
    protected abstract fun getToolbarBackButtonID(): Int

    protected abstract fun getLoadingStateCurtainID(): Int
    protected abstract fun getSwipeToRefreshContainerID(): Int
    /**
     * Function that should be used only in combination with
     * [getSwipeToRefreshContainerID] to set a particular colors to your
     * [SwipeRefreshLayout]. Where [Pair] first - it's an accent color
     * and second - it's a foreground/secondary color
     */
    protected abstract fun getSwipeToRefreshContrastColors(): Pair<Int, Int>
    //endregion

    /**
     * We should keep an instance of created callback
     * for removing purpose, and remove it right after
     * view is destroyed and add it again when view is creating.
     * Because this callback will be removed only when fragment is completely destroyed, not view, by default.
     */
    private var onBackPressedCallback: OnBackPressedCallback? = null

    /**
     * Function that will tell you whether linked [BaseViewModel]
     * was initialized or not
     */
    protected fun hasViewModel() = ::viewModel.isInitialized

    /**
     * Function that will help to register [OnBackPressedCallback]
     * to the inherited [Fragment] and will remove it as soon as [Fragment.onDestroyView]
     * will be called.
     *
     * @Note designed to be called from [Fragment.onViewCreated] to add a
     * callback as soon as [Fragment] has a View
     *
     * @author Ilya Kislenka
     */
    protected fun registerOnBackPressedListener(
        callback: () -> Unit
    ) {
        onBackPressedCallback = addOnBackPressedCallback(callback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(getLayoutID(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = injectViewModel(getVMClass())
        buildSwipeRefreshLayout()
        handleBackButton(getToolbarBackButtonID())
    }

    private fun buildSwipeRefreshLayout() {
        view?.let {
            val refreshLayout = it.findViewById<SwipeRefreshLayout>(getSwipeToRefreshContainerID())
            refreshLayout?.apply {

                val (accentColor, secondaryColor) = getSwipeToRefreshContrastColors()

                setColorSchemeResources(
                    accentColor,
                    secondaryColor,
                    accentColor,
                    secondaryColor
                )
                setProgressBackgroundColorSchemeResource(android.R.color.white)
                setOnRefreshListener {
                    isRefreshing = true
                    onRefreshCalled()
                    isRefreshing = false
                }
            }
        }
    }

    /**
     * Function that after overriding it will notify
     * you when swipe to refresh action was called
     *
     * @author Ilya Kislenka
     */
    open protected fun onRefreshCalled() {}

    override fun onDestroyView() {
        onBackPressedCallback?.remove()
        if (::viewModel.isInitialized)
            viewModel.cleanUp()
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (::viewModel.isInitialized)
            viewModel.cleanUpFinally()
        super.onDestroy()
    }

    //region Loading state handling
    /**
     * Function that will help with handling of the
     * loading state within any inherited [Fragment],
     * as soon as you provided [getLoadingStateCurtainID]
     * and [BaseViewModel.asyncLoading] was called the inherrited
     * [Fragment] will be notified and this method automatically
     * will show/hide provided curtain view/loading indicator
     *
     * @author Ilya Kislenka
     */
    protected fun bindLoadingIndicator() {

        viewModel.subscribeToLoadingState().observe(viewLifecycleOwner, Observer { isLoading ->

            activity?.runOnUiThread {

                if (isLoading)
                    blockUI()
                else
                    unblockUI()
            }
        })
    }

    /**
     * Function that will set [View.VISIBLE] flag to the
     * curtain view by [getLoadingStateCurtainID] id
     *
     * @author Ilya Kislenka
     */
    protected open fun blockUI() {
        view?.findViewById<FrameLayout>(getLoadingStateCurtainID())?.show()
    }

    /**
     * Function that will set [View.GONE] flag to the
     * curtain view by [getLoadingStateCurtainID] id
     *
     * @author Ilya Kislenka
     */
    protected open fun unblockUI() {
        view?.findViewById<FrameLayout>(getLoadingStateCurtainID())?.hide()
    }
    //endregion
}

