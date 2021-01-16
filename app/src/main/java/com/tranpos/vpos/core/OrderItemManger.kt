package com.tranpos.vpos.core

import android.text.TextUtils
import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.db.manger.MemberPointRuleDbManger
import com.tranpos.vpos.db.manger.MultipleQueryProductDbManger
import com.tranpos.vpos.db.manger.ProductContractDbManger
import com.tranpos.vpos.db.manger.PromotionTicketDbManger
import com.tranpos.vpos.entity.*
import com.tranpos.vpos.utils.DateUtil
import com.tranpos.vpos.utils.IdWorkerUtils
import com.tranpos.vpos.utils.KeyConstrant
import com.tranpos.vpos.utils.StringKotlin
import com.transpos.tools.GsonHelper
import com.transpos.tools.logger.Logger
import com.transpos.tools.tputils.TPUtils
import java.util.*


object OrderItemManger {

    private val orderItemList : MutableList<OrderItem> = mutableListOf()
    private var serialNum = 0
    private val mLock = Object()
    val promotionTicks = mutableListOf<PromotionTicket>()
    get() {
        synchronized(mLock){
            return field
        }
    }

    fun initPromotion(){
        synchronized(mLock){
            promotionTicks.clear()
            PromotionItemManger.clearData()
            promotionTicks.addAll(PromotionTicketDbManger.loadAll().filter {
                if(TextUtils.isEmpty(it.startTime)){
                    it.startTime = "00:00:00"
                }
                if(TextUtils.isEmpty(it.endTime)){
                    it.endTime = "23:59:59"
                }
                val startTime = "${it.startDate} ${it.startTime}"
                val endTime = "${it.endDate} ${it.endTime}"
                val isBetween = DateUtil.isBetween(startTime,endTime)
                var isValidWeek = true
                var isValidMonth = true
                var isValidMember = true
                if(it.onlyMemberFlag == 1 && !TextUtils.isEmpty(it.memberLevel)){
                    isValidMember = false
                    val member = TPUtils.getObject(BaseApp.getApplication(),
                        KeyConstrant.KEY_MEMBER, Member::class.java)
                    if(member != null){
                        val memberLevel = member.memberLevelNo
                        isValidMember = it.memberLevel.contains(memberLevel)
                    }
                }
                if(it.dayType == "W" && !TextUtils.isEmpty(it.validWeek)){
                    isValidWeek = false
                    it.validWeek.split(",").forEach {s->
                        if(s == DateUtil.getWeek(Date())){
                            isValidWeek = true
                        }
                    }
                }
                if(it.dayType == "M" && !TextUtils.isEmpty(it.validMonth)){
                    isValidMonth = false
                    it.validMonth.split(",").forEach { s->
                        if(s.toInt() == DateUtil.getCurrentDay().toInt()){
                            isValidMonth = true
                        }
                    }
                }

                isValidWeek && isValidMonth && isBetween && isValidMember
            })
        }
    }

    fun createItems(product : MultipleQueryProduct,
                    state: AddItemState,
                    position : Int,
                    member: Member?,
                    joinType : JoinTypeEnmu
    ) : OrderItem?{
        when(state){
            AddItemState.REMOVE -> {
                if(orderItemList.isNotEmpty()){
                    PromotionItemManger.removePromotion(orderItemList[position].id, null)
                    OrderItemPayManger.removeItems(orderItemList[position].id)
                    orderItemList.removeAt(position)
                }
            }
            AddItemState.MODIFY -> {
                if(position < 0 || position >= orderItemList.size) {
                    Logger.e("AddItemState.MODIFY throw IndexOutOfBoundsException $position list size=${orderItemList.size}")
                } else{
                    val item = orderItemList[position]
                    item.quantity = product.getmAmount().toDouble()
                    modifyBindProducts(item)
                }
            }
            AddItemState.ADD -> {
                val orderBean = OrderManger.getOrderBean()
                val item = OrderItem()
                orderBean?.let {
                    item.id = IdWorkerUtils.nextId()
                    item.tenantId = it.tenantId
                    item.orderId = it.id
                    item.tradeNo = it.tradeNo
                    item.orderNo = ++serialNum
                    item.productId = product.id
                    item.productName = product.name
                    item.shortName = product.shortName
                    item.specId = product.specId
                    item.specName = product.specName
                    item.displayName = product.name
                    item.quantity = product.getmAmount().toDouble()
//                        item.quantity = product.getmAmount().toDouble()
                    item.rquantity = 0.toDouble()
                    item.ramount = 0.toDouble()
                    item.orgItemId = product.no
                    item.salePrice = product.originPrice.toDouble()
                    item.price = product.originPrice.toDouble()
                    item.bargainReason = ""
//                        item.discountPrice = product.finalPrice.toDouble()
                    item.vipPrice = product.vipPrice.toDouble()
                    item.otherPrice = product.otherPrice.toDouble()
                    item.batchPrice = product.batchPrice.toDouble()
                    item.postPrice = product.postPrice.toDouble()
                    item.purPrice = product.purPrice.toDouble()
                    item.minPrice = product.minPrice.toDouble()
                    item.giftQuantity = 0.0
                    item.giftAmount = 0.0
                    item.giftReason = ""
                    item.flavorCount = 0
                    item.flavorNames = ""
                    item.flavorAmount = 0.0
                    item.flavorDiscountAmount = 0.0
                    item.flavorReceivableAmount = 0.0
                    item.amount = StringKotlin.formatPriceDouble(product.getmAmount() * product.originPrice.toFloat())
                    item.totalAmount = StringKotlin.formatPrice2(item.amount * item.quantity)
//                        item.discountAmount = StringUtils.formatPriceDouble((item.amount - item.discountPrice).toFloat())
//                        item.receivableAmount = product.finalPrice.toDouble()
//                        item.totalDiscountAmount = item.discountAmount
//                        item.totalReceivableAmount = item.receivableAmount
//                        item.discountRate = item.discountAmount / item.amount
//                        item.totalDiscountRate = item.discountRate
                    item.malingAmount = 0.0
                    item.remark = ""
                    item.saleDate = it.saleDate
                    item.finishDate = DateUtil.getNowDateStr()
                    item.cartDiscount = 0.0
                    item.underline = 1
                    item.group = item.id
                    item.parentId = ""
                    item.flavor = 0
                    item.scheme = ""
                    item.rowType = if(product.type == ProductTypeEnum.BIND.type) 2 else 1
                    item.suitId = ""
                    item.suitQuantity = 0.0
                    item.suitAddPrice = 0.0
                    item.suitAmount = 0.0
                    item.chuda = ""
                    item.chudaFlag = ""
                    item.chudaQty = 0.0
                    item.chupin = ""
                    item.chupinFlag = ""
                    item.chupinQty = 0.0
                    item.productType = product.type
                    item.barCode = product.barCode
                    item.subNo = product.subNo
                    item.batchNo = ""
                    item.productUnitId = product.unitId
                    item.productUnitName = product.unitName
                    item.categoryId = product.categoryId
                    item.categoryNo = product.categoryNo
                    item.categoryName = product.categoryName
                    item.brandId = product.brandId
                    item.brandName = product.brandName
                    item.foreDiscount = product.foreDiscount
                    item.weightFlag = product.weightFlag
                    item.weightWay = product.weightWay
                    item.foreBargain = product.foreBargain
                    item.pointFlag = product.pointFlag
                    item.pointValue = product.pointValue
                    item.foreGift = product.foreGift
                    item.promotionFlag = product.promotionFlag
                    item.stockFlag = product.stockFlag
                    item.batchStockFlag = product.batchStockFlag
                    item.labelPrintFlag = product.labelPrintFlag
                    item.labelQty = 0.0
                    item.purchaseTax = product.purchaseTax
                    item.saleTax = product.saleTax
                    item.supplierId = product.supplierId
                    item.supplierName = product.supplierName
                    item.managerType = product.managerType
                    item.salesCode = ""
                    item.salesName = ""
                    item.itemSource = it.orderSource
                    item.posNo = it.posNo
                    item.addPoint = calcPoint(member,product.finalPrice.toFloat()).toDouble()
                    item.refundPoint = 0.0
                    item.promotionInfo = product.promotionInfo
                    item.createUser = it.createUser
                    item.createDate = DateUtil.getNowDateStr(DateUtil.SIMPLE_FORMAT)
                    item.modifyUser = it.modifyUser
                    item.modifyDate = item.createDate
                    item.lyRate = product.lyRate
                    item.chuDaLabel = ""
                    item.chuDaLabelFlag = ""
                    item.chuDaLabelQty = 0.0
                    item.shareCouponLeastCost = 0.0
                    item.couponAmount = 0.0
//                        item.totalReceivableRemoveCouponAmount = item.receivableAmount
//                        item.totalReceivableRemoveCouponLeastCost = item.receivableAmount
                    item.joinType = joinType.type
                    item.labelAmount = 0.0
                    item.isPlusPrice = 0
                    item.showBarcode = product.ext1
                    item.ext1 = "0"

                    orderItemList.add(item)
                    //创建分摊明细
                    /***现金***/
                    OrderItemPayManger.createItemPay(
                        item.id,
                        OrderPayManger.PayModeEnum.PAYCASH.payNo,
                        item
                    )
                    /***支付宝***/
                    OrderItemPayManger.createItemPay(
                        item.id,
                        OrderPayManger.PayModeEnum.PAYAIL.payNo,
                        item
                    )
                    /***微信***/
                    OrderItemPayManger.createItemPay(
                        item.id,
                        OrderPayManger.PayModeEnum.PAYWX.payNo,
                        item
                    )
                    /***会员***/
                    OrderItemPayManger.createItemPay(
                        item.id,
                        OrderPayManger.PayModeEnum.PAYMEMBER.payNo,
                        item
                    )
                    if(product.type == ProductTypeEnum.BIND.type){
                        createBindProducts(item)
                    }
                    return item
                }

            }
            else -> {

            }
        }
        return null
    }

    /**
     * 修改数量
     */
    private fun modifyBindProducts(mainItem: OrderItem) {
        if(!TextUtils.isEmpty(mainItem.bindProducts)){
            val binds =
                    GsonHelper.fromJsonToList(mainItem.bindProducts, Array<OrderItem>::class.java)
            binds.forEach {
                item ->
                item.quantity = mainItem.quantity * item.slaveNum
                item.amount = StringKotlin.formatPriceDouble(item.quantity * item.salePrice)
                item.totalAmount = StringKotlin.formatPrice2(item.amount * item.quantity)
                item.amount = StringKotlin.formatPriceDouble(item.quantity * item.salePrice)
                item.discountPrice = item.salePrice * item.quantity
                item.receivableAmount = item.salePrice * item.quantity
                item.discountAmount =
                        StringKotlin.formatPriceDouble((item.amount - item.discountPrice).toFloat())
                item.totalDiscountAmount = item.discountAmount
                item.totalReceivableAmount = item.receivableAmount
                item.discountRate = if (item.amount != 0.0) item.discountAmount / item.amount else 0.0
                item.totalDiscountRate = item.discountRate
                item.totalReceivableRemoveCouponAmount = item.receivableAmount
                item.totalReceivableRemoveCouponLeastCost = item.receivableAmount
                item.price = item.salePrice
                item.totalAmount = StringKotlin.formatPriceDouble(item.quantity * item.salePrice)
            }
            mainItem.bindProducts = GsonHelper.toJson(binds)
        }
    }

    fun calcPoint(
        member: Member?,
        totalPrice: Float
    ): Float {
        var p = 0
        if (member != null) {
            val memberLevelId = member.memberLevelId
            val pointRules =
                MemberPointRuleDbManger.getInstance().loadAll()
            var memberPointRule: MemberPointRule? = null
            if (pointRules != null && pointRules.size > 0) {
                for (rule in pointRules) {
                    if (rule.levelId == memberLevelId) {
                        memberPointRule = rule
                        break
                    }
                }
            }
            if (memberPointRule != null) {
                if (memberPointRule.pointType == 1) {
                    p = (totalPrice * memberPointRule.rate).toInt()
                }
            }
        }
        return p * 1.0f
    }

    fun clear(){
        PromotionItemManger.clearData()
        OrderItemPayManger.clearData()
        orderItemList.clear()
    }

    @Synchronized
    fun getList() : MutableList<OrderItem>{
        return orderItemList
    }


    enum class AddItemState{
        ADD,REMOVE,MODIFY,NONE
    }

    private fun createBindProducts(orderItem: OrderItem) {
        val binds = ProductContractDbManger.getInstance().findBindProducts(orderItem.specId)
        if(binds.isNotEmpty()){
            val orderItemBindList = mutableListOf<OrderItem>()
            binds.forEach {
                c ->
                val product = MultipleQueryProductDbManger.queryOneProductById(c.slaveSpecId)
                if(product != null){
                    product.setmAmount((c.slaveNum * orderItem.quantity ).toFloat())

                    val orderBean = OrderManger.getOrderBean()
                    val item = OrderItem()
                    item.slaveNum = c.slaveNum
                    orderBean?.let {
                        item.id = IdWorkerUtils.nextId()
                        item.tenantId = it.tenantId
                        item.orderId = it.id
                        item.tradeNo = it.tradeNo
                        item.orderNo = ++serialNum
                        item.productId = product.id
                        item.productName = product.name
                        item.shortName = product.shortName
                        item.specId = product.specId
                        item.specName = product.specName
                        item.displayName = product.name
                        item.quantity = product.getmAmount().toDouble()
//                        item.quantity = product.getmAmount().toDouble()
                        item.rquantity = 0.toDouble()
                        item.ramount = 0.toDouble()
                        item.orgItemId = product.no
                        item.salePrice = product.originPrice.toDouble()
                        item.price = product.originPrice.toDouble()
                        item.bargainReason = ""
//                        item.discountPrice = product.finalPrice.toDouble()
                        item.vipPrice = product.vipPrice.toDouble()
                        item.otherPrice = product.otherPrice.toDouble()
                        item.batchPrice = product.batchPrice.toDouble()
                        item.postPrice = product.postPrice.toDouble()
                        item.purPrice = product.purPrice.toDouble()
                        item.minPrice = product.minPrice.toDouble()
                        item.giftQuantity = 0.0
                        item.giftAmount = 0.0
                        item.giftReason = ""
                        item.flavorCount = 0
                        item.flavorNames = ""
                        item.flavorAmount = 0.0
                        item.flavorDiscountAmount = 0.0
                        item.flavorReceivableAmount = 0.0
                        item.amount = StringKotlin.formatPriceDouble(product.getmAmount() * product.originPrice.toFloat())
                        item.totalAmount = StringKotlin.formatPrice2(item.amount * item.quantity)
//                        item.discountAmount = StringUtils.formatPriceDouble((item.amount - item.discountPrice).toFloat())
//                        item.receivableAmount = product.finalPrice.toDouble()
//                        item.totalDiscountAmount = item.discountAmount
//                        item.totalReceivableAmount = item.receivableAmount
//                        item.discountRate = item.discountAmount / item.amount
//                        item.totalDiscountRate = item.discountRate
                        item.malingAmount = 0.0
                        item.remark = ""
                        item.saleDate = it.saleDate
                        item.finishDate = DateUtil.getNowDateStr()
                        item.cartDiscount = 0.0
                        item.underline = 1
                        item.group = item.id
                        item.parentId = ""
                        item.flavor = 0
                        item.scheme = ""
                        item.rowType = 3
                        item.suitId = ""
                        item.suitQuantity = 0.0
                        item.suitAddPrice = 0.0
                        item.suitAmount = 0.0
                        item.chuda = ""
                        item.chudaFlag = ""
                        item.chudaQty = 0.0
                        item.chupin = ""
                        item.chupinFlag = ""
                        item.chupinQty = 0.0
                        item.productType = product.type
                        item.barCode = product.barCode
                        item.subNo = product.subNo
                        item.batchNo = ""
                        item.productUnitId = product.unitId
                        item.productUnitName = product.unitName
                        item.categoryId = product.categoryId
                        item.categoryNo = product.categoryNo
                        item.categoryName = product.categoryName
                        item.brandId = product.brandId
                        item.brandName = product.brandName
                        item.foreDiscount = product.foreDiscount
                        item.weightFlag = product.weightFlag
                        item.weightWay = product.weightWay
                        item.foreBargain = product.foreBargain
                        item.pointFlag = product.pointFlag
                        item.pointValue = product.pointValue
                        item.foreGift = product.foreGift
                        item.promotionFlag = product.promotionFlag
                        item.stockFlag = product.stockFlag
                        item.batchStockFlag = product.batchStockFlag
                        item.labelPrintFlag = product.labelPrintFlag
                        item.labelQty = 0.0
                        item.purchaseTax = product.purchaseTax
                        item.saleTax = product.saleTax
                        item.supplierId = product.supplierId
                        item.supplierName = product.supplierName
                        item.managerType = product.managerType
                        item.salesCode = ""
                        item.salesName = ""
                        item.itemSource = it.orderSource
                        item.posNo = it.posNo
                        item.addPoint = 0.0
                        item.refundPoint = 0.0
                        item.promotionInfo = product.promotionInfo
                        item.createUser = it.createUser
                        item.createDate = DateUtil.getNowDateStr(DateUtil.SIMPLE_FORMAT)
                        item.modifyUser = it.modifyUser
                        item.modifyDate = item.createDate
                        item.lyRate = product.lyRate
                        item.chuDaLabel = ""
                        item.chuDaLabelFlag = ""
                        item.chuDaLabelQty = 0.0
                        item.shareCouponLeastCost = 0.0
                        item.couponAmount = 0.0
//                        item.totalReceivableRemoveCouponAmount = item.receivableAmount
//                        item.totalReceivableRemoveCouponLeastCost = item.receivableAmount
                        item.joinType = JoinTypeEnmu.SCANCODE.type
                        item.labelAmount = 0.0
                        item.isPlusPrice = 0

                        item.amount = StringKotlin.formatPriceDouble(product.getmAmount() * product.originPrice.toFloat())
                        item.discountPrice = product.originPrice.toDouble()
                        item.receivableAmount = product.originPrice.toDouble()
                        item.discountAmount =
                                StringKotlin.formatPriceDouble((item.amount - item.discountPrice).toFloat())
                        item.totalDiscountAmount = item.discountAmount
                        item.totalReceivableAmount = item.receivableAmount
                        item.discountRate = if (item.amount != 0.0) item.discountAmount / item.amount else 0.0
                        item.totalDiscountRate = item.discountRate
                        item.totalReceivableRemoveCouponAmount = item.receivableAmount
                        item.totalReceivableRemoveCouponLeastCost = item.receivableAmount
                        item.quantity = product.getmAmount().toDouble()
                        item.promotionFlag = if(product.isPromotionFlags) 1 else 0
                        item.price = StringKotlin.formatPrice2(product.salePrice.toDouble())
                        item.totalAmount = StringKotlin.formatPriceDouble(product.getmAmount() * product.originPrice.toFloat())

                    }
                    orderItemBindList.add(item)
                }
            }
            orderItem.bindProducts = GsonHelper.toJson(orderItemBindList)
        }
    }

    /**
     * @description:
     * @param:          设置赠送促销数据
     * @return:
     * @author:         PC-202001
     * @time:           2020/12/17 15:51
     */
    fun setGiftData(item : OrderItem) {
        if(item.promotionsJson.isNullOrBlank()){
            val giftList = arrayListOf(
                PromotionItemManger.createGiftPromotion(item.id, "gift", item)
            )
            item.promotionsJson = GsonHelper.toJson(giftList)
        }
    }

    /**
     * 商品类型 0-普通商品；1-可拆零商品；2-捆绑商品；3-自动转货；
     */
    enum class ProductTypeEnum(val type : Int){
        NORMALL(0),
        SPLIT(1),
        BIND(2),
        AUTO(3),
        NONE(-1)
    }

    /// 订单项加入方式
    //        NONE = -1,
    //        触摸点击 = 0,
    //        扫描条码 = 1,
    //        扫描金额码 = 2,
    //        扫描数量码 = 3,
    //        系统自动添加 = 4, //促销赠送
    enum class JoinTypeEnmu(val type: Int){

        NONE(-1),
        TOUCH(0),
        SCANCODE(1),
        SCANMONEY(2),
        SCANAMOUNT(3),
        SYSTEMADD(4)
    }
}


