package co.za.dstv.mytodo.extensions


fun String.isValidTitle(): Boolean =  this.isNotEmpty() && this.length > 3
fun String.isValidDescription(): Boolean =  this.isNotEmpty() && this.length > 10