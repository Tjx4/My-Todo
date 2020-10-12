package co.za.dstv.mytodo.features.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.extensions.isValidDescription
import co.za.dstv.mytodo.extensions.isValidTitle
import co.za.dstv.mytodo.features.base.viewModels.BaseVieModel
import co.za.dstv.mytodo.helpers.getCurrentDateAndTime
import co.za.dstv.mytodo.models.DbOperation
import co.za.dstv.mytodo.models.TodoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application, private val dashboardRepository: DashboardRepository) : BaseVieModel(application) {

    private var _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _isViewMode: MutableLiveData<Boolean> = MutableLiveData()
    val isViewMode: MutableLiveData<Boolean>
        get() = _isViewMode

    private val _isNoItems: MutableLiveData<Boolean> = MutableLiveData()
    val isNoItems: MutableLiveData<Boolean>
        get() = _isNoItems

    var busyMessage: String = "Please wait..."

    private val _isItemAdded: MutableLiveData<Boolean> = MutableLiveData()
    val isItemAdded: MutableLiveData<Boolean>
        get() = _isItemAdded

    private val _isItemsDeleted: MutableLiveData<Int> = MutableLiveData()
    val itemsDeleted: MutableLiveData<Int>
        get() = _isItemsDeleted

    private val _todoProgress: MutableLiveData<Int> = MutableLiveData()
    var todoProgress: MutableLiveData<Int> = MutableLiveData()
        get() = _todoProgress

    private val _isSelectionMode: MutableLiveData<Boolean> = MutableLiveData()
    val isSelectionMode: MutableLiveData<Boolean>
        get() = _isSelectionMode

    private val _newItem: MutableLiveData<TodoItem> = MutableLiveData()
    val newItem: MutableLiveData<TodoItem>
        get() = _newItem

    private var _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    var todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
        get() = _todoItems

    private var _checkList: MutableLiveData<MutableList<Int>> = MutableLiveData()
    val checkList: MutableLiveData<MutableList<Int>>
        get() = _checkList

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
        get() = _errorMessage

    init {
        _newItem.value = TodoItem()
        _checkList.value = ArrayList()
    }

    fun checkItems(itemCount: Int){
        if(itemCount > 0){
            _isSelectionMode.value = true
        }
        else{
            _isViewMode.value = true
        }
    }

    fun addNewTodoListItem(itemIndex: Int){
        _checkList.value?.add(itemIndex)
        _checkList.value = _checkList.value
    }

    fun removeItemFroCheckList(itemIndex: Int){
        _checkList.value?.remove(itemIndex)
        _checkList.value = _checkList.value
    }

    fun setDueDate(selectedDateTime: String){
        _newItem.value?.dueDate = selectedDateTime
    }

    fun getProgress(todoItems: List<TodoItem>): Int{
        var completed = 0
        todoItems.forEach {
            if(it.complete == true){
                completed++
            }
        }

        return  if(todoItems.isNotEmpty()) completed * 100 / todoItems.size else 0
    }

    fun getItemsAndSetProgress(){
        ioScope.launch {
            val todoItems = getTodoItems()

            uiScope.launch {
                if(todoItems.isNullOrEmpty()){
                    _isNoItems.value = true
                }
                else{
                    _todoItems.value = todoItems
                }

                _todoProgress.value = getProgress(todoItems)
            }
        }
    }

    suspend fun getTodoItems() = dashboardRepository.getItemsFromDb()

    fun checkAndAddItem(){
        if(!checkIsValidTitle(_newItem.value?.title)){
            _errorMessage.value = "Please enter a valid title"
            return
        }

        if(!checkIsValidDescription(_newItem.value?.description)){
            _errorMessage.value = "Please enter a minimum of 10 characters for your description"
            return
        }

        busyMessage = "Adding item please wait..."
        _showLoading.value = true

        ioScope.launch {
            delay(1000)

            _newItem.value?.dateCreated = getCurrentDateAndTime()
            val addItem = dashboardRepository.addItemToDb(newItem.value!!)

            uiScope.launch {

                if(addItem.success){
                    _isItemAdded.value = true
                    _newItem.value = TodoItem()
                }
                else{
                    _errorMessage.value = app.getString(R.string.item_add_error)
                }

                _isViewMode.value = true
            }
        }
    }

    fun checkAndDeleteItems(){
        val itemsDeleteList = arrayListOf<TodoItem?>()
        _checkList.value?.forEach {
            itemsDeleteList.add(todoItems.value?.get(it))
        }

        if(itemsDeleteList.isNullOrEmpty()){
            return
        }

        ioScope.launch {
            var deleteItems = dashboardRepository.deleteItemsFromDb(itemsDeleteList)

            uiScope.launch {

                if(deleteItems.success){
                    _isItemsDeleted.value = itemsDeleteList.size
                    _checkList.value?.clear()
                }
                else{
                    _errorMessage.value = app.getString(R.string.item_delete_error)
                }

                _isViewMode.value = true
            }
        }
    }

    fun setPriorityOnSelectedItems(){
        val itemsPriorityList = arrayListOf<TodoItem?>()
        _checkList.value?.forEach {
            itemsPriorityList.add(todoItems.value?.get(it))
        }

        ioScope.launch {
            var priorityItems = dashboardRepository.toggleItemPrioriy(itemsPriorityList)

            uiScope.launch {
                if(priorityItems.success){
                    _checkList.value?.clear()
                    getItemsAndSetProgress()
                }
                else{
                    _errorMessage.value = app.getString(R.string.priority_error_message)
                }

                _isViewMode.value = true
            }
        }
    }

    fun checkIsValidTitle(title: String?): Boolean {
        return title?.isValidTitle() ?: false
    }

    fun checkIsValidDescription(description: String?): Boolean {
        return description?.isValidDescription() ?: false
    }
}