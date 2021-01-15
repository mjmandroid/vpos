package com.tranpos.vpos.core

import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.entity.OrderObject
import com.tranpos.vpos.entity.RegistrationCode
import com.tranpos.vpos.entity.Worker
import com.tranpos.vpos.entity.state.OrderPaymentStatusFlag
import com.tranpos.vpos.entity.state.OrderSourceFlag
import com.tranpos.vpos.entity.state.OrderStatusFlag
import com.tranpos.vpos.entity.state.PostWayFlag
import com.tranpos.vpos.utils.DateUtil.SIMPLE_FORMAT
import com.tranpos.vpos.db.manger.OrderObjectDbManger
import com.tranpos.vpos.db.manger.ShiftLogDbManger
import com.tranpos.vpos.utils.*

import com.transpos.tools.tputils.TPUtils
import java.util.*

object OrderManger {
    private var orderBean : OrderObject? = null

    fun initalize() : OrderObject{
        val serialNumber = Tools.generateSerialNumber(4, KeyConstrant.KEY_SERIAL_ORDER)
        val api = TPUtils.getObject(
            BaseApp.getApplication(), KeyConstrant.KEY_AUTH_REGISTER,
            RegistrationCode::class.java
        )
        val payNo = "${api.storeNo}1${DateUtil.getNowDateStr("yyMMddHHmm")}${api.posNo}$serialNumber${StringKotlin.generateString(2)}"
        orderBean = OrderObject()

        val worker = TPUtils.getObject(
            BaseApp.getApplication(), KeyConstrant.KEY_WORKER,
            Worker::class.java
        )
        val shift = ShiftLogDbManger.getNotShift(worker.id)
        orderBean!!.let{
            it.id = IdWorkerUtils.nextId()
            it.tenantId = api.tenantId
            it.objectId = IdWorkerUtils.nextId()
            it.tradeNo = payNo
            //it.orderNo
            it.storeId = api.storeId
            it.storeNo = api.storeNo
            it.storeName = api.storeName
            it.workerNo = worker.no
            it.workerName = worker.name
            it.saleDate = DateUtil.getNowDateStr(SIMPLE_FORMAT)
            //it.finishDate
            it.posNo = api.posNo
            it.deviceName = DeviceUtils.getInstance().computerName
            it.macAddress = DeviceUtils.getInstance().macAddress
            it.ipAddress = DeviceUtils.getInstance().ipAddress
            //it.itemCount
            //it.payCount
            //it.totalQuantity
            //it.amount
            //it.discountAmount
            //it.receivableAmount
            //it.paidAmount
            //it.receivedAmount
            //it.malingAmount
            //it.changeAmount
            //it.invoicedAmount
            //it.overAmount
            it.orderStatus = OrderStatusFlag.WAIT_STATE
            it.paymentStatus = OrderPaymentStatusFlag.NO_PAY_STATE
            //it.printStatus
            //it.printTimes
            it.postWay = PostWayFlag.ZITI_STATE
            it.orderSource = OrderSourceFlag.CASH_PLATFORM
            it.people = 1
            it.shiftId = shift?.id
            it.shiftName = "默认班次"
            it.shiftNo = shift?.no
            //it.discountRate
            //it.isMember
            //it.memberNo
            //it.memberName
            //it.memberMobileNo
            //it.cardFaceNo
            //it.prePoint
            //it.addPoint
            //it.refundPoint
            //it.aftPoint
            //it.aftAmount
            it.uploadStatus = 0
            //it.uploadErrors
            //it.uploadCode
            //it.uploadMessage
            //it.serverId
            //it.uploadTime
            //it.weather
            it.weeker = DateUtil.getWeekOfDate(Date())
            //it.remark
            it.createUser = worker.createUser
            it.createDate = DateUtil.getNowDateStr(SIMPLE_FORMAT)
            it.modifyUser = worker.modifyUser
            it.modifyDate = DateUtil.getNowDateStr(SIMPLE_FORMAT)
            //it.memberId
            //it.receivableRemoveCouponAmount
            //it.isPlus
            //it.plusDiscountAmount
            //it.salesCode
            //it.salesName
            //it.freightAmount
            //it.orgTradeNo
            //it.refundCause


            OrderItemManger.clear() //创建订单的时候必须把内存中订单项list清空
            OrderPayManger.clear()
            OrderObjectDbManger.insert(it)
        }
        return orderBean!!
    }

    fun reset(){
        orderBean?.let {

            it.finishDate = ""
            it.itemCount = 0
            it.totalQuantity = 0.0
            it.amount = 0.0
            it.discountAmount = 0.0
            it.receivableAmount = 0.0
            it.paidAmount = 0.0
            it.receivedAmount = 0.0
            it.malingAmount = 0.0
            it.changeAmount = 0.0
            it.invoicedAmount = 0.0
            it.overAmount = 0.0
            it.orderStatus = OrderStatusFlag.WAIT_STATE
            it.paymentStatus = OrderPaymentStatusFlag.NO_PAY_STATE
            //it.printStatus
            //it.printTimes
            it.postWay = PostWayFlag.ZITI_STATE
            it.orderSource = OrderSourceFlag.CASH_PLATFORM
            it.people = 1

            it.discountRate = 0.0
            it.isMember
            //it.memberNo
            //it.memberName
            //it.memberMobileNo
            //it.cardFaceNo
            //it.prePoint
            //it.addPoint
            //it.refundPoint
            //it.aftPoint
            //it.aftAmount
            it.uploadStatus = 0
            //it.uploadErrors
            //it.uploadCode
            //it.uploadMessage
            //it.serverId
            //it.uploadTime
            //it.weather
//            it.weeker = DateUtil.getWeekOfDate(Date())
            //it.remark
//            it.createUser = worker.createUser
//            it.createDate = DateUtil.getNowDateStr(SIMPLE_FORMAT)
//            it.modifyUser = worker.modifyUser
//            it.modifyDate = DateUtil.getNowDateStr(SIMPLE_FORMAT)
            //it.memberId
            it.receivableRemoveCouponAmount = 0.0
            //it.isPlus
            //it.plusDiscountAmount
            //it.salesCode
            //it.salesName
//            it.freightAmount = 0.0
            //it.orgTradeNo
            //it.refundCause
            it.ext1 = ""
            it.ext2 = ""
        }
        OrderItemManger.clear() //创建订单的时候必须把内存中订单项list清空
        OrderPayManger.clear()
    }

    fun getOrderBean() : OrderObject?{
        return orderBean
    }

    fun setOrderBean(bean : OrderObject){
        orderBean = bean
    }

    fun clear(){
        orderBean = null
        OrderItemManger.clear()
        VipOrderDirector.clearOrder()
    }
}