package co.za.dstv.mytodo.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DbOperationResult (
    var success: Boolean = false
): Parcelable