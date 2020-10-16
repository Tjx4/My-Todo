package co.za.dstv.mytodo.features.database.tables

import androidx.room.*

@Dao
interface TodoItemsDao {
    @Insert
    fun insert(todoItemsTable: TodoItemsTable)

    @Update
    fun update(todoItems: List<TodoItemsTable>)

    @Delete
    fun delete(todoItems: List<TodoItemsTable>)

    @Query("SELECT * FROM todo_items WHERE id = :key")
    fun get(key: Int): TodoItemsTable?

    @Query("SELECT * FROM todo_items ORDER BY id DESC LIMIT 1")
    fun getLastItem(): TodoItemsTable?

    @Query("SELECT * FROM todo_items ORDER BY id DESC")
    fun getAllItems():List<TodoItemsTable>?

    @Query("DELETE  FROM todo_items")
    fun clear()
}