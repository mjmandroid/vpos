package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/24
 *DESCRIPTION:
 **/
class BusDailyTotal {
    //总营业额
    var bus_total_money = 0.0
    //总订单数
    var bus_total_order = 0
    //客单价
    var bus_avg_money = 0.0
    val goodsList = mutableListOf<GoodsItem>()
    val payList = mutableListOf<PayItem>()
    val promotionList = mutableListOf<PromotionWay>()

    class GoodsItem(var name : String?,var count : Double = 0.0,var money : Double = 0.0,var percent : Double = 0.0)
    class PayItem(var payName : String,var count : Int = 0,var money: Double = 0.0,var percent: Double = 0.0)
    class PromotionWay(var name : String,var count : Int = 0,var money : Double = 0.0,var percent : Double = 0.0)
}