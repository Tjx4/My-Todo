package co.za.dstv.mytodo.extensions

import co.za.dstv.mytodo.features.database.tables.TodoItemsTable
import co.za.dstv.mytodo.models.TodoItem

fun TodoItem.toItemsTable(): TodoItemsTable {
    return TodoItemsTable(this.id, this.title, this.description, this.complete, this.priority ,this.dateCreated, this.dueDate)
}

fun TodoItemsTable.todoItem(): TodoItem {
    return TodoItem(this.id, this.title, this.description, this.complete, this.priority ,this.dateCreated, this.dueDate)
}
