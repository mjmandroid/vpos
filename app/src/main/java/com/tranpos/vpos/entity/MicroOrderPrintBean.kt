package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/6
 *DESCRIPTION:
 **/
class MicroOrderPrintBean {
    var tradeNo = ""
    var createDate = ""
    var deliveryDate = ""
    var postWay = ""
    var remark = ""
    var items: List<OrderItem>? = null
    var amount = "0.0"
    var freightAmount = "0.0"
    var paidAmount = "0.0"
    var discountAmount = "0.0"
    var name = ""
    var phone = ""
    var address = ""
    var itemCount=""
}
