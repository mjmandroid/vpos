package com.tranpos.vpos.ui.main

import android.text.TextUtils
import com.tranpos.network.manager.RequestManager
import com.tranpos.vpos.base.ApiService
import com.tranpos.vpos.base.BaseViewModel
import com.tranpos.vpos.base.IBaseView
import com.tranpos.vpos.entity.BaseResult
import com.tranpos.vpos.entity.RegistrationCode
import com.tranpos.vpos.utils.DeviceUtils
import com.tranpos.vpos.utils.FoodConstant
import com.tranpos.vpos.utils.OpenApiUtils
import com.tranpos.vpos.utils.UiUtils
import com.transpos.tools.GsonHelper
import com.transpos.tools.logger.Logger
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

/**
 *CREATED BY AM
 *ON 2021/1/16
 *DESCRIPTION:
 **/
class RegisterModel : BaseViewModel() {

    private val mService : ApiService by lazy {
        RequestManager.apiService(ApiService::class.java)
    }

    fun register(authCode : String,iView : IBaseView,callback: (RegistrationCode)->Unit){
        var computerName = "android"
        var macAddress = "192.168.0.23"
        var serialNumber = "ffffffff-f794-c36e-ffff-ffffef05ac4a"
        var cpuNumber = "0000000000000000"

        val parameters: MutableMap<String, Any> =
            OpenApiUtils.getInstance().newParameters()
        parameters["name"] = "regist"
        parameters["appSign"] = FoodConstant.appSign
        parameters["terminalType"] = FoodConstant.terminalType
        val device: DeviceUtils = DeviceUtils.getInstance()
        if (!TextUtils.isEmpty(device.getComputerName())) {
            computerName = device.getComputerName()
        }
        if (!TextUtils.isEmpty(device.getMacAddress())) {
            macAddress = device.getMacAddress()
        }
        if (!TextUtils.isEmpty(device.getMotherboardNumber())) {
            serialNumber = device.getMotherboardNumber()
        }
        if (!TextUtils.isEmpty(device.getCpuID())) {
            cpuNumber = device.getCpuID()
        }
        parameters["computerName"] = computerName
        parameters["macAddress"] = macAddress
        parameters["serialNumber"] = serialNumber
        parameters["cpuNumber"] = cpuNumber
        parameters["authCode"] = authCode
        parameters["sign"] = OpenApiUtils.getInstance().sign(parameters, ArrayList<String>())

        apiDSL<BaseResult<RegistrationCode>> {
            onStart {
                iView.showLoading()
                false
            }
            onRequest {
                mService.doRegister(getRequestBody(parameters))
            }
            onResponse {
                response ->
                if(response.code == BaseResult.SUCCESS){
                    callback.invoke(response.data)
                } else {
                    UiUtils.showToastShort(response.msg)
                }
            }

            onError {
                Logger.e(it.toString())
                UiUtils.showToastShort("注册失败")
                true
            }

            onFinal {
                iView.hideLoading()
                false
            }
        }
    }
}