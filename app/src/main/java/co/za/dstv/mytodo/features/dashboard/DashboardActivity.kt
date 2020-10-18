package co.za.dstv.mytodo.features.dashboard

import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.adapters.TodoItemAdapter
import co.za.dstv.mytodo.databinding.ActivityDashboardBinding
import co.za.dstv.mytodo.extensions.blinkView
import co.za.dstv.mytodo.extensions.runWhenReady
import co.za.dstv.mytodo.features.base.activities.BaseParentActivity
import co.za.dstv.mytodo.features.dashboard.fragments.AddItemFragment
import co.za.dstv.mytodo.helpers.hideCurrentLoadingDialog
import co.za.dstv.mytodo.helpers.showDialogFragment
import co.za.dstv.mytodo.helpers.showLoadingDialog
import co.za.dstv.mytodo.helpers.showSuccessAlert
import co.za.dstv.mytodo.models.TodoItem
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseParentActivity(), TodoItemAdapter.TodoItemClickListener {
    private lateinit var binding: ActivityDashboardBinding
    lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var addItemFragment: AddItemFragment
    private lateinit var todRvLayoutManager: LinearLayoutManager
    private var todoItemAdapter: TodoItemAdapter? = null
    private var deleteMenuItem: MenuItem? = null
    private var priorityMenuItem: MenuItem? = null
    private var exitMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var application = requireNotNull(this).application
        var viewModelFactory = DashboardViewModelFactory(application)

        dashboardViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.dashboardViewModel = dashboardViewModel
        binding.lifecycleOwner = this

        addObservers()

        iniViews()

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_burger_menu)
    }

    override fun onRestart() {
        super.onRestart()
        if(todoItemAdapter != null){
            val position = todoItemAdapter!!.selectedPos
            dashboardViewModel.setTodoItems()
            rvItems.runWhenReady {
                rvItems.scrollToPosition(position)
            }
        }
    }

    private fun addObservers() {
        dashboardViewModel.showLoading.observe(this, Observer { onShowLoading(it) })
        dashboardViewModel.isViewMode.observe(this, Observer { setViewMode(it) })
        dashboardViewModel.isSelectionMode.observe(this, Observer { setSelectionMode(it) })
        dashboardViewModel.errorMessage.observe(this, Observer { onError(it) })
        dashboardViewModel.currentItem.observe(this, Observer { onItemAdded(it) })
        dashboardViewModel.itemsDeleted.observe(this, Observer { onItemsDeleted(it) })
        dashboardViewModel.priorityItems.observe(this, Observer { onPriorityItemsSet(it) })
        dashboardViewModel.isNoItems.observe(this, Observer { onNoItems(it) })
        dashboardViewModel.todoItems.observe(this, Observer { onTodoItemsSet(it) })
        dashboardViewModel.updatedItems.observe(this, Observer { onTodoItemsUpdated(it) })
        dashboardViewModel.checkList.observe(this, Observer { onCheckListUpdated(it) })
    }

    private fun iniViews() {
        todRvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems?.layoutManager = todRvLayoutManager
    }

    private fun onShowLoading(isBusy: Boolean) {
        showLoadingDialog(dashboardViewModel.busyMessage, this)
    }

    fun setSelectionMode(isSelectionMode: Boolean) {
        supportActionBar?.setDisplayShowHomeEnabled(false)
        deleteMenuItem?.isVisible = true
        priorityMenuItem?.isVisible = true
        exitMenuItem?.isVisible = false
    }

    private fun setViewMode(isViewMode: Boolean) {
        hideCurrentLoadingDialog(this)
        deleteMenuItem?.isVisible = false
        priorityMenuItem?.isVisible = false
        exitMenuItem?.isVisible = true

        supportActionBar?.title = getString(R.string.todo_list)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_burger_menu)
    }

    private fun onCheckListUpdated(items: MutableList<Int>) {
        val itemCount = items.size
        supportActionBar?.title = if (itemCount == 1) "$itemCount item" else "$itemCount items"
        dashboardViewModel.toggleViewSelectMode(items.size)
    }

    private fun onTodoItemsSet(todoItems: List<TodoItem>) {
        todRvLayoutManager.initialPrefetchItemCount = todoItems.size
        todoItemAdapter = TodoItemAdapter(this, todoItems)
        rvItems.adapter = todoItemAdapter

        rvItems.visibility = View.VISIBLE
        llLoading.visibility = View.GONE
        tvNoItems.visibility = View.GONE
    }

    private fun onTodoItemsUpdated(todoItems: List<TodoItem>) {
        todRvLayoutManager.initialPrefetchItemCount = todoItems.size
        todoItemAdapter?.updateItems(todoItems)
    }

    private fun onNoItems(isNoItems: Boolean) {
        llLoading.visibility = View.GONE
        rvItems.visibility = View.GONE
        tvNoItems.visibility = View.VISIBLE
    }

    override fun onServiceCategoryClick(view: View, position: Int) {

    }

    private fun onItemAdded(todoItems: TodoItem) {
        setViewMode(true)
        dashboardViewModel.checkList.value?.clear()
        showSuccessAlert(this, getString(R.string.done), getString(R.string.item_added), getString(R.string.ok)) {
            dashboardViewModel?.setTodoItems()
            addItemFragment.dismiss()
        }
    }

    private fun onItemsDeleted(deletedItems: List<TodoItem>) {
        resetItemList()
        Toast.makeText(this, dashboardViewModel.itemDeleteMessage, Toast.LENGTH_LONG).show()
    }

    private fun onPriorityItemsSet(priorityItems: List<TodoItem>) {
        resetItemList()
    }

    private fun onError(errorMessage: String) {
        addItemFragment.errorContainer.blinkView(0.6f, 1.0f, 50, 2, Animation.ABSOLUTE, 0, {
            addItemFragment.errorContainer.visibility = View.VISIBLE
        })

        addItemFragment.contentSv.smoothScrollTo(0,0)
    }

    fun resetItemList(){
        dashboardViewModel.updateTodoItems()
    }

    fun onAddButtonClicked(view: View){
        addItemFragment = AddItemFragment.newInstance()
        addItemFragment.isCancelable = true
        showDialogFragment(getString(R.string.add_item), R.layout.fragment_add_item, addItemFragment,this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        deleteMenuItem = menu.findItem(R.id.action_delette_item)
        priorityMenuItem = menu.findItem(R.id.action_priority)
        exitMenuItem = menu.findItem(R.id.action_exit)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delette_item -> {
                setViewMode(true)
                dashboardViewModel.deleteTodoItems()
            }
            R.id.action_priority -> {
                setViewMode(true)
                dashboardViewModel.setPriorityOnSelectedItems()
            }
            R.id.action_exit -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val isEmptyCheckList = dashboardViewModel.checkList.value?.isNullOrEmpty() ?: true
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isEmptyCheckList) {
                moveTaskToBack(true)
                return super.onKeyDown(keyCode, event)
            }
            else{
                dashboardViewModel.checkList.value?.clear()
                setViewMode(true)
                resetItemList()
            }
        }
        return true
    }

}