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

    var busyMessage: String = "Please wait..."

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    var errorMessage: MutableLiveData<String> = MutableLiveData()
        get() = _errorMessage

    private val _showContent: MutableLiveData<Boolean> = MutableLiveData()
    val showContent: MutableLiveData<Boolean>
        get() = _showContent

    private var _todoItem: MutableLiveData<TodoItem> = MutableLiveData()
    var todoItem: MutableLiveData<TodoItem> = MutableLiveData()
        get() = _todoItem

    private var _updated: MutableLiveData<Boolean> = MutableLiveData()
    val updated: MutableLiveData<Boolean>
        get() = _updated


    fun setTodoItem(todoItem: TodoItem?){
        _todoItem.value = todoItem
    }

    fun setItemsComplete(){
        val itemsCompleList = arrayListOf<TodoItem?>(_todoItem.value)

        ioScope.launch {
            var completeItems = itemViewRepository.setItemAsComplete(itemsCompleList)

            uiScope.launch {
                if(completeItems.success){
                    _updated.value = true
                }
                else{
                    _errorMessage.value = app.getString(R.string.priority_error_message)
                }

                _showContent.value = true
            }
        }

    }
}