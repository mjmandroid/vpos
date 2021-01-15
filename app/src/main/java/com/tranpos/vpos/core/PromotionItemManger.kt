package com.tranpos.vpos.core

import android.text.TextUtils
import com.tranpos.vpos.entity.OrderItem
import com.tranpos.vpos.entity.PromotionItem
import com.tranpos.vpos.entity.state.PromotionTypeEnumFlag
import com.tranpos.vpos.utils.DateUtil
import com.tranpos.vpos.utils.IdWorkerUtils
import com.tranpos.vpos.utils.StringKotlin
import com.tranpos.vpos.entity.DirectSpecialInfo


/**
 *CREATED BY AM
 *ON 2020/8/25
 *DESCRIPTION:促销管理类
 **/
object PromotionItemManger {

    private val dataSet = mutableMapOf<String, PromotionItem>()
    var needSpecialCategory = false
    var needSpecialBrand = false
    val evenSpecialMap = mutableMapOf<String,Double>()
    val anySpecialMap = mutableMapOf<String,Double>()
    val anySpecialCountMap = mutableMapOf<String,Double>()
    val directSpecialMap = mutableMapOf<String, DirectSpecialInfo>()
    //商品 件数折
    val discountPCountMap = mutableMapOf<String,Double>()
    //商品 买满折扣
    val discountPFullMap = mutableMapOf<String,Double>()
    //商品直接折扣
    val discountPDirectMap = mutableMapOf<String,Double>()
    //品牌 件数折
    val discountBCountMap = mutableMapOf<String,Double>()
    //品牌 满金额折扣
    val discountBFullMap = mutableMapOf<String,Double>()
    //品牌 直接折扣
    val discountBDirectMap = mutableMapOf<String,Double>()
    //类别 件数折扣
    val discountCCountMap = mutableMapOf<String,Double>()
    //类别 满金额折扣
    val discountCFullMap = mutableMapOf<String,Double>()
    //类别 直接折扣
    val discountCDirectMap = mutableMapOf<String,Double>()
    //全场 件数折
    val discountGCountMap = mutableMapOf<String,Double>()
    //全场 满金额折扣
    val discountGFullMap = mutableMapOf<String,Double>()
    //全场 直接折扣
    val discountGDirectMap = mutableMapOf<String,Double>()
    /**
     * key 是orderItem的id
     */
    fun createPromotionItem(itemId : String,promotionId : String) : PromotionItem{
        var orderBean = OrderManger.getOrderBean()
        if(orderBean == null){
            orderBean = OrderManger.initalize()
        }
        val item = PromotionItem()
        item.id = IdWorkerUtils.nextId()
        item.tenantId = orderBean.tenantId
        item.orderId = itemId
        item.itemId = itemId
        item.tradeNo = orderBean.tradeNo

        dataSet[getRealKey(itemId,promotionId)] = item
        return item
    }

    fun createGiftPromotion(itemId : String,promotionId : String,oItem : OrderItem) : PromotionItem{
        var orderBean = OrderManger.getOrderBean()
        if(orderBean == null){
            orderBean = OrderManger.initalize()
        }
        val item = PromotionItem()
        item.id = IdWorkerUtils.nextId()
        item.clientId = item.id
        item.clientOrderItemId = itemId
        item.storeId = orderBean.storeId
        item.storeName = orderBean.storeName
        item.storeNo = orderBean.storeNo
        item.tenantId = orderBean.tenantId
        item.orderId = itemId
        item.itemId = itemId
        item.tradeNo = orderBean.tradeNo
        item.promotionType = PromotionTypeEnumFlag.GIFT
        item.amount = oItem.discountAmount
        item.reason = oItem.giftReason
        item.price = if(oItem.quantity != 0.0) StringKotlin.formatPrice2(oItem.discountPrice / oItem.quantity) else 0.0
        item.bargainPrice = 0.0
        item.discountAmount = item.discountAmount
        item.discountRate = 1.0
        item.finishDate = DateUtil.getNowDateStr()
        dataSet[getRealKey(itemId,promotionId)] = item
        return item
    }

    private fun getRealKey(itemId : String,promotionId : String) : String{
        return itemId + promotionId
    }

    fun getItemPromotion(itemId : String,promotionId : String) : PromotionItem?{
        return dataSet[getRealKey(itemId,promotionId)]
    }

    fun removePromotion(itemId : String,promotionId : String?){
        if(!TextUtils.isEmpty(promotionId)){
            dataSet.remove(getRealKey(itemId,promotionId!!))
        } else {
            val list = mutableListOf<String>()
            dataSet.forEach {
                if(it.key.startsWith(itemId)){
                    list.add(it.key)
                }
            }
            list.forEach {
                dataSet.remove(it)
            }
        }
    }

    fun getAllPromotion() : Map<String,PromotionItem>{
        return dataSet
    }

    fun clearData(){
        needSpecialBrand = false
        needSpecialCategory = false
        evenSpecialMap.clear()
        anySpecialMap.clear()
        anySpecialCountMap.clear()
        directSpecialMap.clear()
        dataSet.clear()
        discountPCountMap.clear()
        discountPFullMap.clear()
        discountPDirectMap.clear()
        discountBCountMap.clear()
        discountBFullMap.clear()
        discountBDirectMap.clear()
        discountCCountMap.clear()
        discountCFullMap.clear()
        discountCDirectMap.clear()
        discountGCountMap.clear()
        discountGFullMap.clear()
        discountGDirectMap.clear()
    }
}