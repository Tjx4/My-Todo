package co.za.dstv.mytodo.helpers

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDateAndTime(): String {
    val formatedDate = SimpleDateFormat("dd/MM/yyyy hh:mm")
    return formatedDate.format(Date())
}

fun getFormatedDateAndTime(day: Int, month: Int, year: Int, hour: Int, minute: Int): String {
    return "$day/$month/$year $hour:$minute"
}

fun getHourAhead(currentHour: Int): Int {
    val hour = currentHour + 1
    return if(hour > 23) 0 else hour
}