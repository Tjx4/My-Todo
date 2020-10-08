package co.za.dstv.mytodo.features.dashboard.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.databinding.FragmentAddItemBinding
import co.za.dstv.mytodo.features.base.fragments.BaseDialogFragment
import co.za.dstv.mytodo.features.dashboard.DashboardActivity
import java.util.*


class AddItemFragment : BaseDialogFragment() {
    private var dashboardActivity: DashboardActivity? = null
     lateinit var binding: FragmentAddItemBinding
     lateinit var closeSaveLocationListImgB: ImageButton
     lateinit var signInBtn: Button
     lateinit var dueDateDp: DatePicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_item, container, false)
        binding.lifecycleOwner = this
        binding.dashboardViewModel = dashboardActivity?.dashboardViewModel
        var parentView = binding.root
        initViews(parentView)
        return parentView
    }

    private fun initViews(parentView: View) {
        dueDateDp = parentView.findViewById(R.id.dpDueDate)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        dueDateDp.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { datePicker, year, month, dayOfMonth ->
            var correctMonth = month + 1
            var dd = "${year} ${correctMonth} ${dayOfMonth}"
            val selectedDate = Date()
            selectedDate.month = 9
            dashboardActivity?.dashboardViewModel?.setDueDate(selectedDate)
        }

        closeSaveLocationListImgB = parentView.findViewById(R.id.imgBCloseSaveLocationList)
        closeSaveLocationListImgB.setOnClickListener {
            dismiss()
        }

        signInBtn = parentView.findViewById(R.id.btnSignIn)
        signInBtn.setOnClickListener {
            dashboardActivity?.dashboardViewModel?.checkAndAddItem()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashboardActivity = context as DashboardActivity
    }

    companion object {
        fun newInstance(): BaseDialogFragment {
            val addItemFragment = AddItemFragment()
            var payload = Bundle()
            //payload.putString(TITLE, title)
            addItemFragment.arguments = payload
            return addItemFragment
        }
    }
}