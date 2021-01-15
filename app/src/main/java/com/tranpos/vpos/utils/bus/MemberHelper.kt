package com.tranpos.vpos.utils.bus

import android.util.Log
import com.tranpos.vpos.db.manger.LineSalesSettingDbManger
import com.tranpos.vpos.entity.Member
import com.tranpos.vpos.entity.MultipleQueryProduct
import com.tranpos.vpos.utils.StringKotlin

/**
 *CREATED BY AM
 *ON 2020/9/19
 *DESCRIPTION:
 **/
class MemberHelper {
    companion object{
        /**
         * 获取会员余额
         */
        fun getBalance(member: Member?) : String{
            return if(member == null){
                "0"
            } else{
                val t1 = member.totalAmount
                var t2 = -1.0
                if(member.cardList.isNotEmpty()){
                    t2 = member.cardList.first().totalAmount
                }
                if(t1 != t2){
                    StringKotlin.formatPrice(t2)
                } else {
                    StringKotlin.formatPrice(t1)
                }
            }
        }


        fun calcVipPrice(member: Member?, item: MultipleQueryProduct): String {
            var price = item.originPrice
            var discount = 1.0f
            member?.let {
                var discountWay = it.memberLevel?.discountWay
                //优惠方式
                // //0：零售价；1：会员价；2：会员价折扣；3：零售价折扣；4：批发价；5：会员价2；6：会员价3；7：会员价4；8：会员价5；

                when (discountWay) {
                    0 -> {
                        Log.e("debug", "没有优惠")
                    }
                    //按会员价
                    1 -> price = item.vipPrice
                    5 -> price = item.vipPrice2
                    6 -> price = item.vipPrice3
                    7 -> price = item.vipPrice4
                    8 -> price = item.vipPrice5
                    //会员价折扣
                    2 -> {
                        val loadAll = LineSalesSettingDbManger.getInstance().loadAll()
                        var tag = "0"
                        run breaking@{
                            loadAll.forEach { set ->
                                if (set.setKey.equals("allow_vip_discount_for_bandiscount_product")) {
                                    tag = set.setValue
                                    return@breaking
                                }
                            }
                        }
                        if (tag.equals("0")) {
                            Log.e("debug", "商品禁止打折，不享受会员折扣")
                        } else {
                            discount = it.memberLevel.discount.toFloat() / 100
                            price = StringKotlin.formatPrice(item.vipPrice.toFloat() * discount)
                        }

                    }
                    //零售价折扣
                    3 -> {
                        val loadAll = LineSalesSettingDbManger.getInstance().loadAll()
                        var tag = "0"
                        run breaking@{
                            loadAll.forEach { set ->
                                if (set.setKey.equals("allow_vip_discount_for_bandiscount_product")) {
                                    tag = set.setValue
                                    return@breaking
                                }
                            }
                        }
                        if (tag.equals("0")) {
                            Log.e("debug", "商品禁止打折，不享受会员折扣")
                        } else {
                            discount = it.memberLevel.discount.toFloat() / 100
                            price = StringKotlin.formatPrice(item.originPrice.toFloat() * discount)
                        }
                    }
                    //批发价
                    4 -> {
                        price = item.batchPrice
                    }
                    else -> {

                    }
                }
            }
            if (price.toFloat() == 0f) {
                price = item.originPrice
            }
            if (price.toFloat() < 0.1f) {
                price = 0.01f.toString()
            }
            if (price.toFloat() < item.minPrice.toFloat()) {
                price = item.minPrice
            }
            return price
        }
    }
}