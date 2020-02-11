package com.mbicycle.core_architecture

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.*
import javax.inject.Inject

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Class that should take a [ViewModelProvider.Factory]
 * from dagger with all [AndroidViewModel] inside
 *
 * @Note it's a sample of how your implementation should look, don't use this class
 * directly
 */
@SuppressLint("Registered")
class BaseDaggerActivityImpl : BaseDaggerActivity() {

    /**
     * You should declare your Daggers module with the Factory provided
     * and how it should be provided in Daggers component
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun provideViewModelsFactory() = viewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
//        Injector.buildModelsComponent().injectIntoActivity(this)
        super.onCreate(savedInstanceState)
    }
}

/**
 * That should be your own realization of Injector class
 * that will build a components for you. It's really specific
 * on your project implementation
 */
/*
object Injector {

    private lateinit var appComponent: AppComponent

    fun initAppComponent(applicationContext: Context) {

        if (!::appComponent.isInitialized)
            appComponent = DaggerAppComponent
                .builder()
                .applicationModule(ApplicationModule(applicationContext))
                .build()
    }

    internal fun getAppComponent() = appComponent

    fun buildModelsComponent(): ModelsComponent {

        if (!::appComponent.isInitialized)
            throw IllegalStateException("Can't build a models component until app components is initialized")

        return appComponent.buildModelsSubComponent(RetrofitModule(), ViewModelsModule(), UtilsModule(), ModelsModule())
    }
}
*/

