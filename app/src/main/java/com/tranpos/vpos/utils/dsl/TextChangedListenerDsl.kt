package com.tranpos.vpos.utils.dsl

import android.text.Editable
import android.text.TextWatcher

/**
 *CREATED BY AM
 *ON 2020/12/23
 *DESCRIPTION:
 **/
class TextChangedListenerDsl : TextWatcher {

    var afterListener : ((Editable?) ->Unit)?  = null
    var beforeListener : ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null
    var changedListener : ((s: CharSequence?, start: Int, before: Int, count: Int)->Unit)? = null

    infix fun afterTextChanged(method :(Editable?)->Unit){
        this.afterListener = method
    }

    infix fun beforeTextChanged(method : ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)){
        this.beforeListener = method
    }

    infix fun onTextChanged(method : (s: CharSequence?, start: Int, before: Int, count: Int)->Unit){
        this.changedListener = method
    }

    override fun afterTextChanged(s: Editable?) {
        afterListener?.invoke(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeListener?.invoke(s,start,count,after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        changedListener?.invoke(s,start,before,count)
    }
}