package com.mbicycle.core_architecture

import android.os.Bundle
import android.view.*
import androidx.databinding.*
import androidx.fragment.app.Fragment
import com.mbicycle.core_utils.extensions.handleBackButton

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Class that inherit all the functionality from [BaseFragment]
 * but designed to be used from Data Binding utils
 *
 * @Note designed to work with a BR.viewModel variable inside
 * your's [Fragment]'s xml files
 */
abstract class BaseDataBindingFragment<VM : BaseViewModel> : BaseFragment<VM>() {

    protected lateinit var binding: ViewDataBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        handleBackButton(getToolbarBackButtonID())
    }
}
