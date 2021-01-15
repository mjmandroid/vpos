package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/31
 *DESCRIPTION:
 **/
class OrderPromotionsBuilder {
    var clientId : String? = ""
    var clientOrderId : String? = ""
    var tradeNo : String? = ""
    var storeId : String? = ""
    var storeNo : String? = ""
    var storeName : String? = ""
    var onlineFlag = 0
    var promotionType = -1
    var scheduleId : String? = ""
    var scheduleSn : String? = ""
    var promotionId : String? = ""
    var promotionSn : String? = ""
    var promotionMode : String? = ""
    var scopeType : String? = ""
    var promotionPlan : String? = ""
    var amount = 0.0
    var discountAmount = 0.0
    var receivableAmount = 0.0
    var discountRate = 0.0
    var enabled = 0
    var couponId : String? = ""
    var couponNo : String? = ""
    var couponName : String? = ""
    var finishDate : String? = ""
}