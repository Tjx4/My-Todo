package co.za.dstv.mytodo.features.dashboard.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.ScrollView
import androidx.databinding.DataBindingUtil
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.databinding.FragmentAddItemBinding
import co.za.dstv.mytodo.features.base.fragments.BaseDialogFragment
import co.za.dstv.mytodo.features.dashboard.DashboardActivity
import co.za.dstv.mytodo.helpers.getDateAndTimeFromDateFormat
import java.util.*


class AddItemFragment : BaseDialogFragment() {
    private var dashboardActivity: DashboardActivity? = null
     lateinit var binding: FragmentAddItemBinding
     lateinit var closeSaveLocationListImgB: ImageButton
     lateinit var signInBtn: Button
     lateinit var dueDateDp: DatePicker
     lateinit var errorContainer: View
     lateinit var contentSv: ScrollView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window?.setGravity(Gravity.BOTTOM)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_item, container, false)
        binding.lifecycleOwner = this
        binding.dashboardViewModel = dashboardActivity?.dashboardViewModel
        var parentView = binding.root
        initViews(parentView)
        return parentView
    }

    private fun initViews(parentView: View) {
        errorContainer = parentView.findViewById(R.id.clErrorContainer)
        contentSv = parentView.findViewById(R.id.svContent)

        dueDateDp = parentView.findViewById(R.id.dpDueDate)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        dueDateDp.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { datePicker, year, month, dayOfMonth ->
            val correctMonth = month + 1
            val selectedDate = getDateAndTimeFromDateFormat(year, correctMonth, dayOfMonth)
            val date = selectedDate.format(Date())

            dashboardActivity?.dashboardViewModel?.setDueDate(date)
        }

        closeSaveLocationListImgB = parentView.findViewById(R.id.imgBCloseSaveLocationList)
        closeSaveLocationListImgB.setOnClickListener {
            dashboardActivity?.dashboardViewModel?.newItem?.value?.title = ""
            dashboardActivity?.dashboardViewModel?.newItem?.value?.description = ""
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

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    companion object {
        fun newInstance(): AddItemFragment {
            val addItemFragment = AddItemFragment()
            var payload = Bundle()
            //payload.putString(TITLE, title)
            addItemFragment.arguments = payload
            return addItemFragment
        }
    }
}