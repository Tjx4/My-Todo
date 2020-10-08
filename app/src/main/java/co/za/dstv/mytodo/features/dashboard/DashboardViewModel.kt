package co.za.dstv.mytodo.features.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.za.dstv.mytodo.extensions.isValidDescription
import co.za.dstv.mytodo.extensions.isValidTitle
import co.za.dstv.mytodo.features.base.viewModels.BaseVieModel
import co.za.dstv.mytodo.models.TodoItem
import kotlinx.coroutines.launch
import java.util.*

class DashboardViewModel(application: Application, val dashboardRepository: DashboardRepository) : BaseVieModel(application) {

    private var _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _showContent: MutableLiveData<Boolean> = MutableLiveData()
    val showContent: MutableLiveData<Boolean>
        get() = _showContent

    private val _isItemAdded: MutableLiveData<Boolean> = MutableLiveData()
    val isItemAdded: MutableLiveData<Boolean>
        get() = _isItemAdded

    private val _todoProgress: MutableLiveData<Int> = MutableLiveData()
    var todoProgress: MutableLiveData<Int> = MutableLiveData()
        get() = _todoProgress

    var busyMessage: String = "Please wait..."

    private val _todoItem: MutableLiveData<TodoItem> = MutableLiveData()
    val todoItem: MutableLiveData<TodoItem>
        get() = _todoItem

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
        get() = _errorMessage


    init {
        _todoItem.value = TodoItem()
_todoProgress.value = 70
    }

    fun setDueDate(selectedDate: Date){
        _todoItem.value?.dueDate = selectedDate
    }

    fun checkAndAddItem(){
        if(!checkIsValidTitle(_todoItem.value?.title)){
            _errorMessage.value = "Please enter a valid title"
            return
        }

        if(!checkIsValidDescription(_todoItem.value?.description)){
            _errorMessage.value = "Please enter a minimum of 10 characters for your description"
            return
        }

        ioScope.launch {
            _todoItem.value?.dateCreated = Date()
            addItem()

            uiScope.launch {
                _isItemAdded.value = true
            }
        }
    }

    suspend fun addItem(){
        dashboardRepository.addItemToDb(_todoItem.value!!)
    }

    suspend fun deleteItem(){
        dashboardRepository.deleteItemFromDb(_todoItem.value!!)
    }

    fun checkIsValidTitle(title: String?): Boolean {
        return title?.isValidTitle() ?: false
    }

    fun checkIsValidDescription(description: String?): Boolean {
        return description?.isValidDescription() ?: false
    }
}