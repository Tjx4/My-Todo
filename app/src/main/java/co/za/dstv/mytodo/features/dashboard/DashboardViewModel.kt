package co.za.dstv.mytodo.features.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.extensions.isValidDescription
import co.za.dstv.mytodo.extensions.isValidTitle
import co.za.dstv.mytodo.features.base.viewModels.BaseVieModel
import co.za.dstv.mytodo.helpers.getDateAndTime
import co.za.dstv.mytodo.models.DbOperationResult
import co.za.dstv.mytodo.models.TodoItem
import kotlinx.coroutines.delay
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

    var busyMessage: String = "Please wait..."

    private val _isItemAdded: MutableLiveData<Boolean> = MutableLiveData()
    val isItemAdded: MutableLiveData<Boolean>
        get() = _isItemAdded

    private val _isItemsDeleted: MutableLiveData<Int> = MutableLiveData()
    val isItemDeleted: MutableLiveData<Int>
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
        displayTodoItems()
    }

    fun checkItems(itemCount: Int){
        if(itemCount > 0){
            _isSelectionMode.value = true
        }
        else{
            _showContent.value = true
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

    fun displayTodoItems(){
        ioScope.launch {
            val todoItems = dashboardRepository.getItemsFromDb()
            uiScope.launch {
                var completed = 0
                todoItems.forEach {
                    if(it.complete == true){
                        completed++
                    }
                }

                _todoProgress.value = completed * 100 / todoItems.size

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

        busyMessage = "Adding item please wait..."
        _showLoading.value = true

        ioScope.launch {
            // Todo: remove just for demo purposes
            delay(1000)

            _newItem.value?.dateCreated = getDateAndTime()
            val addItem = addNewTodoListItem()

            uiScope.launch {

                if(addItem.success){
                    _isItemAdded.value = true
                    _newItem.value = TodoItem()
                }
                else{
                    _errorMessage.value = app.getString(R.string.item_add_error)
                }

                _showContent.value = true
            }
        }
    }

    suspend fun addNewTodoListItem(): DbOperationResult {
        return dashboardRepository.addItemToDb(_newItem.value!!)
    }

    fun deleteSelectedItems(){
        val itemsDeleteList = arrayListOf<TodoItem?>()
        _checkList.value?.forEach {
            itemsDeleteList.add(todoItems.value?.get(it))
        }

        busyMessage = "Busy deleting please wait"
        _showLoading.value = true

        ioScope.launch {
            // Todo: remove just for demo purposes
            delay(1500)

            var deleteItems = dashboardRepository.deleteItemsFromDb(itemsDeleteList)

            uiScope.launch {

                if(deleteItems.success){
                    _isItemsDeleted.value = itemsDeleteList.size
                    _checkList.value?.clear()
                }
                else{
                    _errorMessage.value = app.getString(R.string.item_delete_error)
                }

                _showContent.value = true
            }
        }
    }


    fun setPriorityOnSelectedItems(){

    }


    fun checkIsValidTitle(title: String?): Boolean {
        return title?.isValidTitle() ?: false
    }

    fun checkIsValidDescription(description: String?): Boolean {
        return description?.isValidDescription() ?: false
    }
}