package co.za.dstv.mytodo.features.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.za.dstv.mytodo.extensions.FADE_IN_ACTIVITY
import co.za.dstv.mytodo.extensions.navigateToActivity
import co.za.dstv.mytodo.features.dashboard.DashboardActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToActivity(DashboardActivity::class.java, FADE_IN_ACTIVITY)
        finish()
    }
}