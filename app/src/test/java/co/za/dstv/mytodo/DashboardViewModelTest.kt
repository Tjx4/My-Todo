package co.za.dstv.mytodo

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.za.dstv.mytodo.features.dashboard.DashboardRepository
import co.za.dstv.mytodo.features.dashboard.DashboardViewModel
import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.models.DbOperation
import co.za.dstv.mytodo.models.TodoItem
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DashboardViewModelTest {
    private lateinit var dashboardViewModel: DashboardViewModel

    @Mock
    lateinit var repository: DashboardRepository
    @Mock
    lateinit var database: TodoDb
    @Mock
    lateinit var application: Application

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        dashboardViewModel = DashboardViewModel(application, repository)
    }


    @Test
    fun `test check items`() = runBlocking  {
        val itemCount = 0

        dashboardViewModel.checkItems(itemCount)

        assert(dashboardViewModel.isViewMode.value == true)
    }

    @Test
    fun `test get progress`() = runBlocking  {
        val itemCount = 0

        dashboardViewModel.checkItems(itemCount)

        assert(dashboardViewModel.isViewMode.value == true)
    }

    @Test
    fun `test Add new item`() = runBlocking  {
        val testItem = TodoItem()

        whenever(repository.addItemToDb(testItem)).thenReturn(DbOperation(true))
        val addItem = dashboardViewModel.checkAndAddItem()

        assert(false)
    }

    @Test
    fun `test get items`() = runBlocking  {
        val testItems = arrayListOf<TodoItem>(
            TodoItem(1, "title", "description", true, false, "09/10/2020 15:00", "10/10/2020 16:00"),
            TodoItem(2, "title 2", "description 2", true, true, "09/10/2020 16:00", "10/10/2020 17:00"),
            TodoItem(3, "title 3", "description 3", false, true, "09/10/2020 17:00", "10/10/2020 19:00")
        )

        whenever(repository.getItemsFromDb()).thenReturn(testItems)
        val todoItems = dashboardViewModel.getTodoItems()

        assert(todoItems.isNotEmpty())
    }

    @Test
    fun `test delete items`() = runBlocking  {
        dashboardViewModel.todoItems.value = arrayListOf<TodoItem>(
            TodoItem(1, "title", "description", true, false, "09/10/2020 15:00", "10/10/2020 16:00"),
            TodoItem(2, "title 2", "description 2", true, true, "09/10/2020 16:00", "10/10/2020 17:00"),
            TodoItem(3, "title 3", "description 3", false, true, "09/10/2020 17:00", "10/10/2020 19:00")
        )
        val item = (dashboardViewModel.todoItems.value as ArrayList<TodoItem>)[2]
        val item2 = (dashboardViewModel.todoItems.value as ArrayList<TodoItem>)[0]
        val itemDeleteList = arrayListOf<TodoItem>(item, item2)

        whenever(repository.deleteItemsFromDb(itemDeleteList)).thenReturn(DbOperation(true))
        val deleteItems = dashboardViewModel.checkAndDeleteItems()

        assertEquals(1, (dashboardViewModel.todoItems.value as ArrayList<TodoItem>).count())
    }


    @Test
    fun `test set item as priority`()  {
        dashboardViewModel.todoItems.value = arrayListOf<TodoItem>(
            TodoItem(1, "title", "description", true, false, "09/10/2020 15:00", "10/10/2020 16:00"),
            TodoItem(2, "title 2", "description 2", true, false, "09/10/2020 16:00", "10/10/2020 17:00"),
            TodoItem(3, "title 3", "description 3", false, false, "09/10/2020 17:00", "10/10/2020 19:00")
        )
        dashboardViewModel.checkList.value?.add(2)
        val item = (dashboardViewModel.todoItems.value as ArrayList<TodoItem>)[2]
        val itemPriorityList = arrayListOf<TodoItem>(item)

        runBlocking {
            whenever(repository.toggleItemPrioriy(itemPriorityList)).thenReturn(DbOperation(true))
            dashboardViewModel.setPriorityOnSelectedItems()
        }

        assert((dashboardViewModel.todoItems.value as ArrayList<TodoItem>)[2].priority)
    }

}