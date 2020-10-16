package co.za.dstv.mytodo.features.item

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.za.dstv.mytodo.features.database.TodoDb
import java.lang.IllegalArgumentException

class ItemViewViewModelFactory( private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ItemViewViewModel::class.java)){
            var database = TodoDb.getInstance(application)
            val itemViewRepository = ItemViewRepository(database)
            return ItemViewViewModel(application, itemViewRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}