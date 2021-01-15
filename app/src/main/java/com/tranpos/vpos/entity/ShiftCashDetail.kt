package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/4
 *DESCRIPTION:
 **/
class ShiftCashDetail {
    // 消费现金收入
    var consume : Double = 0.0
    //储值卡充值现金
    var cardRecharge : Double = 0.0
    //plus会员现金
    var plus : Double = 0.0
    // 计次项目现金充值
    var project : Double = 0.0
    // 消费现金退款
    var consumeBack : Double = 0.0
    //储值卡现金退款
    var cardBack : Double = 0.0
    //备用金
    var spare : Double = 0.0
    //交班应缴现金
    var shiftPay : Double = 0.0
}