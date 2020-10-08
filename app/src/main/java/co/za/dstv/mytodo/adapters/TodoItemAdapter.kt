package co.za.dstv.mytodo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.features.dashboard.DashboardActivity
import co.za.dstv.mytodo.models.TodoItem


class TodoItemAdapter(context: Context, private val todoItem: List<TodoItem>) : RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {
    private val dashboardActivity = context as DashboardActivity
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var todoItemClickListener: TodoItemClickListener? = null
    private var todoItemLongClickListener: TodoItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.todo_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoItem = todoItem[position]
        holder.titleTv.text = todoItem.title
        holder.descriptionTv.text = todoItem.description
        holder.doneCb.isChecked = todoItem.complete

        //Todo: check if over due
        holder.dueDateTv.text = "Due on ${todoItem.dueDate}"
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        internal var titleTv = itemView.findViewById<TextView>(R.id.tvTitle)
        internal var descriptionTv = itemView.findViewById<TextView>(R.id.tvDescription)
        internal var dueDateTv = itemView.findViewById<TextView>(R.id.tvDueDate)
        internal var doneCb = itemView.findViewById<CheckBox>(R.id.cbDone)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener (this)
        }

        override fun onClick(view: View) {
            todoItemClickListener?.onServiceCategoryClick(view, adapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            todoItemLongClickListener?.onServiceCategoryLongClick(view, adapterPosition)
            return true
        }

    }

    internal fun getItem(id: Int): TodoItem? {
        return todoItem[id]
    }

    interface TodoItemClickListener {
        fun onServiceCategoryClick(view: View, position: Int)
    }

    interface TodoItemLongClickListener{
        fun onServiceCategoryLongClick(view: View, position: Int)
    }

    fun setTodoClickListener(todoItemClickListener: TodoItemClickListener) {
        this.todoItemClickListener = todoItemClickListener
    }

    fun setTodoLongClickListener(todoItemLongClickListener: TodoItemLongClickListener) {
        this.todoItemLongClickListener = todoItemLongClickListener
    }

    override fun getItemCount() = todoItem.size

}