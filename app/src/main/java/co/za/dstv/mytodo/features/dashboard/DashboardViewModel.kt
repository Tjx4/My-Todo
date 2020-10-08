package co.za.dstv.mytodo.features.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.za.dstv.mytodo.features.base.viewModels.BaseVieModel
import co.za.dstv.mytodo.models.TodoItem
import java.util.*

class DashboardViewModel(application: Application, val dashboardRepository: DashboardRepository) : BaseVieModel(application) {

    private var _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _showContent: MutableLiveData<Boolean> = MutableLiveData()
    val showContent: MutableLiveData<Boolean>
        get() = _showContent

    private val _todoProgress: MutableLiveData<Int> = MutableLiveData()
    var todoProgress: MutableLiveData<Int> = MutableLiveData()
        get() = _todoProgress

    private val _todoProgressPcnt: MutableLiveData<String> = MutableLiveData()
    val todoProgressPcnt: MutableLiveData<String>
        get() = _todoProgressPcnt

    var busyMessage: String = "Please wait..."

    private val _todoItem: MutableLiveData<TodoItem> = MutableLiveData()
    val todoItem: MutableLiveData<TodoItem>
        get() = _todoItem


    init {
_todoProgress.value = 70
_todoProgressPcnt.value = "${_todoProgress.value}%"
    }


    fun setDateTime(selectedDate: Date){
        _todoItem.value?.dueDate = selectedDate
    }

    fun checkAndAddItem(){


    }

    fun addItem(todoItem: TodoItem){

    }
}