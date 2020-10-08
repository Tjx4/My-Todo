package co.za.dstv.mytodo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.features.dashboard.DashboardActivity
import co.za.dstv.mytodo.models.TodoItem

class TodoItemAdapter(context: Context, private val todoItem: List<TodoItem>) : RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {
    private val dashboardActivity = context as DashboardActivity
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var todoItemClickListener: TodoItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.todo_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoItem = todoItem[position]
        holder.titleTv.text = todoItem.title
        holder.descriptionTv.text = todoItem.description
        holder.doneCb.isChecked = todoItem.complete
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var titleTv = itemView.findViewById<TextView>(R.id.tvTitle)
        internal var descriptionTv = itemView.findViewById<TextView>(R.id.tvDescription)
        internal var doneCb = itemView.findViewById<CheckBox>(R.id.cbDone)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            todoItemClickListener?.onServiceCategoryClick(view, adapterPosition)
        }
    }

    internal fun getItem(id: Int): TodoItem? {
        return todoItem[id]
    }

    interface TodoItemClickListener {
        fun onServiceCategoryClick(view: View, position: Int)
    }

    fun setLocationClickListener(todoItemClickListener: TodoItemClickListener) {
        this.todoItemClickListener = todoItemClickListener
    }

    override fun getItemCount() = todoItem.size

}