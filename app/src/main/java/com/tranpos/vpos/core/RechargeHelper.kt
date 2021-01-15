package com.tranpos.vpos.core

import android.text.TextUtils
import com.tranpos.vpos.entity.Member
import com.transpos.sale.thread.ThreadDispatcher
import com.transpos.tools.tputils.TPUtils
import org.json.JSONObject
import java.net.URLEncoder
import java.util.HashMap

/**
 *CREATED BY AM
 *ON 2020/10/28
 *DESCRIPTION:
 **/
class RechargeHelper {

    /*companion object{
        private var payModel : CashModel? = null
        private var queryCount = 0

        fun doRecharge(option : RechargeOption?, otherMoney : Double, giftMoney : Double, member: Member, payTypeNo : String,
                       callback: JsonObjectCallback<*>){
            val parameters =
                OpenApiUtils.getInstance().newApiParameters()
            parameters["name"] = "member.card.recharge"
            val api = TPUtils.getObject(
                BaseApp.getApplication(), KeyConstrant.KEY_AUTH_REGISTER,
                RegistrationCode::class.java
            )
            val worker = TPUtils.getObject(
                BaseApp.getApplication(), KeyConstrant.KEY_WORKER,
                Worker::class.java
            )
            val serialNumber = String.format("%0" + 4 + "d", 0)
            val payNo ="${api.storeNo}1${DateUtil.getNowDateStr("yyMMddHHmm")}${api.posNo}$serialNumber${StringKotlin.generateString(2)}"
            val reqData: MutableMap<String, Any?> =
                HashMap()
            reqData["tradeNo"] = payNo
            reqData["cardNo"] = member.cardNo
            reqData["rechargeAmount"] = StringKotlin.getFenValue(option?.money?.toFloat() ?: otherMoney.toFloat())
            reqData["actualAmount"] = StringKotlin.getFenValue(option?.money?.toFloat() ?: otherMoney.toFloat())
            reqData["giftAmount"] = StringKotlin.getFenValue(option?.giftMoney?.toFloat() ?: giftMoney.toFloat())
            reqData["giftPoint"] = StringKotlin.getFenValue(option?.giftPoint?.toFloat() ?: 0f)
            reqData["freeFlag"] = 1
            reqData["payTypeNo"] = payTypeNo
            reqData["payTypeName"] = URLEncoder.encode(OrderPayManger.getPayName(payTypeNo),"UTF-8")
            reqData["schemeId"] = ""
            reqData["description"] = ""
            reqData["onlineFlag"] = 0
            reqData["salesCode"] = OrderManger.getOrderBean()?.salesCode
            reqData["salesName"] = OrderManger.getOrderBean()?.salesName
            reqData["shopNo"] = api.storeNo
            reqData["posNo"] = api.posNo
            reqData["workerNo"] = worker.no
            reqData["sourceSign"] = Global.SOURCE_SIGN
            parameters["data"] = GsonHelper.toJson(reqData)
            parameters["sign"] = OpenApiUtils.getInstance().sign(api, parameters)
            HttpManger.getSingleton().postJsonObject(
                HttpUrl.BASE_API_URL,
                parameters,
                null,
                callback
            )
        }
        fun doOnlinePay(option : RechargeOption?,
                        otherMoney : Double,
                        giftMoney : Double,
                        member: Member,
                        barcode : String,
                       callback: JsonObjectCallback<*>){
            val payMoney = option?.money ?: otherMoney
            performPay(option,otherMoney,giftMoney,member,payMoney,barcode,callback)
        }

        private fun performPay(option : RechargeOption?,
                               otherMoney : Double,
                               giftMoney : Double,
                               member: Member,
                               money : Double,
                               barcode: String,
                               callback: JsonObjectCallback<*>){
            val startCode = barcode.substring(0, 2)
            if (!StringUtils.isNumeric(startCode)) {
                UiUtils.showToastShort("条码错误")
                callback.onFinish()
                return
            }
            when (startCode.toInt()) {
                in 10..15 -> {
                    //微信
                    realPay(OrderPayManger.PayModeEnum.PAYWX.payNo,option,otherMoney,giftMoney,member,money, barcode,callback)
                }
                in 25..30 -> {
                    //支付宝
                    realPay(OrderPayManger.PayModeEnum.PAYAIL.payNo,option,otherMoney,giftMoney,member,money, barcode,callback)
                }
                else -> {
                    UiUtils.showToastShort("条码错误")
                    callback.onFinish()
                }
            }
        }


        private fun realPay(payTypeNo: String,option : RechargeOption?,
                          otherMoney : Double,
                          giftMoney : Double,
                          member: Member,
                          money: Double,
                          barcode: String,
                          callback: JsonObjectCallback<*>) {
            if(payModel == null){
                payModel = CashModel()
            }
            payModel!!.scanPay(money.toFloat(), barcode, object : StringCallback() {
                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    response?.let {
                        val leshua_order_id = Tools.parseGetAuthInfoXML(it.body(), "leshua_order_id")
                        if (!TextUtils.isEmpty(leshua_order_id)) {
                            queryCount = 0
                            queryOrder(option,otherMoney,giftMoney,member,payTypeNo,leshua_order_id,callback)
                        } else {
                            UiUtils.showToastShort(Tools.parseGetAuthInfoXML(it.body(), "resp_msg"))
                            callback.onFinish()
                        }
                    }
                }

                override fun onError(response: Response<String>?) {
                    super.onError(response)
                    UiUtils.showToastShort(response?.exception.toString())
                    callback.onFinish()
                }
            })
        }

        private fun queryOrder(option : RechargeOption?,
                               otherMoney : Double,
                               giftMoney : Double,
                               member: Member,
                               payNo: String,
                               leshuaOrderId: String,
                               callback: JsonObjectCallback<*>) {
            if(payModel == null){
                payModel = CashModel()
            }
            payModel?.queryOrder(leshuaOrderId, object : StringCallback() {
                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    var body = response?.body()
                    if (!TextUtils.isEmpty(body)) {
                        val status = Tools.parseGetAuthInfoXML(body, "status")
                        val errorMsg = Tools.parseGetAuthInfoXML(body, "error_msg")
                        val payWay = Tools.parseGetAuthInfoXML(body, "pay_way")
                        when (status) {
                            "0" -> {
                                //支付中
                                queryCount++
                                if (queryCount >= 30) {
                                    callback.onFinish()
                                    UiUtils.showToastLong("支付超时")
                                } else {
                                    ThreadDispatcher.getDispatcher().postOnMainDelayed({
                                        queryOrder(option,otherMoney,giftMoney,member,payNo,leshuaOrderId,callback)
                                    }, 3000L)
                                }
                            }
                            "2" -> {

                                //支付成功
                                doRecharge(
                                    option,otherMoney,giftMoney,member,payNo,callback
                                )
                            }
                            "6" -> {
                                callback.onFinish()
                                //订单关闭
                                UiUtils.showToastShort("订单关闭")

                            }
                            "8" -> {
                                //支付失败]
                                callback.onFinish()
                                UiUtils.showToastLong("支付失败")

                            }
                            else -> {
                                UiUtils.showToastLong("支付失败")
                               callback.onFinish()
                            }
                        }
                    }
                }

                override fun onError(response: Response<String>?) {
                    UiUtils.showToastLong("支付失败")
                    callback.onFinish()
                }
            })
        }

        *//**
         * 获取会员兴趣爱好标签
         *//*
        fun requestMemberLabel(callback: (List<MemberHobbyLabel>) -> Unit){
            val parameters =
                OpenApiUtils.getInstance().newApiParameters()
            parameters["name"] = "member.tag"
            val api = TPUtils.getObject(
                BaseApp.getApplication(), KeyConstrant.KEY_AUTH_REGISTER,
                RegistrationCode::class.java
            )
            val reqData: MutableMap<String, Any?> =
                HashMap()
            reqData["storeId"] = api.storeId
            parameters["data"] = GsonHelper.toJson(reqData)
            parameters["sign"] = OpenApiUtils.getInstance().sign(api, parameters)
            HttpManger.getSingleton().postJsonObject(
                HttpUrl.BASE_API_URL,
                parameters,
                null,
                object : JsonObjectCallback<BaseListResponse<MemberHobbyLabel>>(){
                    override fun onSuccess(response: Response<BaseListResponse<MemberHobbyLabel>>?) {
                        super.onSuccess(response)
                        response?.let {
                            if(it.body().code == BaseResponse.SUCCESS){
                                val list = it.body().data
                                callback.invoke(list)
                            }
                        }
                    }
                }
            )
        }

        *//**
         * 开户
         *//*
        fun register(member: Member,callback : () ->Unit){
            val parameters =
                OpenApiUtils.getInstance().newApiParameters()
            parameters["name"] = "member.open"
            val api = TPUtils.getObject(
                BaseApp.getApplication(), KeyConstrant.KEY_AUTH_REGISTER,
                RegistrationCode::class.java
            )
            parameters["data"] = GsonHelper.toJson(member)
            parameters["sign"] = OpenApiUtils.getInstance().sign(api, parameters)

            HttpManger.getSingleton().postString(HttpUrl.BASE_API_URL,parameters,object : StringCallback(){
                override fun onSuccess(response: Response<String>?) {
                    super.onSuccess(response)
                    response?.let {
                        val jsonObject = JSONObject(it.body())
                        val code = jsonObject.optInt("code")
                        val success = code == BaseResponse.SUCCESS
                        if(!success){
                            UiUtils.showToastShort(jsonObject.optString("msg"))
                        } else{
                            UiUtils.showToastShort("开户成功")
                            callback.invoke()
                        }

                    }
                }
            })
        }

        *//**
         * 会员类型
         *//*
        fun requestMemberType(callback: (List<MemberType>) ->Unit){
            val parameters =
                OpenApiUtils.getInstance().newApiParameters()
            parameters["name"] = "member.type"
            val api = TPUtils.getObject(
                BaseApp.getApplication(), KeyConstrant.KEY_AUTH_REGISTER,
                RegistrationCode::class.java
            )
            val reqData: MutableMap<String, Any?> =
                HashMap()
            reqData["storeId"] = api.storeId
            parameters["data"] = GsonHelper.toJson(reqData)
            parameters["sign"] = OpenApiUtils.getInstance().sign(api, parameters)
            HttpManger.getSingleton().postJsonObject(
                HttpUrl.BASE_API_URL,
                parameters,
                null,
                object : JsonObjectCallback<BaseListResponse<MemberType>>(){
                    override fun onSuccess(response: Response<BaseListResponse<MemberType>>?) {
                        super.onSuccess(response)
                        response?.let {
                            if(it.body().code == BaseResponse.SUCCESS){
                                callback.invoke(it.body().data)
                            }
                        }
                    }
                }
            )
        }
    }*/
}