package com.mbicycle.core_utils.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

/**
 * This is a wrap on a plain [AlertDialog] with an ability to
 * setup only the required properties in a particular case
 *
 * @author Ilya Kislenka
 */
class GenericAlertDialogFragment : DialogFragment() {

    private var title: String = ""
    private var message: String = ""
    private var positiveButtonMessage: String = ""
    private var positiveListener: (() -> Unit)? = null
    private var negativeButtonMessage: String = ""
    private var negativeListener: (() -> Unit)? = null
    private var neutralButtonMessage: String = ""
    private var neutralListener: (() -> Unit)? = null

    companion object {

        fun build(
            title: String,
            message: String,
            positiveButtonMessage: String,
            positiveListener: (() -> Unit)?,
            negativeButtonMessage: String,
            negativeListener: (() -> Unit)?,
            neutralButtonMessage: String,
            neutralListener: (() -> Unit)?,
            cancelable: Boolean = true
        ) = GenericAlertDialogFragment().apply {
            this.title = title
            this.message = message
            this.positiveButtonMessage = positiveButtonMessage
            this.positiveListener = positiveListener
            this.negativeButtonMessage = negativeButtonMessage
            this.negativeListener = negativeListener
            this.neutralButtonMessage = neutralButtonMessage
            this.neutralListener = neutralListener
            isCancelable = cancelable
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        if (positiveButtonMessage.isNotEmpty())
            alertDialogBuilder.setPositiveButton(positiveButtonMessage) { dialog, _ ->
                dialog?.dismiss()
                positiveListener?.invoke()
            }

        if (negativeButtonMessage.isNotEmpty()) {
            alertDialogBuilder.setNegativeButton(negativeButtonMessage) { dialog, _ ->
                dialog?.dismiss()
                if (negativeListener != null)
                    negativeListener?.invoke()
            }
        }

        if (neutralButtonMessage.isNotEmpty()) {
            alertDialogBuilder.setNeutralButton(neutralButtonMessage) { dialog, _ ->
                dialog?.dismiss()
                if (neutralListener != null)
                    neutralListener?.invoke()
            }
        }

        return alertDialogBuilder.create()
    }
}
