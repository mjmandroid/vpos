package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/24
 *DESCRIPTION:
 **/
class ChartGoodsTotalBean {
    var id = ""
    //编码
    var code : String? = null
    //自编码
    var subCode : String? = null
    var productName : String? = null
    //销售数量
    var saleCount = 0.0
    //退货数量
    var rSaleCount = 0.0
    //赠送数量
    var giftCount = 0.0
    //小计数量
    var totalCount = 0.0
    //原价
    var originPrice = 0.0
    //售价
    var salePrice = 0.0
    //优惠金额
    var reduceMoney = 0.0
    //退货金额
    var rMoney = 0.0
    //小计金额
    var totalMoney = 0.0
    //赠送成本金额
    var giftMoney = 0.0
}

class ChartCategoryTotalBean{
    var cid = ""
    //编码
    var code : String? = null
    //类别名称
    var categoryName : String? = null
    //销售数量
    var saleCount = 0.0
    //退货数量
    var rSaleCount = 0.0
    //赠送数量
    var giftCount = 0.0
    //小计数量
    var totalCount = 0.0
    //原价
    var originPrice = 0.0
    //售价
    var salePrice = 0.0
    //优惠金额
    var reduceMoney = 0.0
    //退货金额
    var rMoney = 0.0
    //小计金额
    var totalMoney = 0.0
    //赠送成本金额
    var giftMoney = 0.0
}

class ChartBrandTotalBean{
    var bid = ""
    //品牌名称
    var brandName : String? = null
    //销售数量
    var saleCount = 0.0
    //退货数量
    var rSaleCount = 0.0
    //赠送数量
    var giftCount = 0.0
    //小计数量
    var totalCount = 0.0
    //原价
    var originPrice = 0.0
    //售价
    var salePrice = 0.0
    //优惠金额
    var reduceMoney = 0.0
    //退货金额
    var rMoney = 0.0
    //小计金额
    var totalMoney = 0.0
    //赠送成本金额
    var giftMoney = 0.0
}