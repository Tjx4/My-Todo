package co.za.dstv.mytodo.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class TodoItem (
      var id: Int = 0,
      var title: String? = null,
      var description: String? = null,
      var complete: Boolean = false,
      var dateCreated: Date? = null,
      var dueDate: Date? = null
): Parcelable