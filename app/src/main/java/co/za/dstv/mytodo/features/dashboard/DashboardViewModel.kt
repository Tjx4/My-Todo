package co.za.dstv.mytodo.features.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.za.dstv.mytodo.extensions.isValidDescription
import co.za.dstv.mytodo.extensions.isValidTitle
import co.za.dstv.mytodo.features.base.viewModels.BaseVieModel
import co.za.dstv.mytodo.helpers.getDateAndTime
import co.za.dstv.mytodo.models.TodoItem
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application, private val dashboardRepository: DashboardRepository) : BaseVieModel(application) {

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

    private var _checkList: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    var checkList: MutableLiveData<ArrayList<Int>> = MutableLiveData()
        get() = _checkList

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
        get() = _errorMessage

    init {
        _newItem.value = TodoItem()
        _checkList.value = ArrayList()
        setTodoItems()

_todoProgress.value = 70
    }

    fun setDueDate(selectedDateTime: String){
        _newItem.value?.dueDate = selectedDateTime
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
            _newItem.value?.dateCreated = getDateAndTime()
            addItem()

            uiScope.launch {
                _isItemAdded.value = true
                _newItem.value = TodoItem()
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