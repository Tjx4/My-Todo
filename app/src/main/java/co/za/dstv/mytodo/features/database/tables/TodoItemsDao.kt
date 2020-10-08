package co.za.dstv.mytodo.features.database.tables

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoItemsDao {
    @Insert
    fun insert(todoItemsTable: TodoItemsTable)

    @Update
    fun update(todoItemsTable: TodoItemsTable)

    @Delete
    fun delete(todoItemsTable: TodoItemsTable)

    @Query("SELECT * FROM todo_items WHERE id = :key")
    fun get(key: Long): TodoItemsTable?

    @Query("SELECT * FROM todo_items ORDER BY id DESC LIMIT 1")
    fun getLastItem(): TodoItemsTable?

    @Query("SELECT * FROM todo_items ORDER BY id DESC")
    fun getAllItemsLiveData(): LiveData<List<TodoItemsTable>>

    @Query("SELECT * FROM todo_items ORDER BY id DESC")
    fun getAllItems():List<TodoItemsTable>?

    @Query("DELETE  FROM todo_items")
    fun clear()
}