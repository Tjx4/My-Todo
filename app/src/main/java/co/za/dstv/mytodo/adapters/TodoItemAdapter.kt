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

        val dueDate = todoItem.dueDate
        holder.dueDateTv.text = "Due on $dueDate"

        if(allItems[position].isPriority){
            setItemPriority(position)
        }

        if(allItems[position].isSelected) {
            setItemSelected(position)
        }

        handleItemClicks(holder, position)
    }

    private fun handleItemClicks(
        holder: ViewHolder,
        position: Int
    ) {
        holder.itemView.setOnClickListener() {
            selectedPos = holder.layoutPosition

            if(allItems[position].isSelected) {
                dashboardActivity.dashboardViewModel.removeItemFroCheckList(position)
                deselectItem(position)

                if(allItems[position].isPriority){
                    setItemPriority(position)
                }
            }
            else {
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
                    setItemSelected(position)
                }
            }
        }

        holder.itemView.setOnLongClickListener {
            selectedPos = holder.layoutPosition

            if(allItems[position].isSelected){
                dashboardActivity.dashboardViewModel.removeItemFroCheckList(position)
                deselectItem(position)

                if(allItems[position].isPriority){
                    setItemPriority(position)
                }
            }
            else{
                dashboardActivity.dashboardViewModel.addNewTodoListItem(position)
                setItemSelected(position)
            }

            true
        }
    }

    fun deselectAllItem(){
        var indx = 0
        allItems.forEach() {
            deselectItem(indx)

            if(it.isPriority){
                setItemPriority(indx)
            }

            indx++
        }
    }

    private fun deselectItem(position: Int) {
        val parentView = allItems[position]?.itemView as View
        parentView.background = dashboardActivity.getDrawable(R.drawable.top_border)

        allItems[position].checkedImg.visibility = View.GONE
        allItems[position].isSelected
        allItems[position].isSelected = false
    }

    private fun setItemSelected(position: Int) {
        val parentView = allItems[position]?.itemView as View
        parentView.background = dashboardActivity.getDrawable(R.drawable.top_border_selected)

        allItems[position].checkedImg.visibility = View.VISIBLE
        allItems[position].isSelected = true
    }

    private fun setItemPriority(position: Int) {
        val parentView = allItems[position]?.itemView as View
        parentView.background = dashboardActivity.getDrawable(R.drawable.top_border_priority)

        allItems[position].checkedImg.visibility = View.GONE
        allItems[position].isPriority = true
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var isPriority = false
        var isSelected = false

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