package com.tranpos.vpos.db.manger

import android.text.TextUtils
import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.core.OrderManger
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.OrderObjectDao
import com.tranpos.vpos.entity.CashParamterInfo
import com.tranpos.vpos.entity.OrderObject
import com.tranpos.vpos.entity.Worker
import com.tranpos.vpos.entity.state.OrderStatusFlag
import com.tranpos.vpos.entity.state.ValueSizeFlag
import com.tranpos.vpos.utils.DateUtil
import com.tranpos.vpos.utils.KeyConstrant

import com.transpos.tools.tputils.TPUtils

object OrderObjectDbManger : BaseDBManager<OrderObject>() {

    override fun getDataBaseDao(): BaseDao<OrderObject> {
        return AppDatabase.getDatabase().orderObjectDao
    }

    override fun deleteAll(): Int {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<OrderObject> {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.loadAll()
    }

    override fun update(bean: OrderObject?): Int {
        TPUtils.getObject(BaseApp.getApplication(), KeyConstrant.KEY_CASH_PARAMETER,
            CashParamterInfo::class.java).takeIf {
            it != null
        }?.let {
            if(it.isPracticeMode){
                OrderManger.getOrderBean()?.orderStatus = OrderStatusFlag.PRACTICE_MODE
            }
        } ?: kotlin.run {
            OrderManger.getOrderBean()?.orderStatus = OrderStatusFlag.PRACTICE_MODE
        }
        if(bean != null && (bean.orderStatus == OrderStatusFlag.COMPLETE_STATE
                    || bean.orderStatus == OrderStatusFlag.PAY_STATE
                    || bean.orderStatus == OrderStatusFlag.RETURN_STATE)){
            //更新交班信息
            TPUtils.getObject(BaseApp.getApplication(),KeyConstrant.KEY_WORKER, Worker::class.java).let {
                val shift = ShiftLogDbManger.getNotShift(it.id)
                if(shift != null){
                    if(TextUtils.isEmpty(shift.firstDealTime)){
                        shift.firstDealTime = bean.finishDate
                    }
                    if(DateUtil.compareTime(shift.startTime,shift.firstDealTime) == ValueSizeFlag.GREATER){
                        shift.firstDealTime = DateUtil.getNowDateStr()
                    }
                    shift.endDealTime = bean.finishDate
                    ShiftLogDbManger.update(shift)
                }
            }
        }

        return super.update(bean)
    }

    fun checkOrder(tradeNo: String): OrderObject? {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.checkOrderByNo(tradeNo)
    }

    fun checkPreviousOrder(): OrderObject? {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        val orders = dao.checkPreviousOrders()
        return if (orders.isEmpty()) {
            null
        } else {
            orders.last()
        }
    }

    fun deleteOrder(tradeNo: String): Int {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.deleteOrder(tradeNo)
    }

    fun deletePracticeOrder(): Int {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        val targetList = dao.checkPracticeOrder()
        targetList.forEach {
            //删除orderPay
            OrderPayDbManger.deletePractice(it.tradeNo)
            //删除orderItem
            OrderItemDbManger.deleteItems(it.tradeNo)
            //删除orderItemPay
            OrderItemPayDbManger.deletePracticeItems(it.tradeNo)
        }
        return dao.deletePracticeOrder()
    }

    fun deleteEmpeyOrder(): Int {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.deleteEmptyOrder("${DateUtil.getYesterdayDate()} 00:00:00")
    }

    fun checkOrderByTime(workerNo: String, startTime: String, endTime: String): List<OrderObject> {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.checkOrderByTime(workerNo, startTime)
    }

    fun checkOrderByTime2(workerNo: String, startTime: String, endTime: String): List<OrderObject> {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.checkOrderByTime(workerNo, startTime,endTime)
    }

    //查找挂单
    fun checkHangOrdrs(): List<OrderObject> {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.checkHangOrders()
    }

    //查找昨天的订单
    fun checkYesterdayOrders(
        workerNo: String,
        startTime: String,
        endTime: String
    ): List<OrderObject> {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.checkYesterdayOrders(workerNo, startTime, endTime)
    }

    //查找收银流水订单
    fun checkIncomeStreamOrders(startTime: String, endTime: String): List<OrderObject> {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.checkIncomeStreamOrders(startTime, endTime)
    }

    //查找商品销售流水
    fun checkGoodsSaleOrders(startTime: String, endTime: String): List<OrderObject> {
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.checkIncomeStreamOrders(startTime, endTime)
    }
    //查找核销的订单
    fun checkVerificationOrders() : List<OrderObject>{
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.checkVerificationOrders().filter {
            it.orderStatus != OrderStatusFlag.RETURN_STATE
        }
    }

    fun checkSaleTopOrder(workerNo: String,startTime: String, endTime: String) : List<OrderObject>{
        val dao: OrderObjectDao = baseDao as OrderObjectDao
        return dao.checkSaleTopOrder(workerNo,startTime,endTime)
    }
}