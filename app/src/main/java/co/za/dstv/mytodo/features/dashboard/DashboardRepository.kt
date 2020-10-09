package co.za.dstv.mytodo.features.dashboard

import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.features.database.tables.TodoItemsTable
import co.za.dstv.mytodo.models.TodoItem

class DashboardRepository(var database: TodoDb) {
    suspend fun addItemToDb(todoItem: TodoItem){
        val todoItemsTable = TodoItemsTable(todoItem.id, todoItem.title, todoItem.description, todoItem.complete ,todoItem.dateCreated, todoItem.dueDate)
        database.todoItemsDao.insert(todoItemsTable)
    }

    suspend fun deleteItemsFromDb(items : List<TodoItem?> ){
        val deleteList = arrayListOf<TodoItemsTable>()

        items.forEach {
            if(it != null){
                val todoItemsTable = TodoItemsTable(it.id, it?.title, it?.description, it?.complete ?: false, it?.dateCreated, it?.dueDate)
                deleteList.add(todoItemsTable)
            }
        }
        database.todoItemsDao.delete(deleteList)
    }

    suspend fun getItemsFromDb(): List<TodoItem> {
        var todoItems =  ArrayList<TodoItem>()
        val itemTable = database.todoItemsDao.getAllItems()?.forEach {
            val currentItem = TodoItem(it.id, it.title, it.description, it.complete, it.dateCreated, it.dueDate)
            todoItems.add(currentItem)
        }

        return todoItems
    }

    suspend fun setItemAsComplete(todoItem: TodoItem){
        val todoItemsTable = TodoItemsTable(todoItem.id, todoItem.title, todoItem.description, todoItem.complete, todoItem.dateCreated, todoItem.dueDate)
        database.todoItemsDao.update(todoItemsTable)
    }

}