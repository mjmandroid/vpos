package com.tranpos.vpos.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.tranpos.vpos.utils.dsl.TextChangedListenerDsl
import com.tranpos.vpos.utils.usbPrint.UsbPhoneService
import java.io.Serializable
import java.util.ArrayList


fun <T> Collection<T>.isListEmpty() : Boolean{
    return this == null || this.isEmpty()
}


inline fun <reified T: Activity> Context.startActivity(
    vararg params: Pair<String, String>) {
    val intent = Intent(this, T::class.java)
    params.forEach { intent.putExtra(it.first, it.second) }
    startActivity(intent)
}

inline fun <reified T: Activity> Activity.startActivityForResults(code : Int,
    vararg params: Pair<String, String>) {
    val intent = Intent(this, T::class.java)
    params.forEach { intent.putExtra(it.first, it.second) }
    startActivityForResult(intent,code)
}

inline fun <reified T> Context.map() : T{
    return this as T
}

fun Context.loopPrint(data: ArrayList<ByteArray>){
    val it = Intent()
    it.action = UsbPhoneService.ACTION_USB_ADD_USBREMIN_DATA
    it.putExtra("data", data as Serializable)
    this.sendBroadcast(it)
}

fun TextView.addTextListener(listenerDsl: TextChangedListenerDsl.() ->Unit){
    var apply = TextChangedListenerDsl().apply(listenerDsl)
    this.addTextChangedListener(apply)
}

fun Double.toMinus() : Double{
    return -this
}

fun TextView.addTextChangedListenerDsl(block : TextChangedListenerDsl.() ->Unit){
    val listener = TextChangedListenerDsl().apply(block)
    this.addTextChangedListener(listener)
}