package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/9/8
 *DESCRIPTION:
 **/
class OrderItemPayBuilder {
    var clientId : String? = ""
    var clientOrderId : String? = ""
    var tradeNo : String? = ""
    var clientOrderItemId : String? = ""
    var clientPayId : String? = ""
    var no : String? = ""
    var name : String? = ""
    var storeId : String? = ""
    var storeNo : String? = ""
    var storeName : String? = ""
    var productId : String? = ""
    var productName : String? = ""
    var specId : String? = ""
    var specName : String? = ""
    var couponId : String? = ""
    var couponNo : String? = ""
    var couponName : String? = ""
    var faceAmount : Double = 0.0
    var shareCouponLeastCost : Double = 0.0
    var shareAmount : Double = 0.0
    var refundAmount : Double = 0.0
    var finishDate : String? = ""

}