package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/10/13
 *DESCRIPTION:
 **/
class BusinessOrder(var check : Boolean,val tradeNo : String){
    var isVip = false
    var phone : String? = null
    var finishDate : String? = null
    var orderObject : OrderObject? = null
}

class BusinessOrderResult{
    val orders = mutableListOf<BusinessOrder>()
    var businessMoney = 0.0 // 销售额
    var businessQuantity = 0 //笔数
    var customerPrice = 0.0
    var refundMoney = 0.0
    var refundQuantity = 0
}

class BusinessProduct{
    var saleTime : String? = null
    var isVip = false
    var phone : String? = null
    var productName = ""
    var specName : String? = null
    var price = 0.0
    var quantity = 0.0
    var total = 0.0
}