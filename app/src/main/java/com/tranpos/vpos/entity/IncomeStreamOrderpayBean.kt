package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/20
 *DESCRIPTION:
 **/
class IncomeStreamOrderpayBean(val orderPay: OrderPay) {
    /*整单金额*/
    var theOrderMoney = 0.0
    /*收银员*/
    var casher : String? = ""
    /*导购*/
    var sales : String? = ""
    /*会员卡号*/
    var memberNo : String? = ""
    /*会员名称*/
    var memberName : String? = ""
    /*会员手机号*/
    var phone : String? = ""
    /**销售方式**/
    var payWays : String? = ""
}