package com.tranpos.vpos.core

import com.tranpos.vpos.entity.MultipleQueryProduct
import com.tranpos.vpos.entity.PromotionOrder
import com.tranpos.vpos.entity.state.PromotionTypeEnumFlag
import com.tranpos.vpos.utils.DateUtil
import com.tranpos.vpos.utils.IdWorkerUtils
import com.tranpos.vpos.utils.MathUtil
import com.tranpos.vpos.utils.StringKotlin
import com.tranpos.vpos.db.manger.PromotionOrderDbManger


/**
 *CREATED BY AM
 *ON 2020/8/31
 *DESCRIPTION:
 **/
object PromotionOrderManger {

    private val mOrderMap = mutableMapOf<Int, PromotionOrder>()

    fun createPOrder(type : Int) : PromotionOrder{
        val oldOrder = mOrderMap[type]
        return if(oldOrder == null){
            val promotionOrder = PromotionOrder()
            promotionOrder.let {
                it.id = IdWorkerUtils.nextId()
                it.tradeNo = OrderManger.getOrderBean()?.tradeNo
                it.orderId = OrderManger.getOrderBean()?.id
                it.tenantId = OrderManger.getOrderBean()?.tenantId
                it.onlineFlag = if(type == PromotionTypeEnumFlag.ONLINE) 1 else 0
                it.promotionType = type
            }
            mOrderMap[type] = promotionOrder
            promotionOrder
        } else {
            oldOrder
        }
    }

    fun clearData(){
        mOrderMap.clear()
    }

    fun insertOnlineDb(){
        val list = mOrderMap.filter { it.key == PromotionTypeEnumFlag.ONLINE || it.key == PromotionTypeEnumFlag.MEMBER_REDUCE }.map {
            it.value
        }
        if(list.isNotEmpty()){
            list.forEach {
                if(it.discountAmount > 0.0){
                    PromotionOrderDbManger.insert(it)
                }
            }
        }
    }

    fun setupOrder(oldPrice : Double, dataSet : MutableList<MultipleQueryProduct>, type : Int){
        var p = false
        var newPrice = 0.0
        dataSet.forEach {
            newPrice += it.getmAmount() * it.salePrice.toDouble()
            if(it.isEvenSpecialPrice){
                p = true
            }
        }
        if(oldPrice > newPrice && p){
            val po = mOrderMap[type]
            po?.price = oldPrice
            po?.bargainPrice = newPrice
            po?.amount = oldPrice
            po?.receivableAmount = newPrice
            po?.discountAmount = MathUtil.subtractMoney(oldPrice,newPrice)
            po?.discountRate = po?.discountAmount ?: 0.0 / oldPrice
            po?.enabled = 1
            po?.finishDate = DateUtil.getNowDateStr()
        }
    }

    fun setupOrder(discount : Double,oldPrice : Double,type : Int){
        val po = mOrderMap[type]
        po?.price = oldPrice
        po?.bargainPrice = StringKotlin.formatPrice2(oldPrice * discount / 100)
        po?.amount = oldPrice
        po?.receivableAmount = po?.bargainPrice ?: 0.0
        po?.discountAmount = MathUtil.subtractMoney(oldPrice,po?.receivableAmount ?: 0.0)
        po?.discountRate = po?.discountAmount ?: 0.0 / oldPrice
        po?.enabled = 1
        po?.finishDate = DateUtil.getNowDateStr()
    }

    fun setArgueOrder(bargainPrice : Double,oldPrice : Double,type : Int){
        val po = mOrderMap[type]
        po?.price = oldPrice
        po?.bargainPrice = bargainPrice
        po?.amount = oldPrice
        po?.receivableAmount = po?.bargainPrice ?: 0.0
        po?.discountAmount = MathUtil.subtractMoney(oldPrice,po?.receivableAmount ?: 0.0)
        po?.discountRate = po?.discountAmount ?: 0.0 / oldPrice
        po?.enabled = 1
        po?.finishDate = DateUtil.getNowDateStr()
    }

    /**
     * 插入单品折扣,整单折扣
     */
    fun insertPromotionDiscountOrder(dataSet : MutableList<MultipleQueryProduct>){
        val po = mOrderMap[PromotionTypeEnumFlag.SINGLE_DISCOUNT]
        if(po != null){
            var p = 0.0
            var r = 0.0
            var old = 0.0
            dataSet.forEach {
                if(it.isSingleArgue && it.isSingleDiscount){
                    if(it.singleArguePrice.toDouble() < it.singleDiscountPrice.toDouble()){
                        return@forEach
                    }
                }
                if(it.isSingleDiscount && it.singleDiscountPrice.toDouble() == it.salePrice.toDouble() && !it.isEvenSpecialPrice){
                    p += it.singleDiscountPrice.toDouble()
                    r += (it.originPrice.toFloat() - it.singleDiscountPrice.toDouble())
                    old += it.originPrice.toDouble()
                }
            }
            if(p > 0.0 && r > 0.0){
                po.price = StringKotlin.formatPrice2(p)
                po.bargainPrice = StringKotlin.formatPrice2(p - r)
                po.amount = po.price
                po.receivableAmount = po.bargainPrice
                po.discountAmount = StringKotlin.formatPrice2(r)
                po.discountRate = po.discountAmount / po.price
                po.enabled = 1
                po.finishDate = DateUtil.getNowDateStr()
                PromotionOrderDbManger.insert(po)
            }
        }
        val orderArguePo =  mOrderMap[PromotionTypeEnumFlag.ALL_ARGUE]
        val orderDiscountPo =  mOrderMap[PromotionTypeEnumFlag.ALL_DISCOUNT]
        if(orderArguePo != null && orderDiscountPo != null){
            if(orderArguePo.discountAmount < orderDiscountPo.discountAmount){
                PromotionOrderDbManger.insert(orderDiscountPo)
            }
        } else if(orderDiscountPo != null){
            PromotionOrderDbManger.insert(orderDiscountPo)
        }
    }

    /**
     * 插入单品议价，整单议价
     */
    fun insertPromotionArgueOrder(dataSet : MutableList<MultipleQueryProduct>){
        val po = mOrderMap[PromotionTypeEnumFlag.SINGLE_ARGUE]
        if(po != null){
            var p = 0.0
            var r = 0.0
            var old = 0.0
            dataSet.forEach {
                if(it.isSingleDiscount && it.isSingleArgue){
                    if(it.singleDiscountPrice.toDouble() < it.singleArguePrice.toDouble() ){
                        return@forEach
                    }
                }
                if(it.isSingleArgue && it.singleArguePrice.toDouble() == it.salePrice.toDouble() && !it.isEvenSpecialPrice){
                    p += it.singleArguePrice.toDouble()
                    r += (it.originPrice.toFloat() - it.singleArguePrice.toDouble())
                    old += it.originPrice.toDouble()
                }
            }
            if(p > 0.0 && r > 0.0){
                po.price = StringKotlin.formatPrice2(old)
                po.bargainPrice = StringKotlin.formatPrice2(p)
                po.amount = po.price
                po.receivableAmount = po.bargainPrice
                po.discountAmount = StringKotlin.formatPrice2(r)
                po.discountRate = po.discountAmount / po.price
                po.enabled = 1
                po.finishDate = DateUtil.getNowDateStr()
                PromotionOrderDbManger.insert(po)
            }
        }
        val orderArguePo =  mOrderMap[PromotionTypeEnumFlag.ALL_ARGUE]
        val orderDiscountPo =  mOrderMap[PromotionTypeEnumFlag.ALL_DISCOUNT]
        if(orderArguePo != null && orderDiscountPo != null){
            if(orderArguePo.discountAmount > orderDiscountPo.discountAmount){
                PromotionOrderDbManger.insert(orderArguePo)
            }
        } else if(orderArguePo != null){
            PromotionOrderDbManger.insert(orderArguePo)
        }
    }


    fun getPromotionOrder(type : Int) : PromotionOrder?{
        return mOrderMap[type]
    }
}