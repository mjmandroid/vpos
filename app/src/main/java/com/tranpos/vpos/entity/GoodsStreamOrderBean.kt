package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/21
 *DESCRIPTION:
 **/
class GoodsStreamOrderBean(val order : OrderObject,val orderItems : List<OrderItem>,val pays : List<OrderPay>) {
    var isCheck = false
}