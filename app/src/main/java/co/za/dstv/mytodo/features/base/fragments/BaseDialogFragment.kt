package co.za.dstv.mytodo.features.base.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.constants.LAYOUT


abstract class BaseDialogFragment : DialogFragment() {
    protected var clickedView: View? = null
    protected var activity: AppCompatActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogTheme
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val layout = arguments!!.getInt(LAYOUT)
        return inflater.inflate(layout, container, false)
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog?.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    protected fun setViewClickEvents(views: Array<View>) {
        for (view in views) {
            if (view == null)
                continue

            view.setOnClickListener { v -> onFragmentViewClickedEvent(v) }
        }
    }

    open fun hideLoaderAndShowEnterMessage() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity?
    }

    protected fun setListviewClickEvents(listView: ListView) {
        listView.setOnItemClickListener { parent, view, position, id ->
            onFragmentViewClickedEvent(view)
        }
    }

    protected fun onFragmentViewClickedEvent(view: View) {
        dialog?.dismiss()
    }

}