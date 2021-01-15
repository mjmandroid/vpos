package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/11/4
 *DESCRIPTION:
 **/
class CashKey {
    var checked = false
    var id = 0
    var name : String = ""

    companion object{
        const val MODIFY_PRICE = 1 // 改价
        const val GIFT = 2 // 赠送
        const val WEIGHT = 3 //称重
        const val ROUND = 4 //凑整
        const val CLEAR = 5 // 清零
        const val LAST_WEIGHT = 6 //连续称重
        const val REMOE_SKIN = 7 //去皮
        const val GUIDE_SALES = 8 // 导购
        const val MORE_VERTICAL = 9// 导购
        const val DELETE_ITEM = 10// 导购
        //********************************
        const val ASK_GOODS = 19 // 门店要货
        const val NEW_APPLICATION = 20 //新品要货
        const val PRE_PACKAGE = 21 // 预包装
        const val PRE_PRODUCT = 22 //预定商品
        const val DEPOSIT_PRODUCT = 23 // 商品寄存
        const val DEPOSIT_TAKE = 24 // 寄存取货
        const val ADJUST_PRICE = 25 // 调价
        const val QUICKLY_CHECK = 26 // 盘点
        const val CUSTOMER_REFUND = 27 // 顾客退货
        const val DEAL_QUERY = 28 //查询交易
        const val TICKET_PRINT = 29 // 打印小票
        const val OPEN_BOX = 30 // 开钱箱
        const val MORE_HORIZONTAL = 31 //更多功能
        const val PRODUCT_QUERY = 32 //商品查询
        const val NEW_PRODUCT = 33  //新增商品
        const val LOCK_SCREEN = 34   //锁屏
        const val PAY_CHECK = 35 // 支付核销
        const val PRE_ACCOUNT = 36 // 预定结算
        const val PRINT_CHANGE = 37 // 打印切换
        const val PRINT_LABEL = 38 // 打印标签
        const val F1_KEY = 39 // F1
    }
}