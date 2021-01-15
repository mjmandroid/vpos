package com.tranpos.vpos.utils.bus

import com.tranpos.vpos.entity.WProductModel
import com.tranpos.vpos.utils.StringKotlin


/**
 *CREATED BY AM
 *ON 2020/7/22
 *DESCRIPTION:
 **/
class WeightRuleHelper {

    companion object{
        fun acquireWeightByCode(code : String?) : WProductModel?{
            code?.let {
                val model = WProductModel()
                return when (it.length) {
                    13 -> {
                        model.flag = it.substring(0,2)
                        model.plu = it.substring(2,7)
                        model.price = StringKotlin.formatPriceFen(it.substring(7,12))
                        model.crc = it.substring(13)
                        model
                    }
                    18 -> {
                        model.flag = it.substring(0,2)
                        model.plu = it.substring(2,7)
                        model.price = StringKotlin.formatPriceFen(it.substring(7,12))
                        model.kg = StringKotlin.formatKg(it.substring(12,17))
                        model.singlePrice = if(model.kg == 0.0) 0.0 else StringKotlin.formatPriceDouble((model.price/model.kg).toFloat())
                        model.crc = it.substring(17)
                        model
                    }
                    else -> null
                }
            }
            return null
        }
    }
}