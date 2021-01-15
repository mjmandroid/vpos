package com.tranpos.vpos.utils


import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.entity.RegistrationCode
import com.transpos.tools.tputils.TPUtils
import java.util.*

class IdWorkerUtils {

    companion object{
        private val worker : IdWorker = IdWorker(0,0,0)
        private val api = TPUtils.getObject(
                BaseApp.getApplication(), KeyConstrant.KEY_AUTH_REGISTER,
                RegistrationCode::class.java
        )
        fun nextId() : String = worker.id.toString()

        fun generateSixSerialNum() : String{
            return "${((Math.random()*9+1)*100000).toInt()}"
        }

        fun createOrderId(tag : String) : String{
            var hashCodeV: Int = UUID.randomUUID().toString().hashCode()
            if (hashCodeV < 0) { //有可能是负数
                hashCodeV = -hashCodeV
            }
            // 0 代表前面补充0
            // 4 代表长度为4
            // d 代表参数为正数型
            return "$tag${String.format("%015d", hashCodeV)}"
        }

        fun generateTradeNo() : String{
            val serialNumber = Tools.generateSerialNumber(4,KeyConstrant.KEY_SERIAL_ORDER)
            return "${api.storeNo}1${DateUtil.getNowDateStr("yyMMddHHmm")}${api.posNo}$serialNumber${StringKotlin.generateString(
                2
            )}"
        }
    }
}