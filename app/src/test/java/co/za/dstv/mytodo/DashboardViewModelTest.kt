package co.za.dstv.mytodo

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.za.dstv.mytodo.features.dashboard.DashboardRepository
import co.za.dstv.mytodo.features.dashboard.DashboardViewModel
import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.models.DbOperation
import co.za.dstv.mytodo.models.TodoItem
import com.nhaarman.mockitokotlin2.whenever
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
    fun `test Add new item`() = runBlocking  {
        val testItem = TodoItem()

        whenever(repository.addItemToDb(testItem)).thenReturn(DbOperation(true))
        val addItem = dashboardViewModel.addNewTodoListItem(testItem)

        assert(addItem.success)
    }

    @Test
    fun `test get items`() = runBlocking  {
        val testItems = arrayListOf<TodoItem>(
            TodoItem(1, "title", "description", true, false, "09/10/2020 15:00", "10/10/2020 16:00"),
            TodoItem(2, "title 2", "description 2", true, true, "09/10/2020 16:00", "10/10/2020 17:00"),
            TodoItem(3, "title 3", "description 3", false, true, "09/10/2020 17:00", "10/10/2020 19:00")
        )

        whenever(repository.getItemsFromDb()).thenReturn(testItems)

        assert(false)
    }

    @Test
    fun `test delete items`() = runBlocking  {
        val testItems = arrayListOf<TodoItem>(
            TodoItem(1, "title", "description", true, false, "09/10/2020 15:00", "10/10/2020 16:00"),
            TodoItem(2, "title 2", "description 2", true, true, "09/10/2020 16:00", "10/10/2020 17:00"),
            TodoItem(3, "title 3", "description 3", false, true, "09/10/2020 17:00", "10/10/2020 19:00")
        )

        whenever(repository.deleteItemsFromDb(testItems)).thenReturn(DbOperation(true))

        assert(false)
    }

    @Test
    fun `test set item complete`() = runBlocking  {

        assert(false)
    }

    @Test
    fun `test set item as priority`() = runBlocking  {

        assert(false)
    }

}