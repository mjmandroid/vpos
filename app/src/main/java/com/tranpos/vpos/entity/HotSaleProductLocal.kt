package com.tranpos.vpos.entity

import com.tranpos.vpos.utils.IdWorkerUtils
import java.lang.StringBuilder

/**
 *CREATED BY AM
 *ON 2020/11/14
 *DESCRIPTION:
 **/
class HotSaleProductLocal {
    var isEnable = false
    var mode = CUSTOM_MODE
    var productIds = ""
    var day : String? = null

    companion object{
        const val CUSTOM_MODE = 1 //自定义模式
        const val LINE_MODE = 2 //线上模式
        val id = IdWorkerUtils.nextId()
        fun saveId(local: HotSaleProductLocal, list: List<HotSaleProduct>){
            val sb = StringBuilder()
            list.filter { it.isBoxChecked  }.forEachIndexed { index, hotSaleProduct ->
                sb.append(hotSaleProduct.specId)
                if(index < list.size - 1){
                    sb.append(",")
                }
            }
            local.productIds = sb.toString()
        }

        fun generateIds(local: HotSaleProductLocal) : List<String>{
            val list = mutableListOf<String>()
            if(local.productIds.isNotBlank()){
                return local.productIds.split(",").toList()
            }
            return list
        }
    }
}