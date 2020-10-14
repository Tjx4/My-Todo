package co.za.dstv.mytodo.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.constants.INDEX
import co.za.dstv.mytodo.constants.TODO_ITEM_KEY
import co.za.dstv.mytodo.extensions.SLIDE_IN_ACTIVITY
import co.za.dstv.mytodo.extensions.blinkView
import co.za.dstv.mytodo.extensions.navigateToActivity
import co.za.dstv.mytodo.features.dashboard.DashboardActivity
import co.za.dstv.mytodo.features.item.ItemViewActivity
import co.za.dstv.mytodo.models.TodoItem

class TodoItemAdapter(context: Context, val todoItems: List<TodoItem>) : RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {
    private val dashboardActivity = context as DashboardActivity
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var todoItemClickListener: TodoItemClickListener? = null
    var selectedPos = 0
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

        val dueDate = todoItem.dueDate
        holder.dueDateTv.text = "Due on $dueDate"

        if(todoItem.isSelected) {
            setItemSelected(holder, position)
        }
        else{
            if(todoItem.priority){
                setItemPriority(holder)
            }
            else{
                deselectItem(holder, position)
            }
        }

        handleItemClicks(holder, todoItem, position)
    }

    private fun handleItemClicks(
        holder: ViewHolder,
        todoItem: TodoItem,
        position: Int
    ) {
        holder.itemView.setOnClickListener() {
            selectedPos = position

            if(todoItem.isSelected) {
                dashboardActivity.dashboardViewModel.removeItemFroCheckList(position)
                deselectItem(holder, position)

                if(todoItem.priority){
                    setItemPriority(holder)
                }
            }
            else {
                val isEmptyCheckList =
                    dashboardActivity.dashboardViewModel.checkList.value?.isNullOrEmpty() ?: true

                if (isEmptyCheckList) {
                    holder.itemView.blinkView(0.6f, 1.0f, 150, 2, Animation.ABSOLUTE, 0, {
                        val item =  todoItems[position]
                        var payload = Bundle()
                        payload.putParcelable(TODO_ITEM_KEY, item)
                        dashboardActivity.navigateToActivity(ItemViewActivity::class.java, SLIDE_IN_ACTIVITY, payload)
                    })

                } else {
                    dashboardActivity.dashboardViewModel.checkList.value?.add(position)
                    dashboardActivity.dashboardViewModel.checkList.value = dashboardActivity.dashboardViewModel.checkList.value
                    setItemSelected(holder, position)
                }
            }
        }

        holder.itemView.setOnLongClickListener {
            selectedPos = position

            if(todoItem.isSelected){
                dashboardActivity.dashboardViewModel.removeItemFroCheckList(position)
                deselectItem(holder, position)

                if(todoItem.priority){
                    setItemPriority(holder)
                }
            }
            else{
                dashboardActivity.dashboardViewModel.checkList.value?.add(position)
                dashboardActivity.dashboardViewModel.checkList.value = dashboardActivity.dashboardViewModel.checkList.value
                setItemSelected(holder, position)
            }

            true
        }
    }

    fun deselectAllItem(){
        var indx = 0
        allItems.forEach() {
            var pos= if(it.position < 0)  0 else it.position

            if(pos < todoItems.size) {
                deselectItem(it, pos)
                if(todoItems[pos]?.priority){
                    setItemPriority(it)
                }
            }

            indx++
        }
    }

    private fun deselectItem(holder: ViewHolder, position: Int) {
        val parentView = holder?.itemView as View
        parentView.background = dashboardActivity.getDrawable(R.drawable.list_item)
        holder.checkedImg.visibility = View.GONE

        if(position < todoItems.size) todoItems[position].isSelected = false
    }

    private fun setItemSelected(holder: ViewHolder, position: Int) {
        val parentView = holder?.itemView as View
        parentView.background = dashboardActivity.getDrawable(R.drawable.list_item_selected)
        holder.checkedImg.visibility = View.VISIBLE

        if(position < todoItems.size)  todoItems[position].isSelected = true
    }

    private fun setItemPriority(holder: ViewHolder) {
        val parentView = holder?.itemView as View
        parentView.background = dashboardActivity.getDrawable(R.drawable.list_item_priority)
        holder.checkedImg.visibility = View.GONE
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