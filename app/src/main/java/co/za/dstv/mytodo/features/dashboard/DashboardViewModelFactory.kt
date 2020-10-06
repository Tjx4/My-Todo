package co.za.dstv.mytodo.features.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class DashboardViewModelFactory( private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DashboardViewModel::class.java)){
            val dashboardRepository = DashboardRepository()
            return DashboardViewModel(application, dashboardRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}