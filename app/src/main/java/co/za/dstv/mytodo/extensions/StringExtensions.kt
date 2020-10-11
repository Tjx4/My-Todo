package co.za.dstv.mytodo.extensions

import co.za.dstv.mytodo.helpers.getCurrentDateAndTime

fun String.isValidTitle(): Boolean =  this.isNotEmpty() && this.length > 3
fun String.isValidDescription(): Boolean =  this.isNotEmpty() && this.length > 10
fun String.isValidDueDate(): Boolean =  this != getCurrentDateAndTime()