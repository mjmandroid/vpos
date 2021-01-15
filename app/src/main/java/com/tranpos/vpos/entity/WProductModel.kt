package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/7/22
 *DESCRIPTION:
 **/
class WProductModel {
    //商品条码标识
    var flag : String? = ""
    //商品PLU
    var plu : String? = ""
    //商品总价
    var price = 0.0
    //单价
    var singlePrice = 0.0
    //重量
    var kg = 0.0
    //校验码
    var crc : String? = ""
}