package co.za.dstv.mytodo.features.dashboard

import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.features.database.tables.TodoItemsTable
import co.za.dstv.mytodo.models.DbOperationResult
import co.za.dstv.mytodo.models.TodoItem
import java.lang.Exception

class DashboardRepository(var database: TodoDb) {

    suspend fun addItemToDb(todoItem: TodoItem): DbOperationResult {
        return try {
            val todoItemsTable = TodoItemsTable(todoItem.id, todoItem.title, todoItem.description, todoItem.complete, todoItem.priority ,todoItem.dateCreated, todoItem.dueDate)
            database.todoItemsDao.insert(todoItemsTable)

            return DbOperationResult(true)
        }
        catch (ex: Exception){
            return DbOperationResult(false)
        }
    }

    suspend fun deleteItemsFromDb(items : List<TodoItem?> ): DbOperationResult {
        return try {
            val deleteList = arrayListOf<TodoItemsTable>()

            items.forEach {
                if(it != null){
                    val todoItemsTable = TodoItemsTable(it.id)
                    deleteList.add(todoItemsTable)
                }
            }
            database.todoItemsDao.delete(deleteList)

            return DbOperationResult(true)
        }
        catch (ex: Exception){
            return DbOperationResult(false)
        }
    }

    suspend fun toggleItemPrioriy(items : List<TodoItem?>): DbOperationResult {
        return try {
            val priorityItemList = arrayListOf<TodoItemsTable>()

            items.forEach {
                if(it != null){
                    val togglePriority = !it.priority
                    val todoItemsTable = TodoItemsTable(it.id, it.title, it.description, it.complete, togglePriority, it.dateCreated, it.dueDate)
                    priorityItemList.add(todoItemsTable)
                }
            }
            database.todoItemsDao.update(priorityItemList)

            return DbOperationResult(true)
        }
        catch (ex: Exception){
            return DbOperationResult(false)
        }
    }

    suspend fun getItemsFromDb(): List<TodoItem> {
        var todoItems =  ArrayList<TodoItem>()
        val itemTable = database.todoItemsDao.getAllItems()?.forEach {
            val currentItem = TodoItem(it.id, it.title, it.description, it.complete, it.priority ,it.dateCreated, it.dueDate)
            todoItems.add(currentItem)
        }

        return todoItems
    }

    suspend fun setItemAsComplete(items : List<TodoItem?>): DbOperationResult{
        return try {
            val priorityItemList = arrayListOf<TodoItemsTable>()

            items.forEach {
                if(it != null){
                    val todoItemsTable = TodoItemsTable(it.id, it.title, it.description, true, !it.priority, it.dateCreated, it.dueDate)
                    priorityItemList.add(todoItemsTable)
                }
            }
            database.todoItemsDao.update(priorityItemList)

            return DbOperationResult(true)
        }
        catch (ex: Exception){
            return DbOperationResult(false)
        }
    }

}