package com.tranpos.vpos.utils

/**
 *CREATED BY AM
 *ON 2020/10/14
 *DESCRIPTION:快速点击处理
 **/
class ClickProfiler {
    private var lastTime = 0L
    companion object{
        const val LIMIT_INTERVAL = 1000L
    }

    fun verifyInterval() : Boolean{
        val curTime = System.currentTimeMillis()
        val interval = (curTime - lastTime)
        lastTime = curTime
        return interval > LIMIT_INTERVAL
    }
    fun verifyInterval(varInterval : Long) : Boolean{
        val curTime = System.currentTimeMillis()
        val interval = (curTime - lastTime)
        lastTime = curTime
        return interval > varInterval
    }
}