package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/7/21
 *DESCRIPTION: 支付参数构建
 **/
class PaysBuilder {
    var clientId : String? = ""
    var clientOrderId : String? = ""
    var tradeNo : String? = ""
    var storeId : String? = ""
    var storeNo : String? = ""
    var storeName : String? = ""
    var orderNo = 0
    var no : String? = ""
    var name : String? = ""
    var amount = 0.0
    var inputAmount = 0.0
    var faceAmount = 0.0
    var paidAmount = 0.0
    var overAmount = 0.0
    var changeAmount = 0.0
    var platformDiscount = 0.0
    var platformPaid = 0.0
    var payNo : String? = ""
    var prePayNo : String? = ""
    var channelNo : String? = ""
    var voucherNo : String? = ""
    var status = -1
    var statusDesc : String? = ""
    var subscribe = 0
    var useConfirmed : String? = ""
    var accountName : String? = ""
    var bankType : String? = ""
    var memo : String? = ""
    var payTime : String? = ""
    var finishDate : String? = ""
    var payChannel = 0
    var pointFlag = 0
    var incomeFlag = 1
    var cardNo : String? = ""
    var cardPreAmount = 0.0
    var cardAftAmount = 0.0
    var cardChangeAmount = 0.0
    var cardPrePoint = 0.0
    var cardChangePoint = 0.0
    var cardAftPoint = 0.0
    var memberMobileNo : String? = ""
    var cardFaceNo : String? = ""
    var shiftId : String? = ""
    var shiftNo : String? = ""
    var shiftName : String? = ""
    var couponId : String? = ""
    var couponNo : String? = ""
    var couponName : String? = ""
    var memberName : String? = ""
    var isMember : Int = 0

    var couponLeastCost = 0.0

}