package co.za.dstv.mytodo.extensions

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.constants.ACTIVITY_TRANSITION
import co.za.dstv.mytodo.constants.PAYLOAD_KEY
import co.za.dstv.mytodo.models.Transition

val SLIDE_IN_ACTIVITY = getTransitionAnimation(R.anim.slide_right, R.anim.no_transition)
val SLIDE_OUT_ACTIVITY =  getTransitionAnimation(R.anim.no_transition, R.anim.slide_left)
val FADE_IN_ACTIVITY = getTransitionAnimation(R.anim.fade_in, R.anim.no_transition)
val FADE_OUT_ACTIVITY = getTransitionAnimation(R.anim.no_transition, R.anim.fade_out)
val TRAIL_TO = getTransitionAnimation(R.anim.trail_out, R.anim.trail_in)
val TRAIL_FROM = getTransitionAnimation(R.anim.trail_in2, R.anim.trail_out2)

const val DEFAULT_STATUS_BAR_ALPHA = 112
private const val FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view


fun AppCompatActivity.navigateToActivity(activity: Class<*>, transitionAnimation: Transition, payload: Bundle? = null) {
    goToActivity(activity, transitionAnimation, payload)
}

private fun AppCompatActivity.goToActivity(activity: Class<*>, transitionAnimation: Transition, payload: Bundle?) {
    val intent = Intent(this, activity)

    val fullPayload = payload ?: Bundle()
    fullPayload.putIntArray(ACTIVITY_TRANSITION, intArrayOf(transitionAnimation.inAnimation, transitionAnimation.outAnimation))

    intent.putExtra(PAYLOAD_KEY, fullPayload)
    startActivity(intent)
}

private fun getTransitionAnimation(inAnimation: Int, outAnimation: Int): Transition {
    val transitionProvider = Transition()
    transitionProvider.inAnimation = inAnimation
    transitionProvider.outAnimation = outAnimation
    return transitionProvider
}

fun Activity.setTranslucentStatusBar(@IntRange(from = 0, to = 255) statusBarAlpha: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
    setTransparent(this)
    addTranslucentView(this, statusBarAlpha)
}

private fun setTransparent(activity: Activity) {
    transparentStatusBar(activity)
    setRootView(activity)
}

private fun setRootView(activity: Activity) {
    val parent = activity.findViewById<View>(android.R.id.content) as ViewGroup
    var i = 0
    val count = parent.childCount
    while (i < count) {
        val childView = parent.getChildAt(i)
        if (childView is ViewGroup) {
            childView.setFitsSystemWindows(true)
            childView.clipToPadding = true
        }
        i++
    }
}


private fun addTranslucentView(
    activity: Activity,
    @IntRange(from = 0, to = 255) statusBarAlpha: Int
) {
    val contentView = activity.findViewById<View>(android.R.id.content) as ViewGroup
    val fakeTranslucentView = contentView.findViewById<View>(FAKE_TRANSLUCENT_VIEW_ID)
    if (fakeTranslucentView != null) {
        if (fakeTranslucentView.visibility == View.GONE) {
            fakeTranslucentView.visibility = View.VISIBLE
        }
        fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0))
    } else {
        contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha))
    }
}

@Deprecated("")
fun setTranslucentDiff(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setRootView(activity)
    }
}

@TargetApi(Build.VERSION_CODES.KITKAT)
private fun transparentStatusBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        activity.window.statusBarColor = Color.TRANSPARENT
    } else {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}


private fun createTranslucentStatusBarView(activity: Activity, alpha: Int): View? {
    val statusBarView = View(activity)
    val params =
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity))
    statusBarView.layoutParams = params
    statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
    statusBarView.id = FAKE_TRANSLUCENT_VIEW_ID
    return statusBarView
}

private fun getStatusBarHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return context.resources.getDimensionPixelSize(resourceId)
}

fun AppCompatActivity.initTransitions() {
    try {
        val activityTransition = this.intent.getBundleExtra(PAYLOAD_KEY).getIntArray(ACTIVITY_TRANSITION)
        this.overridePendingTransition(activityTransition!![0], activityTransition[1])
    }
    catch (e: Exception) {
        Log.e("AT", "$e")
    }
}
