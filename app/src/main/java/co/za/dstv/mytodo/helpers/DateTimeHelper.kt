package co.za.dstv.mytodo.helpers

import java.text.SimpleDateFormat
import java.time.Period
import java.util.*

fun getCurrentDateAndTime(): String {
    val formatedDate = SimpleDateFormat("dd/MM/yyyy hh:mm")
    return formatedDate.format(Date())
}

fun getFormatedDateAndTime(day: Int, month: Int, year: Int, hour: Int, minute: Int): String {
    return "$day/$month/$year $hour:$minute"
}

fun getTimeBetween(dateTimeFrom: String, dateTimeTo: String): String {
    val pattern = "dd/MM/yyyy hh:mm"
    val format = SimpleDateFormat(pattern)

    val fromDate = format.parse(dateTimeFrom)
    val toDate = format.parse(dateTimeTo)
    // val period = Period.between(fromDate.time.toInt(), fromDate.time.toInt())
    return "7 hours"
}