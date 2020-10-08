package co.za.dstv.mytodo.helpers

import java.text.SimpleDateFormat
import java.util.*

fun getDateAndTime(): String {
    val formatedDate = SimpleDateFormat("dd/MM/yyyy hh:mm")
    return formatedDate.format(Date())
}

fun getDateAndTimeFromDateFormat(year: Int, month: Int, day: Int): SimpleDateFormat {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
    simpleDateFormat.calendar.set(year, month, day)
    return simpleDateFormat
}