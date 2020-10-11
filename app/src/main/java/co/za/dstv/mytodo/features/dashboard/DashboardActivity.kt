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

    override fun onResume() {
        super.onResume()
        dashboardViewModel.displayTodoItems()
    }

    private fun addObservers() {
        dashboardViewModel.showLoading.observe(this, Observer { onShowLoading(it) })
        dashboardViewModel.showContent.observe(this, Observer { onShowContent(it) })
        dashboardViewModel.isSelectionMode.observe(this, Observer { setSelectionMode(it) })
        dashboardViewModel.errorMessage.observe(this, Observer { onError(it) })
        dashboardViewModel.isItemAdded.observe(this, Observer { onItemAdded(it) })
        dashboardViewModel.isItemDeleted.observe(this, Observer { onItemDeleted(it) })
        dashboardViewModel.isNoItems.observe(this, Observer { onNoItems(it) })
        dashboardViewModel.todoItems.observe(this, Observer { onTodoItemsSet(it) })
        dashboardViewModel.checkList.observe(this, Observer { onCheckListUpdated(it) })
    }

    private fun iniViews() {
        todRvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems?.layoutManager = todRvLayoutManager
    }

    private fun onNoItems(isNoItems: Boolean) {
        rvItems.visibility = View.GONE
        tvNoItems.visibility = View.VISIBLE
    }

    private fun onShowLoading(isBusy: Boolean) {
        clCParent.visibility = View.INVISIBLE
        showLoadingDialog(dashboardViewModel.busyMessage, this)
    }

    fun setSelectionMode(isSelectionMode: Boolean) {
        val itemCount = dashboardViewModel.checkList.value?.size
        supportActionBar?.title = if (itemCount == 1) "$itemCount item" else "$itemCount items"
        deleteMenuItem?.isVisible = true
        priorityMenuItem?.isVisible = true
        exitMenuItem?.isVisible = false
    }

    private fun onShowContent(showContent: Boolean) {
        hideCurrentLoadingDialog(this)
        clCParent.visibility = View.VISIBLE

        supportActionBar?.title = getString(R.string.todo_list)
        deleteMenuItem?.isVisible = false
        priorityMenuItem?.isVisible = false
        exitMenuItem?.isVisible = true
    }

    private fun onCheckListUpdated(items: MutableList<Int>) {
        dashboardViewModel.checkItems(items.size)
    }

    private fun onTodoItemsSet(todoItems: List<TodoItem>) {
        todRvLayoutManager.initialPrefetchItemCount = todoItems.size

        todoItemAdapter = TodoItemAdapter(this, todoItems)
        todoItemAdapter?.setTodoClickListener(this)


        rvItems.adapter = todoItemAdapter
        // rvItems.adapter?.notifyDataSetChanged()

        rvItems.visibility = View.VISIBLE
        tvNoItems.visibility = View.GONE
    }

    override fun onServiceCategoryClick(view: View, position: Int) {

    }

    private fun onItemAdded(showContent: Boolean) {
        showSuccessAlert(this, getString(R.string.done), getString(R.string.item_added), getString(R.string.ok)) {
            addItemFragment.dismiss()
            dashboardViewModel.displayTodoItems()
        }
    }

    private fun onItemDeleted(deletedItems: Int) {
        Toast.makeText(this, if(deletedItems == 1) getString(R.string.item_deleted) else "$deletedItems items deleted", Toast.LENGTH_LONG).show()
        dashboardViewModel.displayTodoItems()
    }

    private fun onError(errorMessage: String) {
        addItemFragment.errorContainer.blinkView(0.6f, 1.0f, 50, 2, Animation.ABSOLUTE, 0, {
            addItemFragment.errorContainer.visibility = View.VISIBLE
        })

        addItemFragment.contentSv.smoothScrollTo(0,0)
    }

    fun onAddButtonClicked(view: View){
        todoItemAdapter?.deselectAllItem()
        dashboardViewModel.checkList.value?.clear()
        dashboardViewModel.checkList.value = dashboardViewModel.checkList.value

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
                dashboardViewModel.deleteSelectedItems()
            }
            R.id.action_priority -> {
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
                todoItemAdapter?.deselectAllItem()
                dashboardViewModel.checkList.value?.clear()
                dashboardViewModel.checkList.value = dashboardViewModel.checkList.value // Todo: fix
            }
        }
        return true
    }

}