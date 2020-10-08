package co.za.dstv.mytodo.features.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.za.dstv.mytodo.features.database.tables.TodoItemsDao
import co.za.dstv.mytodo.features.database.tables.TodoItemsTable

@Database(entities = [TodoItemsTable::class], version = 1, exportSchema = false)
abstract class TodoDb : RoomDatabase() {
    abstract val todoItemsDao: TodoItemsDao

    companion object{
        @Volatile
        private var INSTANCE: TodoDb? = null

        fun getInstance(context: Context): TodoDb {
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, TodoDb::class.java, "todo_db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return  instance
            }
        }
    }

}