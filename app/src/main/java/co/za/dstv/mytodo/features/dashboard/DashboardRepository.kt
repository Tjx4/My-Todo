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
}