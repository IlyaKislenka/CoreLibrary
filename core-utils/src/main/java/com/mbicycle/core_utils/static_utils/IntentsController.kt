package com.mbicycle.core_utils.static_utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * A singleton class that contains util methods operating with [Intent]
 */
object IntentsController {

    /**
     * Function to open a new activity on a top of activities stack with
     * clearing all activities that already been added to the stack.
     *
     * @param context should be an Activity instance
     * @param toActivityClass is a desired activity class to navigate to
     *
     * @author Ilya Kislenka
     */
    fun startActivityWithANewStack(
        context: Context,
        toActivityClass: Class<out Activity>
    ) {

        val intent = Intent(context, toActivityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    /**
     * Function to open a new activity on a top of activities stack
     *
     * @param context should be an Activity instance
     * @param toActivityClass it's a desired activity class to navigate to
     * @param intentAppendix it's a lambda where you will receive a created [Intent] to
     * have an ability to set your own flags to the [Intent]
     *
     * @author Ilya Kislenka
     */
    fun startActivityAddingToBackStack(
        context: Context,
        toActivityClass: Class<out Activity>,
        intentAppendix: (Intent) -> Unit
    ) {
        context.startActivity(Intent(context, toActivityClass).apply {
            intentAppendix.invoke(this)
        })
    }

    /**
     * Function to open a standard android files selector for PDF files only
     *
     * @param host it's a fragment from behalf the action is performed.
     * @Note You should override [Activity.onActivityResult] inside your `host` to handle
     * the result (picked file, or close picker action)
     * @param resultCode it's a constant [Int], that will be used inside your @param `host`
     * to determine which activity has send a result
     *
     * @author Ilya Kislenka
     */
    fun browseForAPDFFile(host: Fragment,
                          resultCode: Int = 1) {

        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }

        host.startActivityForResult(intent, resultCode)
    }

    /**
     * Function to open a standard android files selector for image files only
     *
     * @param host it's a fragment from behalf the action is performed.
     * @Note You should override [Activity.onActivityResult] inside your `host` to handle
     * the result (picked file, or close picker action)
     * @param resultCode it's a constant [Int], that will be used inside your @param `host`
     * to determine which activity has send a result
     *
     * @author Ilya Kislenka
     */
    fun pickImageFromGallery(
        host: Fragment,
        requestCode: Int = 1
    ) {
        host.startActivityForResult(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
            requestCode
        )
    }

    /**
     * Function to open an external android browser with a @param `link` within
     *
     * @param context should be an Activity instance
     * @param link it's a web site link to open within a browser
     *
     * @author Ilya Kislenka
     */
    fun openAWebLink(
        context: Context,
        link: String
    ) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}
