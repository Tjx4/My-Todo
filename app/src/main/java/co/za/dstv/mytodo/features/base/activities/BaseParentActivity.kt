package co.za.dstv.mytodo.features.base.activities

import androidx.appcompat.app.ActionBar
import co.za.dstv.mytodo.extensions.SLIDE_OUT_ACTIVITY

abstract class BaseParentActivity : BaseActivity() {

    var parentActionBar: ActionBar? = null

    override fun onResume() {
        super.onResume()

        if (isNewActivity)
            return

        overridePendingTransition(SLIDE_OUT_ACTIVITY.inAnimation, SLIDE_OUT_ACTIVITY.outAnimation)
    }

    override fun onPause() {
        super.onPause()
        isNewActivity = false
    }

    protected fun setParentActionbarActivityDependencies(actionBar : ActionBar?) {
        parentActionBar = actionBar
        //parentActionBar.setDisplayHomeAsUpEnabled(false);
        //parentActionBar.setDefaultDisplayHomeAsUpEnabled(true);
    }
}