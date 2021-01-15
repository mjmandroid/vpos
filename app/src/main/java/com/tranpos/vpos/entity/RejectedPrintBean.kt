package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/6
 *DESCRIPTION:
 **/
class RejectedPrintBean {
    var tradeNo = ""
    var saleName = ""
    var saleCode = ""
    var items : List<OrderItem>? = null
    var paidMoney = "0.0"
}