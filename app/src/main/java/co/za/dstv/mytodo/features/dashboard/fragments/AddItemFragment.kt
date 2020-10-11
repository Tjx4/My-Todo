package co.za.dstv.mytodo.features.dashboard.fragments

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.*
import androidx.databinding.DataBindingUtil
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.databinding.FragmentAddItemBinding
import co.za.dstv.mytodo.extensions.blinkView
import co.za.dstv.mytodo.features.base.fragments.BaseDialogFragment
import co.za.dstv.mytodo.features.dashboard.DashboardActivity
import co.za.dstv.mytodo.helpers.getFormatedDateAndTime

class AddItemFragment : BaseDialogFragment() {
    private var dashboardActivity: DashboardActivity? = null
     lateinit var binding: FragmentAddItemBinding
     lateinit var closeSaveLocationListButton: Button
     lateinit var addTodoItemBtn: Button
     lateinit var dueDateDp: DatePicker
     lateinit var dueTimeTp: TimePicker
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

        closeSaveLocationListButton = parentView.findViewById(R.id.btnCloseSaveLocationList)
        closeSaveLocationListButton.setOnClickListener {
            dashboardActivity?.dashboardViewModel?.newItem?.value?.title = ""
            dashboardActivity?.dashboardViewModel?.newItem?.value?.description = ""
            dismiss()
        }

        dueDateDp = parentView.findViewById(R.id.dpDueDate)
        dueTimeTp = parentView.findViewById(R.id.tpDueTime)

        addTodoItemBtn = parentView.findViewById(R.id.btnAddTodoItem)
        addTodoItemBtn.setOnClickListener {
            setDueDateAndTime()
            dashboardActivity?.dashboardViewModel?.checkAndAddItem()
        }
    }

    private fun setDueDateAndTime() {
        val selectedDateTime = getFormatedDateAndTime(dueDateDp.dayOfMonth, dueDateDp.month + 1, dueDateDp.year, dueTimeTp.hour, dueTimeTp.minute)
        dashboardActivity?.dashboardViewModel?.setDueDate(selectedDateTime)
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