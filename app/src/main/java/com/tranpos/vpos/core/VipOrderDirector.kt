package com.tranpos.vpos.core


import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.entity.CardRechargeOrder
import com.tranpos.vpos.entity.Member
import com.tranpos.vpos.entity.RegistrationCode
import com.tranpos.vpos.entity.state.OrderPaymentStatusFlag
import com.tranpos.vpos.utils.*
import com.tranpos.vpos.utils.bus.MemberHelper
import com.transpos.tools.tputils.TPUtils

/**
 *CREATED BY AM
 *ON 2020/10/28
 *DESCRIPTION:
 **/
object VipOrderDirector {

    private val api = TPUtils.getObject(
        BaseApp.getApplication(), KeyConstrant.KEY_AUTH_REGISTER,
        RegistrationCode::class.java
    )

    var mRechargeOrder : CardRechargeOrder? = null

    fun createOrder(chargeAmount : Double,giftAmount : Double,giftPoint : Double){
        mRechargeOrder = CardRechargeOrder()
        val serialNumber = Tools.generateSerialNumber(4, KeyConstrant.KEY_SERIAL_ORDER)

        val tradeNo = "${api.storeNo}1${DateUtil.getNowDateStr("yyMMddHHmm")}${api.posNo}$serialNumber${StringKotlin.generateString(2)}"


        val member = TPUtils.getObject(BaseApp.getApplication(),KeyConstrant.KEY_MEMBER, Member::class.java)
        mRechargeOrder?.let {
            val orderObject = OrderManger.getOrderBean()
            it.id = IdWorkerUtils.nextId()
            it.tenantId = api.tenantId
            it.tradeNo = tradeNo
            it.voucherNo = orderObject?.tradeNo
            it.memberId = orderObject?.memberId
            it.memberName = orderObject?.memberName
            it.mobile = orderObject?.memberMobileNo
            it.cardNo = orderObject?.memberNo
            it.cardFaceNo = orderObject?.cardFaceNo
            it.payStatus = OrderPaymentStatusFlag.PAY_COMPLETE_STATE
            it.rechargeStatus = OrderPaymentStatusFlag.PAY_COMPLETE_STATE
            it.preAmount = if(member != null) MemberHelper.getBalance(member).toDouble() else 0.0
            it.chargeAmount = chargeAmount
            it.actualAmount = chargeAmount
            it.totalChargeAmount = chargeAmount
            it.giftAmount = giftAmount
            it.aftAmount = it.preAmount + chargeAmount
            it.prePoint = member?.totalPoint ?: 0.0
            it.giftPoint = giftPoint
            it.salesCode = orderObject?.salesCode
            it.salesName = orderObject?.salesName
            it.workerNo = orderObject?.workerNo
            it.workerName = orderObject?.workerName
            it.storeId = api.storeId
            it.storeNo = api.storeNo
            it.storeName = api.storeName
            it.posNo = api.posNo
            it.finishDate = DateUtil.getNowDateStr()
            it.shiftId = orderObject?.shiftId
            it.shiftNo = orderObject?.shiftNo
        }
    }

    fun clearOrder(){
        mRechargeOrder = null
        VipOrderPayDirector.clearPay()
    }

    fun getOrder() : CardRechargeOrder?{
        return mRechargeOrder
    }
}