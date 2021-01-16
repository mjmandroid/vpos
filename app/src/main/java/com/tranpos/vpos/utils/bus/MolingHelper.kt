package com.tranpos.vpos.utils.bus

import android.util.Log
import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.entity.MolingSetting
import com.tranpos.vpos.entity.MolingType
import com.tranpos.vpos.utils.KeyConstrant
import com.tranpos.vpos.utils.StringKotlin
import com.transpos.tools.tputils.TPUtils
import kotlin.math.*

/**
 *CREATED BY AM
 *ON 2020/8/17
 *DESCRIPTION:
 **/
class MolingHelper {

    companion object {
        fun getMolingPrices(price : Double) : Array<Double>{
            //0 -> 抹零后金额  1->抹零金额
            val result = arrayOf(price,0.0)
            val open = TPUtils.get(BaseApp.getApplication(), KeyConstrant.KEY_MOLING_SWITCH,false)
            val setting = TPUtils.getObject(BaseApp.getApplication(),KeyConstrant.KEY_MOLING_SET,
                MolingSetting::class.java)
            if(open && setting != null && setting.isOpen){
                when(setting.type){
                    MolingType.ROUND_JIAO_TYPE -> {
                        result[0] = StringKotlin.formatPrice2((price * 10).roundToInt() / 10.0)
                        result[1] = StringKotlin.formatPrice2(((price * 100 - result[0]*100))/100)
                    }
                    MolingType.FLOOR_JIAO_TYPE -> {
                        result[0] = StringKotlin.formatPrice2(floor(price * 10) / 10.0)
                        result[1] = StringKotlin.formatPrice2(((price * 100 - result[0]*100))/100)
                    }
                    MolingType.CEIL_JIAO_TYPE -> {
                        result[0] = StringKotlin.formatPrice2(ceil(price * 10) / 10.0)
                        result[1] = StringKotlin.formatPrice2(((price * 100 - result[0]*100))/100)
                    }
                    MolingType.ROUND_YUAN_TYPE -> {
                        result[0] = StringKotlin.formatPrice2(price.roundToInt().toDouble())
                        result[1] = StringKotlin.formatPrice2(((price * 100 - result[0]*100))/100)
                    }
                    MolingType.FLOOR_YUAN_TYPE -> {
                        result[0] = StringKotlin.formatPrice2(floor(price))
                        result[1] = StringKotlin.formatPrice2(((price * 100 - result[0]*100))/100)
                    }
                    MolingType.CEIL_YUAN_TYPE -> {
                        result[0] = StringKotlin.formatPrice2(ceil(price))
                        result[1] = StringKotlin.formatPrice2(((price * 100 - result[0]*100))/100)
                    }
                    MolingType.FLOOR_FIVE_JIAO_TYPE -> {
                        val m = (price*10).toInt() % 10
                        when {
                            m < 5 -> {
                                result[0] = StringKotlin.formatPrice2(((price * 10).toInt() - m)/10.0)
                            }
                            m == 5 -> {
                                result[0] = StringKotlin.formatPrice2(((price * 10).toInt())/10.0)
                            }
                            else -> {
                                result[0] = StringKotlin.formatPrice2(((price * 10).toInt() - m + 5)/10.0)
                            }
                        }
                        result[1] = StringKotlin.formatPrice2(((price * 100 - result[0]*100))/100)
                    }
                    MolingType.CEIL_FIVE_JIAO_TYPE -> {
                        val m = (price*10).toInt() % 10
                        when {
                            m == 5 || m == 0 -> {
                                result[0] = StringKotlin.formatPrice2(((price * 10).toInt())/10.0)
                            }
                            m < 5 -> {
                                result[0] = StringKotlin.formatPrice2(((price * 10).toInt() - m + 5)/10.0)
                            }
                            else -> {
                                result[0] = StringKotlin.formatPrice2(((price * 10).toInt() - m + 10)/10.0)
                            }
                        }
                        result[1] = StringKotlin.formatPrice2(((price * 100 - result[0]*100))/100)
                    }
                }
            }
            if(result[0] == 0.0){
                Log.e("debug","抹零出现非法数据")
                result[0] = price
                result[1] = 0.0
            }
            return result
        }

    }
}