package co.za.dstv.mytodo.features.database.tables

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "todo_items")
data class TodoItemsTable (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int = 0,
    @ColumnInfo(name = "title")
    var title:String? = null,
    @ColumnInfo(name = "description")
    var description:String? = null,
    @ColumnInfo(name = "complete")
    var complete:Boolean = false,
    @ColumnInfo(name = "priority")
    var priority:Boolean = false,
    @ColumnInfo(name = "dateCreated")
    var dateCreated: String? = null,
    @ColumnInfo(name = "dueDate")
    var dueDate: String? = null,
): Parcelable