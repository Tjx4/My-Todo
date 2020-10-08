package co.za.dstv.mytodo.features.dashboard

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.adapters.TodoItemAdapter
import co.za.dstv.mytodo.databinding.ActivityDashboardBinding
import co.za.dstv.mytodo.features.base.activities.BaseParentActivity
import co.za.dstv.mytodo.features.base.fragments.BaseDialogFragment
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
    private lateinit var addItemFragment: BaseDialogFragment

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
    }

    private fun addObservers() {
        dashboardViewModel.showLoading.observe(this, Observer { onShowLoading(it) })
        dashboardViewModel.showContent.observe(this, Observer { onShowContent(it) })
        dashboardViewModel.errorMessage.observe(this, Observer { onError(it) })
        dashboardViewModel.isItemAdded.observe(this, Observer { onItemAdded(it) })
        dashboardViewModel.isNoItems.observe(this, Observer { onNoItems(it) })
        dashboardViewModel.todoItems.observe(this, Observer { onTodoItemsSet(it) })
    }

    private fun onTodoItemsSet(todoItems: List<TodoItem>) {
        val todRvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems?.layoutManager = todRvLayoutManager


        todRvLayoutManager.initialPrefetchItemCount = todoItems.size

        val todoItemAdapter = TodoItemAdapter(this, todoItems)
        todoItemAdapter.setTodoClickListener(this)
        rvItems.adapter = todoItemAdapter

        rvItems.visibility = View.VISIBLE
        tvNoItems.visibility = View.GONE
    }

    private fun onNoItems(isNoItems: Boolean) {
        rvItems.visibility = View.GONE
        tvNoItems.visibility = View.VISIBLE
    }

    override fun onServiceCategoryClick(view: View, position: Int) {

    }

    fun onItemSelected(view: View, position: Int) {
        dashboardViewModel.addToCheckList(position)

        val parent = view as CardView
        val child = parent.getChildAt(0) as FrameLayout
        val grandChild = child.getChildAt(0) as ConstraintLayout

        grandChild.background = getDrawable(R.drawable.top_border_selected)

        // val item = dashboardViewModel?.todoItems?.value?.get(position)
        // Toast.makeText(this, "Long click == ${item?.title}", Toast.LENGTH_LONG).show()
    }

    fun onItemDeSelected(view: View, position: Int) {
        dashboardViewModel.removeFromCheckList(position)

        val parent = view as CardView
        val child = parent.getChildAt(0) as FrameLayout
        val grandChild = child.getChildAt(0) as ConstraintLayout

        grandChild.background = getDrawable(R.drawable.top_border)
    }

    private fun onShowLoading(isBusy: Boolean) {
        clCParent.visibility = View.INVISIBLE
        showLoadingDialog(dashboardViewModel.busyMessage, this)
    }

    private fun onShowContent(showContent: Boolean) {
        hideCurrentLoadingDialog(this)
        clCParent.visibility = View.VISIBLE
    }

    private fun onItemAdded(showContent: Boolean) {
        showSuccessAlert(this, getString(R.string.done), getString(R.string.item_added), getString(R.string.ok)) {
            addItemFragment.dismiss()
            dashboardViewModel.setTodoItems()
        }
    }

    private fun onError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    fun onAddButtonClicked(view: View){
        addItemFragment = AddItemFragment.newInstance()
        addItemFragment.isCancelable = true
        showDialogFragment(getString(R.string.add_item), R.layout.fragment_add_item, addItemFragment,this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return super.onKeyDown(keyCode, event)
        }

        return true
    }

}