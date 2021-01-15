package com.tranpos.vpos.utils

import android.view.KeyEvent
import com.tranpos.vpos.callback.IPayKeyboardListener
import com.transpos.sale.thread.ThreadDispatcher


/**
 *CREATED BY AM
 *ON 2020/8/5
 *DESCRIPTION:
 **/
class CommonKeyHandler(val callback : IPayKeyboardListener) {
    private var mCaps = false
    private val mStrBuilder = StringBuilder()


    fun handle(event : KeyEvent){
        val keyCode = event.keyCode
        checkLetterStatus(event)
        if (event.action == KeyEvent.ACTION_UP) {
            if(keyCode == KeyEvent.KEYCODE_DEL){
                keyboardHandle("",keyCode)
                return
            }
            if(mStrBuilder.isEmpty()){
               ThreadDispatcher.getDispatcher().postOnMainDelayed(Runnable{
                   keyboardHandle(mStrBuilder.toString(),keyCode)
                   mStrBuilder.clear()
                },200)
            }
            val key = getInputCode(event)
            mStrBuilder.append(key)
        }
    }

     private fun keyboardHandle(key : String,keyCode : Int){
         callback.onKeyClick(keyCode)
    }


    private fun checkLetterStatus(event: KeyEvent) {
        val keyCode = event.keyCode
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            mCaps = event.action == KeyEvent.ACTION_DOWN
        }
    }

    private fun getInputCode(event: KeyEvent): Char {
        val keyCode = event.keyCode
        val aChar: Char
        aChar = if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            ((if (mCaps) 'A' else 'a').toInt() + keyCode - KeyEvent.KEYCODE_A).toChar()
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) { // 数字
            ('0'.toInt() + keyCode - KeyEvent.KEYCODE_0).toChar()
        } else { // 其他符号
            when (keyCode) {
                KeyEvent.KEYCODE_MINUS -> if (mCaps) '_' else '-'
                KeyEvent.KEYCODE_BACKSLASH -> if (mCaps) '|' else '\\'
                KeyEvent.KEYCODE_LEFT_BRACKET -> if (mCaps) '{' else '['
                KeyEvent.KEYCODE_RIGHT_BRACKET -> if (mCaps) '}' else ']'
                KeyEvent.KEYCODE_COMMA -> if (mCaps) '<' else ','
                KeyEvent.KEYCODE_PERIOD -> if (mCaps) '>' else '.'
                KeyEvent.KEYCODE_SLASH -> if (mCaps) '?' else '/'
                KeyEvent.KEYCODE_SEMICOLON -> if (mCaps) ':' else ';'
                KeyEvent.KEYCODE_APOSTROPHE -> if (mCaps) '"' else '\''
                else -> 0.toChar()
            }
        }
        return aChar
    }
}
