package co.za.dstv.mytodo.features.item

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.constants.PAYLOAD_KEY
import co.za.dstv.mytodo.constants.TODO_ITEM_KEY
import co.za.dstv.mytodo.databinding.ActivityItemViewBinding
import co.za.dstv.mytodo.features.base.activities.BaseChildActivity
import co.za.dstv.mytodo.models.TodoItem
import kotlinx.android.synthetic.main.activity_item_view.*

class ItemViewActivity : BaseChildActivity() {
    private lateinit var binding: ActivityItemViewBinding
    lateinit var itemViewViewModel: ItemViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var application = requireNotNull(this).application
        var viewModelFactory = ItemViewViewModelFactory(application)

        itemViewViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ItemViewViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_view)
        binding.itemViewViewModel = itemViewViewModel
        binding.lifecycleOwner = this

        addObservers()

        var item = intent.getBundleExtra(PAYLOAD_KEY).getParcelable<TodoItem>(TODO_ITEM_KEY)
        itemViewViewModel.setTodoItem(item)

        supportActionBar?.title = item?.title
    }

    private fun addObservers() {
        itemViewViewModel.todoItem.observe(this, Observer { isItemSet(it) })
        itemViewViewModel.isComplete.observe(this, Observer { isItemComplete(it) })
        itemViewViewModel.updated.observe(this, Observer { onUpdated(it) })
    }

    private fun isItemSet(todoItem: TodoItem?) {
        itemViewViewModel.checkAndShowItemComplete(todoItem)
    }

    private fun isItemComplete(isComplete: Boolean) {
        btnSetComplete.visibility = View.GONE
        llItemComplete.visibility = View.VISIBLE
    }

    fun onSetCompleteButtonClicked(view: View){
        itemViewViewModel.setItemsComplete()
    }

    fun onUpdated(isUpdated: Boolean){
        Toast.makeText(this, "Item updated", Toast.LENGTH_LONG).show()
    }
}