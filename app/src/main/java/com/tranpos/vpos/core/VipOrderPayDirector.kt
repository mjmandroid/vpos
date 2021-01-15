package com.tranpos.vpos.core


import com.tranpos.vpos.entity.CardRechargeOrderPay
import com.tranpos.vpos.entity.state.OrderPaymentStatusFlag
import com.tranpos.vpos.utils.DateUtil
import com.tranpos.vpos.utils.IdWorkerUtils

/**
 *CREATED BY AM
 *ON 2020/10/28
 *DESCRIPTION:
 **/
object VipOrderPayDirector {

    private val mPayMap = mutableMapOf<String, CardRechargeOrderPay>()

    fun createPay(no : String,chargeAmount : Double){
        VipOrderDirector.getOrder()?.let {
            order ->
            val pay = CardRechargeOrderPay()
            pay.id = IdWorkerUtils.nextId()
            pay.tenantId = order.tenantId
            pay.tradeNo = order.tradeNo
            pay.orderId = pay.id
            pay.no = no
            pay.name = OrderPayManger.getPayName(no)
            pay.amount = chargeAmount
            pay.paidAmount = chargeAmount
            pay.payNo = IdWorkerUtils.nextId()
            pay.status = OrderPaymentStatusFlag.PAY_COMPLETE_STATE
            pay.payTime = DateUtil.getNowDateStr()
            pay.finishDate = pay.payTime
            pay.incomeFlag = 1
            pay.shiftId = order.shiftId
            pay.shiftName = OrderManger.getOrderBean()?.shiftName
            pay.shiftNo = order.shiftNo

            mPayMap[no] = pay
        }
    }

    fun getOrderPay(no : String) : CardRechargeOrderPay?{
        return mPayMap[no]
    }

    fun clearPay(){
        mPayMap.clear()
    }
}