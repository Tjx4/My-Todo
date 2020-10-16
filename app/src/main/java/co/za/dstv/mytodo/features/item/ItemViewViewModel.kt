package co.za.dstv.mytodo.features.item

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.features.base.viewModels.BaseVieModel
import co.za.dstv.mytodo.models.TodoItem
import kotlinx.coroutines.launch

class ItemViewViewModel(application: Application, private val itemViewRepository: ItemViewRepository) : BaseVieModel(application) {

    private var _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    var busyMessage: String = app.getString(R.string.please_wait)

    private var _isComplete: MutableLiveData<Boolean> = MutableLiveData()
    val isComplete: MutableLiveData<Boolean>
    get() = _isComplete

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
        get() = _errorMessage

    private var _todoItem: MutableLiveData<TodoItem> = MutableLiveData()
    var todoItem: MutableLiveData<TodoItem> = MutableLiveData()
        get() = _todoItem

    private var _updated: MutableLiveData<Boolean> = MutableLiveData()
    val updated: MutableLiveData<Boolean>
        get() = _updated

    init {

    }

    fun checkAndShowItemComplete(todoItem: TodoItem?){
        val isComplete = _todoItem.value?.complete ?: false
        if(isComplete) {
            _isComplete.value = true
        }
    }

    fun setTodoItem(todoItem: TodoItem?){
        _todoItem.value = todoItem
    }

    fun setItemsComplete(){
        busyMessage = app.getString(R.string.updating_item)
        _showLoading.value = true

        val itemsCompleList = arrayListOf<TodoItem?>(_todoItem.value)

        ioScope.launch {
            var completeItems = itemViewRepository.setItemAsComplete(itemsCompleList)

            uiScope.launch {
                if(completeItems.success){
                    _updated.value = true
                    _isComplete.value = true
                }
                else{
                    _errorMessage.value = app.getString(R.string.priority_error_message)
                }

            }
        }

    }
}