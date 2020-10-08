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

    private val _isNoItems: MutableLiveData<Boolean> = MutableLiveData()
    val isNoItems: MutableLiveData<Boolean>
        get() = _isNoItems

    private val _isItemAdded: MutableLiveData<Boolean> = MutableLiveData()
    val isItemAdded: MutableLiveData<Boolean>
        get() = _isItemAdded

    private val _todoProgress: MutableLiveData<Int> = MutableLiveData()
    var todoProgress: MutableLiveData<Int> = MutableLiveData()
        get() = _todoProgress

    var busyMessage: String = "Please wait..."

    private val _newItem: MutableLiveData<TodoItem> = MutableLiveData()
    val newItem: MutableLiveData<TodoItem>
        get() = _newItem

    private var _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    var todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
        get() = _todoItems

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
        get() = _errorMessage

    init {
        _newItem.value = TodoItem()
        setTodoItems()
_todoProgress.value = 70
    }

    fun setDueDate(selectedDate: Date){
        _newItem.value?.dueDate = selectedDate
    }

    fun setTodoItems(){
        ioScope.launch {
            val todoItems = dashboardRepository.getItemsFromDb()
            uiScope.launch {

                if(todoItems.isNullOrEmpty()){
                    _isNoItems.value = true
                }
                else{
                    _todoItems.value = todoItems
                }
            }
        }
    }

    fun checkAndAddItem(){
        if(!checkIsValidTitle(_newItem.value?.title)){
            _errorMessage.value = "Please enter a valid title"
            return
        }

        if(!checkIsValidDescription(_newItem.value?.description)){
            _errorMessage.value = "Please enter a minimum of 10 characters for your description"
            return
        }

        ioScope.launch {
            _newItem.value?.dateCreated = Date()
            addItem()

            uiScope.launch {
                _isItemAdded.value = true
            }
        }
    }

    suspend fun addItem(){
        dashboardRepository.addItemToDb(_newItem.value!!)
    }

    suspend fun deleteItem(){
        dashboardRepository.deleteItemFromDb(_newItem.value!!)
    }

    fun checkIsValidTitle(title: String?): Boolean {
        return title?.isValidTitle() ?: false
    }

    fun checkIsValidDescription(description: String?): Boolean {
        return description?.isValidDescription() ?: false
    }
}