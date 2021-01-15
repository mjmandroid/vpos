package com.tranpos.vpos.core

import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.entity.Member
import com.tranpos.vpos.entity.OrderPay
import com.tranpos.vpos.entity.state.OrderPaymentStatusFlag
import com.tranpos.vpos.entity.state.PayChannelFlag
import com.tranpos.vpos.utils.DateUtil
import com.tranpos.vpos.utils.IdWorkerUtils
import com.tranpos.vpos.utils.KeyConstrant
import com.tranpos.vpos.db.manger.OrderPayDbManger
import com.transpos.tools.tputils.TPUtils

object OrderPayManger {

    lateinit var payInfo: OrderPay
    private var serialNum = 0
    private val map = mutableMapOf<String,OrderPay>()

    fun createPayInfo() {
        payInfo = OrderPay()
        var orderBean = OrderManger.getOrderBean()
        if(orderBean == null){
            OrderManger.initalize()
        }
        orderBean = OrderManger.getOrderBean()
        orderBean?.let {
            payInfo.id = IdWorkerUtils.nextId()
            payInfo.tenantId = it.tenantId
            payInfo.tradeNo = it.tradeNo
            payInfo.orderId = it.id
            payInfo.orgPayId = ""
            payInfo.orderNo = ++serialNum
            //payInfo.no  lateinit
            //payInfo.name lateinit
            payInfo.amount = it.amount
            payInfo.inputAmount = 0.toDouble()
            payInfo.faceAmount = 0.toDouble()
           // payInfo.paidAmount = it.amount
            payInfo.ramount = 0.toDouble()
            payInfo.changeAmount = 0.toDouble()
            payInfo.platformDiscount = 0.toDouble()
            payInfo.platformPaid = 0.toDouble()
            payInfo.payNo = ""
            payInfo.channelNo = ""
            payInfo.voucherNo = ""
            payInfo.status = OrderPaymentStatusFlag.NO_PAY_STATE //lateinit
            payInfo.statusDesc = ""  // lateinit
            payInfo.payTime = ""  // lateinit
            payInfo.finishDate = ""  //lateinit
            payInfo.payChannel = PayChannelFlag.NATIVE_PAY //lateinit
            payInfo.incomeFlag = 1
            payInfo.pointFlag = 1
            payInfo.subscribe = ""
            payInfo.useConfirmed = 0 // lateinit
            payInfo.accountName = ""
            payInfo.bankType = ""
            payInfo.memo = ""
            payInfo.cardNo = ""
            payInfo.cardPreAmount = 0.toDouble()
            payInfo.cardChangeAmount = 0.toDouble()
            payInfo.cardAftAmount = 0.toDouble()
            payInfo.cardPrePoint = 0.toDouble()
            payInfo.cardChangePoint = 0.toDouble()
            payInfo.cardAftPoint = 0.toDouble()

            payInfo.shiftId = orderBean.shiftId
            payInfo.shiftName = orderBean.shiftName
            payInfo.shiftNo = orderBean.shiftNo
            payInfo.createUser = orderBean.createUser
            payInfo.createDate = DateUtil.getNowDateStr(DateUtil.SIMPLE_FORMAT)
            payInfo.modifyUser = orderBean.modifyUser
            payInfo.modifyDate = DateUtil.getNowDateStr(DateUtil.SIMPLE_FORMAT)
            payInfo.sourceSign = ""

        }

    }

    fun clear() {
        map.clear()
    }

    /**
     * key = payNo
     */
    fun putInfo(key : String,b : OrderPay){
        map[key] = b
    }

    fun putAndCreateInfo(value : Double,payMode: PayModeEnum){
        createPayInfo()
        payInfo.payNo = payMode.payNo
        payInfo.no = payMode.payNo
        payInfo.name = payMode.payName
        payInfo.finishDate = (DateUtil.getNowDateStr(DateUtil.SIMPLE_FORMAT))
        payInfo.payTime = DateUtil.getNowDateStr(DateUtil.SIMPLE_FORMAT)
        payInfo.status = (OrderPaymentStatusFlag.PAY_COMPLETE_STATE)
        payInfo.paidAmount = value //实收金额
        when(payInfo.payNo){
            "01","02","05","04" ->{
                TPUtils.getObject(BaseApp.getApplication(), KeyConstrant.KEY_MEMBER, Member::class.java).takeIf {
                    it != null
                }?.let {
                    payInfo.memberMobileNo = it.mobile
                    payInfo.memberId = it.id
                    payInfo.memberName = it.name
                }
            }
        }
        map[payMode.payNo] = payInfo
    }

    fun getPayInfo(key : String) = map[key]

    fun getAllPayInfo() = map

    fun removePayInfo(key : String){
        map.remove(key)
    }

    fun setMemberInfo(member: Member?) {
//        member?.run {
//            payInfo.memberMobileNo = this.mobile
//            payInfo.cardFaceNo = ""
//            payInfo.pointAmountRate = 0.toDouble()
//            OrderPayDbManger.update(payInfo)
//        }
    }
    fun insertOrderPay(value : Double,payMode: PayModeEnum){
        createPayInfo()
        payInfo.payNo = payMode.payNo
        payInfo.no = payMode.payNo
        payInfo.name = payMode.payName
        payInfo.finishDate = (DateUtil.getNowDateStr(DateUtil.SIMPLE_FORMAT))
        payInfo.payTime = DateUtil.getNowDateStr(DateUtil.SIMPLE_FORMAT)
        payInfo.status = (OrderPaymentStatusFlag.PAY_COMPLETE_STATE)
        payInfo.paidAmount = value //实收金额
        when(payInfo.payNo){
            "01","02","05","04" ->{
                TPUtils.getObject(BaseApp.getApplication(),KeyConstrant.KEY_MEMBER,Member::class.java).takeIf {
                    it != null
                }?.let {
                    payInfo.memberMobileNo = it.mobile
                    payInfo.memberId = it.id
                    payInfo.memberName = it.name
                }
            }
        }
        map[payMode.payNo] = payInfo
        OrderPayDbManger.insert(payInfo)
    }

    enum class PayModeEnum(val payNo: String, val payName: String) {
        NONE("",""),
        PAYCASH("01", "人民币"),
        PAYMEMBER("02", "储值卡"),
        PAYWX("05", "微信"),
        PAYAIL("04", "支付宝"),
        PAYCOUPON("07", "代金券"),
        MOLING("06", "抹零"),

        //以下三个 payNo待定
        TRANPOS("11","传果pay"),
        UNION("03","银行卡"),
        PAYPOINT("08","积分付款"),
        PAYCLOUD("09","银联云闪付"),
        OTHER("50","其他")
    }

    fun getPayType(payNo: String) : PayModeEnum?{
        return PayModeEnum.values().find {
            it.payNo == payNo
        }
    }

    fun getPayName(payNo: String) : String?{
        val pay = PayModeEnum.values().find {
            it.payNo == payNo
        }
        return pay?.payName
    }

}