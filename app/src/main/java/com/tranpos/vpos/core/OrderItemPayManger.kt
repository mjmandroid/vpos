package com.tranpos.vpos.core

import com.tranpos.vpos.db.manger.OrderItemPayDbManger
import com.tranpos.vpos.entity.OrderItem
import com.tranpos.vpos.entity.OrderItemPay
import com.tranpos.vpos.utils.DateUtil
import com.tranpos.vpos.utils.IdWorkerUtils
import com.tranpos.vpos.utils.MathUtil
import com.tranpos.vpos.utils.StringKotlin

/**
 *CREATED BY AM
 *ON 2020/9/8
 *DESCRIPTION:支付方式分摊管理类
 **/
object OrderItemPayManger{

    private val mItemPayMap = mutableMapOf<String, OrderItemPay>()

    fun createItemPay(itemId : String,payNo : String,orderItem : OrderItem){
        val key = getMapKey(itemId,payNo)
        val itemPay = mItemPayMap[key]
        if(itemPay == null){
            val itemPay = OrderItemPay()
            OrderManger.getOrderBean()?.let {
                itemPay.id = IdWorkerUtils.nextId()
                itemPay.orderId = it.id
                itemPay.tradeNo = it.tradeNo
                itemPay.itemId = itemId
                itemPay.productId = orderItem.productId
                itemPay.specId = orderItem.specId
                itemPay.no = payNo
                itemPay.name = OrderPayManger.getPayName(payNo)
                itemPay.finishDate = DateUtil.getNowDateStr()
                mItemPayMap[key] = itemPay
            }
        }

    }

    /**
     * payMap key=payNo value = money
     */
    fun sharePayAndInsert(payMap : Map<String,Double>){
        calcPercent()
        payMap.forEach {
            val payNo = it.key
            val money = it.value
            val list = OrderItemManger.getList()
            if(list.isEmpty())
                return@forEach
            if(list.size == 1){
                getItemPay(list.first().id,payNo)?.shareAmount = money
            } else {
                var part = 0.0
                list.forEachIndexed { index, orderItem ->
                    if(index != list.size - 1){
                        val m = StringKotlin.formatPrice2(money * orderItem.payPercent)
                        getItemPay(orderItem.id,payNo)?.shareAmount = m
                        part = MathUtil.addMoney(part,m)
                    }
                }
                getItemPay(list.last().id,payNo)?.shareAmount = MathUtil.subtractMoney(money,part)
            }
            val dataSet = mItemPayMap.filter { m->
                m.key.split(",")[1] == payNo
            }.map {
                m->
                m.value
            }
            if(dataSet.isNotEmpty()){
                OrderItemPayDbManger.insert(dataSet)
            }
        }
    }

    fun removeItem(itemId : String,payNo : String){
        val key = getMapKey(itemId,payNo)
        mItemPayMap.remove(key)
    }

    fun removeItems(itemId : String){
        val iterator = mItemPayMap.entries.iterator()
        while (iterator.hasNext()){
            val next = iterator.next()
            if(next.key.contains(itemId)){
                iterator.remove()
            }
        }
    }

    private fun calcPercent(){
        val list = OrderItemManger.getList()
        val total = list.fold(0.0){acc, orderItem -> acc + orderItem.receivableAmount }
        if(total > 0){
            if(list.size == 1){
                list.forEach {
                    it.payPercent = it.receivableAmount / total
                }
            } else {
                var part = 0.0
                list.forEachIndexed { index, orderItem ->
                    if(index != list.size - 1){
                        orderItem.payPercent = StringKotlin.formatPrice2(orderItem.receivableAmount / total)
                        part += orderItem.payPercent * 100
                    }
                }
                list.last().payPercent = (100 - part) / 100
            }
        }

    }

    private fun getMapKey(itemId : String,payNo : String) : String{
        return "$itemId,$payNo"
    }

    private fun getItemPay(itemId : String,payNo : String) : OrderItemPay?{
        return mItemPayMap[getMapKey(itemId,payNo)]
    }

    fun clearData(){
        mItemPayMap.clear()
    }
}