package co.za.dstv.mytodo.features.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.za.dstv.mytodo.features.database.TodoDb
import java.lang.IllegalArgumentException

class DashboardViewModelFactory( private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DashboardViewModel::class.java)){
            var database = TodoDb.getInstance(application)
            val dashboardRepository = DashboardRepository(database)
            return DashboardViewModel(application, dashboardRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}