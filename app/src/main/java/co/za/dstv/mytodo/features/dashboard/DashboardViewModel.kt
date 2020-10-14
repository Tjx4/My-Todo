package co.za.dstv.mytodo.features.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.extensions.isValidDescription
import co.za.dstv.mytodo.extensions.isValidTitle
import co.za.dstv.mytodo.features.base.viewModels.BaseVieModel
import co.za.dstv.mytodo.helpers.getCurrentDateAndTime
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

    var busyMessage: String = app.getString(R.string.please_wait)

    private val _itemsDeleted: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val itemsDeleted: MutableLiveData<List<TodoItem>>
        get() = _itemsDeleted

    private val _priorityItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val priorityItems: MutableLiveData<List<TodoItem>>
        get() = _priorityItems

    lateinit var itemDeleteMessage: String

    private val _todoProgress: MutableLiveData<Int> = MutableLiveData()
    var todoProgress: MutableLiveData<Int> = MutableLiveData()
        get() = _todoProgress

    private val _isSelectionMode: MutableLiveData<Boolean> = MutableLiveData()
    val isSelectionMode: MutableLiveData<Boolean>
        get() = _isSelectionMode

    private val _newItem: MutableLiveData<TodoItem> = MutableLiveData()
    val newItem: MutableLiveData<TodoItem>
        get() = _newItem

    private val _currentItem: MutableLiveData<TodoItem> = MutableLiveData()
    val currentItem: MutableLiveData<TodoItem>
        get() = _currentItem

    private var _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    var todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
        get() = _todoItems

    private var _checkList: MutableLiveData<MutableList<Int>> = MutableLiveData()
    val checkList: MutableLiveData<MutableList<Int>>
        get() = _checkList

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: MutableLiveData<String>
        get() = _errorMessage

    init {
        _todoProgress.value = 0
        _newItem.value = TodoItem()
        _checkList.value = ArrayList()
        setTodoItems()
    }

    fun toggleViewSelectMode(selectedItems: Int){
        when(selectedItems){
           0 -> _isViewMode.value = true
           1 -> _isSelectionMode.value = true
        }
    }

    fun setSelectionMode(){
        _isSelectionMode.value = true
    }

    fun removeItemFroCheckList(itemIndex: Int){
        _checkList.value?.remove(itemIndex)
        _checkList.value = _checkList.value
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

                _todoProgress.value = getProgress(todoItems)
            }
        }
    }

    fun checkAndAddItem(){
        if(!checkIsValidTitle(_newItem.value?.title)){
            _errorMessage.value = app.getString(R.string.title_error)
            return
        }

        if(!checkIsValidDescription(_newItem.value?.description)){
            _errorMessage.value = app.getString(R.string.description_error)
            return
        }

        busyMessage = app.getString(R.string.adding_items)
        _showLoading.value = true

        ioScope.launch {
            delay(1000)

            val newItem = _newItem.value!!
            newItem?.dateCreated = getCurrentDateAndTime()

            val addItem = dashboardRepository.addItemToDb(newItem)

            uiScope.launch {
                if(addItem.success){
                    ((_todoItems.value as ArrayList)).add(0, newItem)
                    _currentItem.value = newItem
                    _newItem.value = TodoItem()
                }
                else{
                    _errorMessage.value = app.getString(R.string.item_add_error)
                }
            }
        }
    }

    fun checkAndDeleteItems(){
        if(_checkList.value.isNullOrEmpty()){
            return
        }

        val itemsDeleteList = ArrayList<TodoItem?>()
        _checkList.value?.forEach {
            val item = _todoItems.value?.get(it)
            itemsDeleteList.add(item)
        }

        itemsDeleteList?.forEach {item ->
            ((_todoItems.value) as ArrayList).remove(item)
        }

        ioScope.launch {
            var deleteItems = dashboardRepository.deleteItemsFromDb(itemsDeleteList)

            uiScope.launch {

                if(deleteItems.success){
                    val deletedItems = _checkList.value?.count()
                    itemDeleteMessage = if(deletedItems == 1) app.getString(R.string.item_deleted) else "$deletedItems items deleted"
                    _itemsDeleted.value = itemsDeleteList as List<TodoItem>
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
            _todoItems.value?.get(it)?.let { it.priority = !it.priority }
            val item = _todoItems.value?.get(it)
            itemsPriorityList.add(item)
        }

        ioScope.launch {
            var priorityItems = dashboardRepository.toggleItemPrioriy(itemsPriorityList)

            uiScope.launch {

                if(priorityItems.success){
                    _priorityItems.value = itemsPriorityList as List<TodoItem>
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