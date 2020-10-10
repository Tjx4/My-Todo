package co.za.dstv.mytodo.features.dashboard

import co.za.dstv.mytodo.extensions.toItemsTable
import co.za.dstv.mytodo.extensions.todoItem
import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.features.database.tables.TodoItemsTable
import co.za.dstv.mytodo.models.DbOperation
import co.za.dstv.mytodo.models.TodoItem
import java.lang.Exception

class DashboardRepository(var database: TodoDb) {

    suspend fun addItemToDb(todoItem: TodoItem): DbOperation {
        return try {
            database.todoItemsDao.insert(todoItem.toItemsTable())
            return DbOperation(true)
        }
        catch (ex: Exception){
            return DbOperation(false)
        }
    }

    suspend fun deleteItemsFromDb(items : List<TodoItem?> ): DbOperation {
        return try {
            val deleteList = arrayListOf<TodoItemsTable>()

            items.forEach {
                if(it != null){
                    deleteList.add(TodoItemsTable(it.id))
                }
            }
            database.todoItemsDao.delete(deleteList)
            return DbOperation(true)
        }
        catch (ex: Exception){
            return DbOperation(false)
        }
    }

    suspend fun toggleItemPrioriy(items : List<TodoItem?>): DbOperation {
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

            return DbOperation(true)
        }
        catch (ex: Exception){
            return DbOperation(false)
        }
    }

    suspend fun getItemsFromDb(): List<TodoItem> {
        var todoItems =  ArrayList<TodoItem>()
        val itemTable = database.todoItemsDao.getAllItems()?.forEach {
            todoItems.add(it.todoItem())
        }

        return todoItems
    }


}