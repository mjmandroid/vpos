package com.tranpos.vpos.utils

import android.content.res.Resources

/**
 *CREATED BY AM
 *ON 2020/11/17
 *DESCRIPTION:
 **/
object DimenUtil {
    fun dp2px(dpValue:Float ):Float{
        return  (0.5f + dpValue * Resources.getSystem().displayMetrics.density)
    }
}