package com.tranpos.vpos.entity

import com.tranpos.vpos.utils.StringKotlin

/**
 *CREATED BY AM
 *ON 2020/8/10
 *DESCRIPTION:
 **/
class HangOrderBean(var isCheck : Boolean,val order : OrderObject){
    var totalQuantity = "0" // 总商品数量

    fun setTotalQuantity(items : List<OrderItem>){
        totalQuantity = StringKotlin.formatSmart(items.fold(0.0){ acc, orderItem -> acc + orderItem.quantity })
    }
}