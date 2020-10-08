package co.za.dstv.mytodo.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodoItem (
      var id: Int = 0,
      var title: String? = null,
      var description: String? = null,
      var complete: Boolean? = null
): Parcelable