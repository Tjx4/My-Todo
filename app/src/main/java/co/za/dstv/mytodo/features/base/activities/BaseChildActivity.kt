package co.za.dstv.mytodo.features.base.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import co.za.dstv.mytodo.extensions.SLIDE_OUT_ACTIVITY

abstract class BaseChildActivity : BaseActivity() {

    protected var childActionBar: ActionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        if (isNewActivity) {
            //Todo: do something on initial entry o delete method
            // overridePendingTransition(slideOutActivity.inAnimation, slideOutActivity.outAnimation)
        }

    }

    protected fun setChildActionbarActivityDependencies(actionBar: ActionBar?) {
        childActionBar = actionBar
        childActionBar?.setDisplayUseLogoEnabled(false)
        childActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        overridePendingTransition(SLIDE_OUT_ACTIVITY.inAnimation, SLIDE_OUT_ACTIVITY.outAnimation)
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Todo: fix out transition
        overridePendingTransition(SLIDE_OUT_ACTIVITY.inAnimation, SLIDE_OUT_ACTIVITY.outAnimation)
    }

}
