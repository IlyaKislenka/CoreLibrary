package com.mbicycle.core_networking

import android.content.Context
import androidx.annotation.WorkerThread
import com.mbicycle.core_networking.retrofit_sample.APIService

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * It's a sample implementation of [BaseNetworkingModel]
 */
class ModelImpl(
    appContext: Context,
    private val apiService: APIService
) : BaseNetworkingModel(appContext) {

    /**
     * Those types of function designed to be called from
     * BaseViewModel.asyncLoading inside a coroutineScope
     */
    @WorkerThread
    fun getSomething(): Any {
        /* val response = apiService.whoAmI().execute()
         return checkRetrofitResponseBodyToError(response)*/
        return Any()
    }

    /**
     * Just a mocks, please use a string references specific to your project
     */
    override fun emptyResponseBodyStringReference() = R.string.app_name

    override fun serverIsUnavailableStringReference() = R.string.app_name
}
