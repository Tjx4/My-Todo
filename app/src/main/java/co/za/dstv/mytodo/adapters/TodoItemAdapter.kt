package co.za.dstv.mytodo.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.constants.TODO_ITEM_KEY
import co.za.dstv.mytodo.extensions.SLIDE_IN_ACTIVITY
import co.za.dstv.mytodo.extensions.navigateToActivity
import co.za.dstv.mytodo.features.dashboard.DashboardActivity
import co.za.dstv.mytodo.features.item.ItemViewActivity
import co.za.dstv.mytodo.models.TodoItem


class TodoItemAdapter(context: Context, private val todoItems: List<TodoItem>) : RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {
    private val dashboardActivity = context as DashboardActivity
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var todoItemClickListener: TodoItemClickListener? = null
    private var selectedPos = RecyclerView.NO_POSITION
    var allItems = ArrayList<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.todo_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        allItems.add(holder)
        val todoItem = todoItems[position]
        holder.titleTv.text = todoItem.title
        holder.descriptionTv.text = todoItem.description
        holder.doneCb.isChecked = todoItem.complete

        //Todo: check if over due
        val due = todoItem.dueDate
        holder.dueDateTv.text = "Due on $due"
        if(due == null){
            holder.dueDateTv.visibility = View.INVISIBLE
        }

        if(todoItem.priority){
            setItemPriority(holder)
        }

        handleItemClicks(holder, position)
    }

    private fun handleItemClicks(
        holder: ViewHolder,
        position: Int
    ) {
        holder.itemView.setOnClickListener() {
            selectedPos = holder.layoutPosition

            if (holder.checkedImg.visibility != View.VISIBLE) {
                val isEmptyCheckList =
                    dashboardActivity.dashboardViewModel.checkList.value?.isNullOrEmpty() ?: true

                if (isEmptyCheckList) {
                    notifyItemChanged(selectedPos)

                    val item =  todoItems[position]
                    var payload = Bundle()
                    payload.putParcelable(TODO_ITEM_KEY, item)
                    dashboardActivity.navigateToActivity(ItemViewActivity::class.java, SLIDE_IN_ACTIVITY, payload)
                } else {
                    dashboardActivity.dashboardViewModel.addNewTodoListItem(position)
                    setItemSelected(holder)
                }
            } else {
                dashboardActivity.dashboardViewModel.removeItemFroCheckList(position)
                deselectItem(holder)

                if(todoItems[position].priority){
                    setItemPriority(holder)
                }
            }
        }

        holder.itemView.setOnLongClickListener {
            selectedPos = holder.layoutPosition

            if (holder.checkedImg.visibility != View.VISIBLE) {
                dashboardActivity.dashboardViewModel.addNewTodoListItem(position)
                setItemSelected(holder)
            } else {
                notifyItemChanged(selectedPos)
                dashboardActivity.dashboardViewModel.removeItemFroCheckList(position)
                deselectItem(holder)

                if(todoItems[position].priority){
                    setItemPriority(holder)
                }
            }

            true
        }
    }

    fun deselectAllItem(){
        var indx = 0
        allItems.forEach(){
            deselectItem(it)

            if(todoItems[indx].priority){
                setItemPriority(it)
            }

            indx++
        }
    }

    private fun deselectItem(
        holder: ViewHolder?
    ) {
        val parent = holder?.itemView as CardView
        val child = parent.getChildAt(0) as FrameLayout
        val grandChild = child.getChildAt(0) as ConstraintLayout
        grandChild.background = dashboardActivity.getDrawable(R.drawable.top_border)

        holder.checkedImg.visibility = View.GONE
       // holder.itemView.isSelected = false
    }

    private fun setItemSelected(
        holder: ViewHolder?
    ) {
        val parent = holder?.itemView as CardView
        val child = parent.getChildAt(0) as FrameLayout
        val grandChild = child.getChildAt(0) as ConstraintLayout
        grandChild.background = dashboardActivity.getDrawable(R.drawable.top_border_selected)

        holder.checkedImg.visibility = View.VISIBLE
        // holder.itemView.isSelected = true
    }

    private fun setItemPriority(
        holder: ViewHolder?
    ) {
        val parent = holder?.itemView as CardView
        val child = parent.getChildAt(0) as FrameLayout
        val grandChild = child.getChildAt(0) as ConstraintLayout
        grandChild.background = dashboardActivity.getDrawable(R.drawable.top_border_priority)

        holder.checkedImg.visibility = View.GONE
        // holder.itemView.isSelected = false
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var titleTv = itemView.findViewById<TextView>(R.id.tvTitle)
        internal var descriptionTv = itemView.findViewById<TextView>(R.id.tvDescription)
        internal var dueDateTv = itemView.findViewById<TextView>(R.id.tvDueDate)
        internal var doneCb = itemView.findViewById<CheckBox>(R.id.cbDone)
        internal var checkedImg = itemView.findViewById<ImageView>(R.id.imgChecked)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            todoItemClickListener?.onServiceCategoryClick(view, adapterPosition)
        }

    }

    internal fun getItem(id: Int): TodoItem? {
        return todoItems[id]
    }

    interface TodoItemClickListener {
        fun onServiceCategoryClick(view: View, position: Int)
    }

    fun setTodoClickListener(todoItemClickListener: TodoItemClickListener) {
        this.todoItemClickListener = todoItemClickListener
    }

    override fun getItemCount() = todoItems.size

}