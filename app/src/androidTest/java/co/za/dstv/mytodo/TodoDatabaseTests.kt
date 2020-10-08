package co.za.dstv.mytodo

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.features.database.tables.TodoItemsDao
import co.za.dstv.mytodo.features.database.tables.TodoItemsTable
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TodoDatabaseTests{
    private lateinit var todoDb: TodoDb
    private lateinit var todoItemsDao: TodoItemsDao

    @Before
    fun createDB(){
        var context = InstrumentationRegistry.getInstrumentation().targetContext

        todoDb = Room.inMemoryDatabaseBuilder(context, todoDb::class.java)
            .allowMainThreadQueries()
            .build()

        todoItemsDao = todoDb.todoItemsDao
    }

    @After
    @Throws(IOException::class)
    fun closeDB(){
        todoDb.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetGameStats(){
        var todoItemsTable = TodoItemsTable()
        todoItemsTable.id = 1

        todoItemsDao.insert(todoItemsTable)
        val currentUser = todoItemsDao.get(todoItemsTable.id)

        assertEquals(currentUser?.id, todoItemsTable.id)
    }

}