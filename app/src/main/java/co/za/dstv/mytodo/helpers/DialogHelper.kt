package co.za.dstv.mytodo.helpers

import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.constants.LAYOUT
import co.za.dstv.mytodo.constants.TITLE
import co.za.dstv.mytodo.features.base.activities.BaseActivity
import co.za.dstv.mytodo.features.base.fragments.BaseDialogFragment
import co.za.dstv.mytodo.features.base.fragments.LoadingSpinnerFragment

fun showLoadingDialog(loadingMessage: String, activity: BaseActivity) {
    var loadingSpinnerFragment = LoadingSpinnerFragment.newInstance("")
    loadingSpinnerFragment.isCancelable = false
    showDialogFragment(loadingMessage, R.layout.fragment_loading_spinner, loadingSpinnerFragment, activity)
}

fun hideCurrentLoadingDialog(activity: BaseActivity) {
    activity.activeDialogFragment?.dismiss()
}

fun showDialogFragment(title: String, Layout: Int, newFragmentBaseBase: BaseDialogFragment, activity: BaseActivity) {
    activity?.activeDialogFragment?.dismiss()
    val ft = activity.supportFragmentManager.beginTransaction()
    var newFragment = getFragmentDialog(title, Layout, newFragmentBaseBase)
    newFragment.show(ft, "dialog")
    activity.activeDialogFragment = newFragment
}

private fun getFragmentDialog(title: String, Layout: Int, newFragmentBaseBase: BaseDialogFragment) : BaseDialogFragment {
    val payload = newFragmentBaseBase.arguments
    payload?.putString(TITLE, title)
    payload?.putInt(LAYOUT, Layout)

    newFragmentBaseBase.arguments = payload
    return newFragmentBaseBase
}