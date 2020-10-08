package co.za.dstv.mytodo.features.item

import android.os.Bundle
import co.za.dstv.mytodo.R
import co.za.dstv.mytodo.features.base.activities.BaseChildActivity

class ItemViewActivity : BaseChildActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)
    }
}