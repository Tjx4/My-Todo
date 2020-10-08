package co.za.dstv.mytodo.features.dashboard

import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.features.database.tables.TodoItemsTable
import co.za.dstv.mytodo.models.TodoItem

class DashboardRepository(var database: TodoDb) {
    suspend fun addItemToDb(todoItem: TodoItem){
        val todoItemsTable = TodoItemsTable(todoItem.id, todoItem.title, todoItem.description)
        database.todoItemsDao.insert(todoItemsTable)
    }

    suspend fun deleteItemFromDb(todoItem: TodoItem){
        val todoItemsTable = TodoItemsTable(todoItem.id, todoItem.title, todoItem.description)
        database.todoItemsDao.delete(todoItemsTable)
    }

    suspend fun getItemsFromDb(): List<TodoItem> {
        var todoItems =  ArrayList<TodoItem>()
        val itemTable = database.todoItemsDao.getAllItems()?.forEach {
            val currentItem = TodoItem(it.id, it.title, it.description, it.complete, it.dateCreated, it.dueDate)
            todoItems.add(currentItem)
        }

        return todoItems
    }
}