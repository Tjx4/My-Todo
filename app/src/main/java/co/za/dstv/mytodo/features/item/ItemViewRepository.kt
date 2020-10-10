package co.za.dstv.mytodo.features.item

import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.features.database.tables.TodoItemsTable
import co.za.dstv.mytodo.models.DbOperation
import co.za.dstv.mytodo.models.TodoItem
import java.lang.Exception

class ItemViewRepository(var database: TodoDb) {

    suspend fun setItemAsComplete(items : List<TodoItem?>): DbOperation {
        return try {
            val completeItemList = arrayListOf<TodoItemsTable>()

            items.forEach {
                if(it != null){
                    val todoItemsTable = TodoItemsTable(it.id, it.title, it.description, true, it.priority, it.dateCreated, it.dueDate)
                    completeItemList.add(todoItemsTable)
                }
            }
            database.todoItemsDao.update(completeItemList)

            return DbOperation(true)
        }
        catch (ex: Exception){
            return DbOperation(false)
        }
    }
}