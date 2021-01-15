package com.tranpos.vpos.entity

import com.transpos.tools.GsonHelper


/**
 *CREATED BY AM
 *ON 2020/7/21
 *DESCRIPTION:构建orderItems参数
 **/
class OrderItemsBuilder {
    var clientId : String? = ""
    var clientOrderId : String? = ""
    var tradeNo : String? = ""
    var storeId : String? = ""
    var storeNo : String? = ""
    var storeName : String? = ""
    var orderNo : String? = ""
    var productId : String? = ""
    var productName : String? = ""
    var shortName : String? = ""
    var specId : String? = ""
    var specName : String? = ""
    var quantity = 0.0
    var rquantity = 0.0
    var ramount = 0.0
    var salePrice = 0.0
    var price = 0.0
    var bargainReason : String? = ""
    var discountPrice = 0.0
    var vipPrice = 0.0
    var otherPrice = 0.0
    var minPrice = 0.0
    var costPrice = 0.0
    var giftQuantity = 0.0
    var giftAmount = 0.0
    var giftReason : String? = ""
    var flavorAmount = 0.0
    var flavorDiscountAmount = 0.0
    var flavorReceivableAmount = 0.0
    var flavorNames : String? = ""
    var amount = 0.0
    var totalAmount = 0.0
    var discountAmount = 0.0
    var receivableAmount = 0.0
    var totalDiscountAmount = 0.0
    var totalReceivableAmount = 0.0
    var discountRate = 0.0
    var totalDiscountRate = 0.0
    var saleDate : String? = ""
    var finishDate : String? = ""
    var remark : String? = ""
    var itemSource = -1
    var posNo : String? = ""
    var groupId : String? = ""
    var parentId : String? = ""
    var scheme : String? = ""
    var rowType = 0
    var suitId : String? = ""
    var suitQuantity = 0.0
    var suitAddPrice = 0.0
    var suitAmount = 0.0
    var productType = 0
    var barCode : String? = ""
    var subNo : String? = ""
    var batchNo : String? = ""
    var productUnitId : String? = ""
    var productUnitName : String? = ""
    var categoryId : String? = ""
    var categoryName : String? = ""
    var brandId : String? = ""
    var brandName : String? = ""
    var weightFlag = 0
    var weightWay = 0
    var pointFlag = 0
    var pointValue = 0.0
    var stockFlag = 0
    var batchStockFlag = 0
    var purchaseTax = 0.0
    var saleTax = 0.0
    var supplierId : String? = ""
    var supplierName : String? = ""
    var managerType : String? = ""
    var salesCode : String? = ""
    var salesName : String? = ""
    var profitAmount = 0.0
    var profitRate = 0.0
    var costAmount = 0.0
    var zkStatus = 0
    var stockStatus = 0
    var lyRate = 0.0
    var addPoint = 0.0
    var refundPoint = 0.0
    var orgItemId : String? = ""
    var joinType = 0
    var labelAmount = 0.0
    var shareCouponLeastCost = 0.0
    var couponAmount = 0.0
    var totalReceivableRemoveCouponAmount = 0.0
    var totalReceivableRemoveCouponLeastCost = 0.0
    var isPlusPrice = 0
    var promotionInfo : String? = ""
    var makes = mutableListOf<Any>()
    var promotions = mutableListOf<PromotionItem>()
    var itemPays = mutableListOf<OrderItemPayBuilder>()

    fun buildOrderItemPay(pays : List<OrderItemPay>,orderItem : OrderItemsBuilder) : MutableList<OrderItemPayBuilder> {
        val itemPays = mutableListOf<OrderItemPayBuilder>()
        pays.forEach {
            val item = OrderItemPayBuilder()
            item.clientId = orderItem.clientId
            item.clientOrderId = it.orderId
            item.tradeNo = orderItem.tradeNo
            item.clientOrderItemId = it.itemId
            item.no = it.no
            item.name = it.name
            item.storeId = orderItem.storeId
            item.storeName = orderItem.storeName
            item.productId = orderItem.productId
            item.productName = orderItem.productName
            item.specId = orderItem.specId
            item.specName = orderItem.specName
            item.couponId = it.couponId
            item.couponNo = it.couponNo
            item.faceAmount = it.faceAmount
            item.shareAmount = it.shareAmount
            item.shareCouponLeastCost = it.shareCouponLeastCost
            item.refundAmount = it.ramount
            item.finishDate = it.finishDate
            itemPays.add(item)
        }
        return itemPays
    }

    fun buildPromotions(promotionsJson: String): MutableList<PromotionItem> {
        val pList = mutableListOf<PromotionItem>()
        pList.addAll(GsonHelper.fromJsonToList(promotionsJson,Array<PromotionItem>::class.java))
        return pList
    }
}