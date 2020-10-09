package co.za.dstv.mytodo.features.item

import android.os.Bundle
import android.widget.Toast
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.constants.PAYLOAD_KEY
import co.za.dstv.mytodo.constants.TODO_ITEM_KEY
import co.za.dstv.mytodo.features.base.activities.BaseChildActivity
import co.za.dstv.mytodo.models.TodoItem

class ItemViewActivity : BaseChildActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)

        var item = intent.getBundleExtra(PAYLOAD_KEY).getParcelable<TodoItem>(TODO_ITEM_KEY)

        Toast.makeText(this, "${item?.title}", Toast.LENGTH_LONG).show()
    }
}