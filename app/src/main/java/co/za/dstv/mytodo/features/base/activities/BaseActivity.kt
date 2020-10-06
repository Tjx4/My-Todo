package co.za.dstv.mytodo.features.base.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import co.za.dstv.mytodo.extensions.initTransitions
import co.za.dstv.mytodo.features.base.fragments.BaseDialogFragment

abstract class BaseActivity : AppCompatActivity() {
    var activeDialogFragment: BaseDialogFragment? = null
    var isNewActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTransitions()
        isNewActivity = true
        supportActionBar?.elevation = 0f
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
           // R.id.action_help -> { showShortToast("Help...", this) }
        }
        return super.onOptionsItemSelected(item)
    }
}