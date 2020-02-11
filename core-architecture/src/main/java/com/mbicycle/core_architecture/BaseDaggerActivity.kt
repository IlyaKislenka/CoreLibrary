package com.mbicycle.core_architecture

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * Class that you should inherit and provide your own realization of
 * [ViewModelProvider.Factory] with all view models inside, which should be
 * declared in a Dagger's module
 *
 * @see [BaseDaggerActivityImpl]
 *
 * @author Ilya Kislenka
 */
abstract class BaseDaggerActivity : AppCompatActivity() {
    abstract fun provideViewModelsFactory(): ViewModelProvider.Factory
}
