package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/11/19
 *DESCRIPTION:
 **/
class VerificationBean(val orderObject: OrderObject) {
    var isChecked = false
    var tradeNo = "" //订单号
    var receivable = 0.0 //应收金额
    var time = "" //时间
    var payWay = "" //支付方式
    var payState = "" //支付状态
    var isUsed = false //是否使用
}