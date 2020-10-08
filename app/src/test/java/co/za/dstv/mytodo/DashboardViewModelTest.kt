package co.za.dstv.mytodo

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.za.dstv.mytodo.features.dashboard.DashboardRepository
import co.za.dstv.mytodo.features.dashboard.DashboardViewModel
import co.za.dstv.mytodo.features.database.TodoDb
import co.za.dstv.mytodo.models.TodoItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

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
        repository.database = database
        dashboardViewModel = DashboardViewModel(application, repository)
    }

    @Test
    fun `test Add item`() = runBlocking  {
        val testItem = TodoItem()
        dashboardViewModel.addItem(testItem)
        assert(false)
    }

}