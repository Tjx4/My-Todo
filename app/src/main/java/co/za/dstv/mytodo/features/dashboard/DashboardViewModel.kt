package co.za.dstv.mytodo.features.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.za.dstv.mytodo.features.base.viewModels.BaseVieModel

class DashboardViewModel(application: Application, val dashboardRepository: DashboardRepository) : BaseVieModel(application) {

    private var _showLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showLoading: MutableLiveData<Boolean>
        get() = _showLoading

    private val _showContent: MutableLiveData<Boolean> = MutableLiveData()
    val showContent: MutableLiveData<Boolean>
        get() = _showContent

    private val _todoProgress: MutableLiveData<Int> = MutableLiveData()
    var todoProgress: MutableLiveData<Int> = MutableLiveData()
        get() = _todoProgress

    private val _todoProgressPcnt: MutableLiveData<String> = MutableLiveData()
    val todoProgressPcnt: MutableLiveData<String>
        get() = _todoProgressPcnt

    private val _itemTitle: MutableLiveData<String> = MutableLiveData()
    val itemTitle: MutableLiveData<String>
        get() = _itemTitle

    private val _itemDescription: MutableLiveData<String> = MutableLiveData()
    val itemDescription: MutableLiveData<String>
        get() = _itemDescription

    var busyMessage: String = ""

    init {
        _todoProgress.value = 70
        _todoProgressPcnt.value = "${_todoProgress.value}%"
    }
}