package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/9/16
 *DESCRIPTION:
 **/
class ShiftRecordOrderBean {
    var checked = false
    var orderNo : String? = ""
    var workerNo : String = ""
    var workerName : String = ""
    var startTime : String = ""
    var headTime : String = ""
    var tailTime : String = ""
    var shiftTime : String = ""
    var totalMoney : Double = 0.0
    var blindMony : Double = 0.0
    var spareMoney : Double = 0.0
    var detail : ShiftRecordDetail? = null
    var shiftLog : ShiftLog? = null

    class ShiftRecordDetail{
        var shop : String = ""
        var pos : String = ""
        var casher : String = ""
        var shiftTime : String = ""
        var headTime = ""
        var tailTime = ""
        var orderCount = ""

        //付款方式统计
        var payTotal = "0.00"
        //消费现金收入：
        var detailConsumeCash = "0.00"
        // 储值卡现金充值：
        var detailVipRecharge = "0.00"
        //购买Plus会员现金：
        var detailBuyPlus = "0.00"
        //计次项目现金充值：
        var detailPro = "0.00"
        //消费现金退款：
        var detailConsumeCashRejected = "0.00"
        //储值现金退款：
        var detailVipCashRejected = "0.00"
        // 备用金：
        var detailSpare = "0.00"
        //交班应缴现金汇总(人民币)：
        var cashCapture= "0.00"
        //支付方式
        val payList = mutableListOf<ShiftPayWaysBean>()
    }
}