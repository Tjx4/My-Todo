package co.za.dstv.mytodo.helpers

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import co.za.dstv.mytodo.R

fun showSuccessAlert(context: Context, title: String, message: String, buttonText: String = "Ok", callbackFun:  () -> Unit = {}){
    val ab = setupBasicMessage(title, message, buttonText, "", "", callbackFun, {}, {}, context)
    ab.setIcon(R.drawable.ic_success_dark)
    ab.setCancelable(false)
    showAlertMessage(ab, context)
}

private fun setupBasicMessage(title: String, message: String,
                              positiveButtonText: String?, neutralButtonText: String?, negativeButtonText: String?,
                              positiveCallback: () -> Unit, neutralCallback: () -> Unit, negativeCallback: () -> Unit,
                              context: Context
): AlertDialog.Builder {

    val ab = AlertDialog.Builder(context, R.style.AlertDialogCustom)
    ab.setMessage(message)
        .setTitle(title)
        .setPositiveButton(positiveButtonText) { dialogInterface, i ->
            positiveCallback()
        }

    if (neutralButtonText != null) {
        ab.setNeutralButton(neutralButtonText) { dialogInterface, i ->
            neutralCallback()
        }
    }

    if (negativeButtonText != null) {
        ab.setNegativeButton(negativeButtonText) { dialogInterface, i ->
            negativeCallback()
        }
    }

    return ab
}

private fun showAlertMessage(ab: AlertDialog.Builder, context: Context) {
    val a = ab.create()
    a.requestWindowFeature(Window.FEATURE_NO_TITLE)
    a.show()

    a.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.darkText))
    a.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.darkText))
    a.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(context.resources.getColor(R.color.darkText))
}