package com.tranpos.vpos.entity

import com.tranpos.vpos.db.manger.OrderObjectDbManger
import com.tranpos.vpos.db.manger.OrderPayDbManger
import com.tranpos.vpos.core.OrderPayManger
import com.tranpos.vpos.utils.IdWorkerUtils
import com.tranpos.vpos.utils.StringKotlin

/**
 *CREATED BY AM
 *ON 2020/9/25
 *DESCRIPTION:
 **/
class ShiftUploadWrapper {

    class PayInfo{
        var id = IdWorkerUtils.nextId()
        var businessType : Int = BusinessTypeEnum.POS.type //业务类型
        var payModeNo : String = ""//收银方式编号
        var payModeName : String = ""//收银方式名称
        var quantity = 0 //支付笔数
        var amount = 0.0//支付金额
    }

    class  ShiftTicketCashUpload{
        var id = IdWorkerUtils.nextId()
        var consumeCash = 0.0 //消费现金收入
        var consumeCashRefund = 0.0 //消费现金退款
        var cardRechargeCash = 0.0 //储值卡现金充值
        var cardCashRefund = 0.0 // 储值卡现金退款
        var noTransCashIn = 0.0 //杂项现金收入
        var noTransCashOut = 0.0 //杂项现金支出
        var timesCashRecharge = 0.0 //计次项目现金充值
        var plusCashRecharge = 0.0 //plus会员购买现金
        var imprest = 0.0 //备用金
        var totalCash = 0.0 //应上交现金
    }

    fun builderPayInfos(worker: Worker,startTime : String,endTime : String) : List<PayInfo>{
        val infos = mutableListOf<PayInfo>()
        val payMap = OrderObjectDbManger.checkOrderByTime2(worker.no,startTime,endTime)
            .flatMap { OrderPayDbManger.findOderPayInfos(it.tradeNo) }
            .groupBy {
                it.payNo
            }
        for (entry in payMap) {
            val payInfo = PayInfo()
            val payList = entry.value

            payInfo.payModeNo = entry.key
            payInfo.payModeName = OrderPayManger.getPayName(entry.key) ?: ""
            payInfo.quantity = payList.size
            payInfo.amount = StringKotlin.formatPrice2(payList.fold(0.0){ acc, orderPay -> acc + orderPay.paidAmount })

            infos.add(payInfo)
        }
        return infos
    }

    fun builderCashDetail(worker: Worker,startTime : String,endTime : String) : ShiftTicketCashUpload {
        val bean = ShiftTicketCashUpload()
        val payList = OrderObjectDbManger.checkOrderByTime2(worker.no,startTime,endTime)
            .flatMap { OrderPayDbManger.findOderPayInfos(it.tradeNo) }
            .filter {
                it.payNo == OrderPayManger.PayModeEnum.PAYCASH.payNo
            }
        payList.forEach {
            if(it.paidAmount > 0)
                bean.consumeCash += StringKotlin.formatPrice2(it.paidAmount)
            else bean.consumeCashRefund -= StringKotlin.formatPrice2(it.paidAmount)
        }
        bean.totalCash = StringKotlin.formatPrice2(bean.consumeCash - bean.consumeCashRefund)

        return bean
    }

    /// <summary>
    /// 业务类型0-pos销售  1-微店销售  10-卡务
    /// </summary>
    enum class BusinessTypeEnum(val type : Int = 0){
        NONE(-1),
        POS(0),
        MICRO(1),
        CARD(10)
    }

}