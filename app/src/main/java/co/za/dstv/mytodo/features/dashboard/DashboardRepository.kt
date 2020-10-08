package co.za.dstv.mytodo.features.dashboard

import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.features.database.tables.TodoItemsTable
import co.za.dstv.mytodo.models.TodoItem

class DashboardRepository(var database: TodoDb) {

    fun addItemToDb(todoItem: TodoItem){
        val todoItemsTable = TodoItemsTable(todoItem.id, todoItem.title, todoItem.description)
        database.todoItemsDao.insert(todoItemsTable)
    }

    fun deleteItemFromDb(todoItem: TodoItem){
        val todoItemsTable = TodoItemsTable(todoItem.id, todoItem.title, todoItem.description)
        database.todoItemsDao.delete(todoItemsTable)
    }
}