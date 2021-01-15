package com.tranpos.vpos.utils

import android.graphics.Paint
import android.widget.TextView
import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.entity.TicketTemplete
import com.tranpos.vpos.entity.order.OrderNoRule
import com.tranpos.vpos.core.OrderPayManger
import com.transpos.tools.tputils.TPUtils
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern


class StringKotlin {

    companion object{
        private val NUMBERCHAR = "0123456789"
        private val df = DecimalFormat("0.00")
        private val df4 = DecimalFormat("0.0000")
        private val df3 = DecimalFormat("0.000")
        private val df2 = DecimalFormat("0.##")

        fun formatPrice(price : Float) : String{
            return df.format(price)
        }

        fun formatPrice(price : Double) : String{
            if(price == 0.0){
                return "0.00"
            }
            return df.format(price)
        }
        fun formatPrice2(price : Double) : Double{
            return df.format(price).toDouble()
        }
        fun formatPrice3(price : Double) : String {
            return df3.format(price)
        }
        fun _formatPrice3(price : Double) : Double {
            return df3.format(price).toDouble()
        }
        fun formatPrice4(price : Double) : Double{
            return df4.format(price).toDouble()
        }
        fun formatSmart(price : Double) : String{
            if(price == 0.0){
                return "0"
            }
            return df2.format(price)
        }

        fun formatSmart(price : String) : String{
            return df2.format(price.toFloat())
        }

        fun formatPriceFloat(price : Float) : Float{
            return df.format(price).toFloat()
        }

        fun formatPriceDouble(price : Float) : Double{
            return df.format(price).toDouble()
        }
        fun formatPriceDouble(price : Double) : Double{
            return df.format(price).toDouble()
        }

        //fun formatAmount(count : Int) : String = df.format(count)
        fun formatPriceFen(price: String) : Double{
            var p = 0.0
            try {
                val value = price.toInt()
                p = formatPriceDouble((value / 100.0).toFloat())
            } catch (e: Exception) {
            }
            return p
        }

        fun formatKg(g: String) : Double{
            var p = 0.0
            try {
                val value = g.toInt()
                p = formatPriceDouble((value / 1000.0).toFloat())
            } catch (e: Exception) {
            }
            return p
        }

        fun formatKg(g : Int) : Double{
            val k = g / 1000.0
            return df3.format(k).toDouble()
        }
        /**
         * 添加下划线
         */
        fun underLineText(tv: TextView, str:String){
            tv.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
            tv.text = str
        }

        /**
         * 移除线
         */
        fun removeLine(tv: TextView){
            tv.paint.flags = 0
        }

        fun getLast2str(str: String) : String?{
            var result : String? = null
            if(str.length < 3){
                result = str
            } else{
                result = str.substring(str.length-2,str.length)
                result = "*$result"
            }

            return result
        }

        fun generateString(len : Int) : String{
            val sb = StringBuilder(len)
            val random = Random()
            for (i in 0 until len) {
                sb.append(NUMBERCHAR[random.nextInt(NUMBERCHAR.length)])
            }
            return sb.toString()
        }

        fun getFenValue(floatMoney: Float) : Int{
            return (floatMoney * 100).toInt()
        }

        fun getFenToYuan(value : Int) : Double{
            val z = value / 100
            val m = value % 100
            return "${z}.${m}".toDouble()
        }

        fun isNumeric(str : String) : Boolean{
            val pattern: Pattern = Pattern.compile("[0-9]*")
            return pattern.matcher(str).matches()
        }
        fun isEmpty(str: String?) : Boolean{
            return str == null || str.trim().isEmpty()
        }

        fun hidePhone(phone : String) : String{
            if(phone.length == 11){
                return phone.substring(0,3) + "****"+phone.substring(7)
            }
            return phone
        }

        fun hideName(name : String?) : String{
            var res = ""
            name?.let {
                if(it.isNotEmpty()){
                    res = "${it.substring(0,1)}**"
                }
            }
            return res
        }

        fun getIdsString(ids : List<String>) : String{
            var idStr = ""
            if(ids.size == 1){
                idStr = ids[0]
            } else{
                ids.forEachIndexed { index, s ->
                    idStr += s
                    if(index != ids.size - 1){
                        idStr += ","
                    }
                }
            }
            return idStr
        }

        /**
         * 区分条码类型
         */
        fun discriminateCode(code : String?) : OrderPayManger.PayModeEnum{
            var mode = OrderPayManger.PayModeEnum.NONE
            if(code != null && code.length == 18){
               val  pattern = "\\d*"
                val matches = Pattern.matches(pattern, code)
                if(matches){
                    val flag = code.substring(0, 2).toInt()
                    mode = when(flag){
                        in 10..15 ->{
                            OrderPayManger.PayModeEnum.PAYWX
                        }
                        in 25..30 ->{
                            OrderPayManger.PayModeEnum.PAYAIL
                        }
                        else ->{
                            OrderPayManger.PayModeEnum.PAYMEMBER
                        }
                    }
                }
            }
            return mode
        }

        /**
         *
         * 获取流水号
         */
        fun getSerialNum() : String{
            val ticketSet = TPUtils.getObject(
                    BaseApp.getApplication(),
                    KeyConstrant.KEY_TICKET_TEMPLETE,
                    TicketTemplete::class.java
            ) ?: TicketTemplete()
            if(!ticketSet.top_serial_num){
                return ""
            }
            var num = 1
            val bit = ticketSet.serialBit
            TPUtils.getObject(BaseApp.getApplication(),KeyConstrant.KEY_ORDER_NO, OrderNoRule::class.java).takeIf {
                it != null
            }.let {
                if(it == null){
                    return ""
                } else{
                    if(DateUtil.isSameDay(it.modifyTime,DateUtil.getNowDateStr())){
                        it.orderNo ++
                        num = it.orderNo
                    } else{
                        it.orderNo = num
                    }
                    it.modifyTime = DateUtil.getNowDateStr()
                    TPUtils.putObject(BaseApp.getApplication(),KeyConstrant.KEY_ORDER_NO,it)
                }
            }
            var target = "$num"
            when(bit){
                2 ->{
                    if(num < 10){
                        target = "0$target"
                    }
                }
                3 -> {
                    if(num < 10){
                        target = "00$target"
                    } else if(num < 100){
                        target = "0$target"
                    }
                }
                4 -> {
                    if(num < 10){
                        target = "000$target"
                    } else if(num < 100){
                        target = "00$target"
                    } else if(num < 1000){
                        target = "0$target"
                    }
                }
            }
            return target
        }
    }
}