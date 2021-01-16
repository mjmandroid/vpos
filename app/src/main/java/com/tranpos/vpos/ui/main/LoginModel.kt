package com.tranpos.vpos.ui.main

import com.tranpos.network.manager.RequestManager
import com.tranpos.vpos.base.*
import com.tranpos.vpos.entity.BaseResult
import com.tranpos.vpos.entity.RegistrationCode
import com.tranpos.vpos.entity.Worker
import com.tranpos.vpos.utils.DES
import com.tranpos.vpos.utils.KeyConstrant
import com.tranpos.vpos.utils.OpenApiUtils
import com.tranpos.vpos.utils.UiUtils
import com.transpos.tools.GsonHelper
import com.transpos.tools.tputils.TPUtils
import java.util.*

/**
 *CREATED BY AM
 *ON 2021/1/16
 *DESCRIPTION:
 **/
class LoginModel : BaseViewModel() {

    private val mService : ApiService by lazy {
        RequestManager.apiService(ApiService::class.java)
    }

    fun login(userName: String, password: String,baseView : IBaseView,callback : (Worker)->Unit){
        RequestManager.resetBaseUrl(HttpUrl.BASE_API_URL)
        val parameters: MutableMap<String, Any> =
            OpenApiUtils.getInstance().newApiParameters()
        parameters["name"] = "worker.login"
        val auth: RegistrationCode = TPUtils.getObject(
            BaseApp.getApplication(), KeyConstrant.KEY_AUTH_REGISTER,
            RegistrationCode::class.java
        )
        val reqData: MutableMap<String, Any> =
            HashMap()
        reqData["storeId"] = auth.storeId
        reqData["posNo"] = auth.posNo
        reqData["workerNo"] = userName
        reqData["passwd"] = DES.encrypt(password)
        parameters["data"] = GsonHelper.toJson(reqData)
        parameters["sign"] = OpenApiUtils.getInstance().sign(auth, parameters)

        apiDSL<BaseResult<Worker>> {
            onStart {
                baseView.showLoading()
                true
            }

            onRequest {
                mService.doLogin(getRequestBody(parameters))
            }

            onResponse {
                response ->
                if(response.code == BaseResult.SUCCESS){
                    callback.invoke(response.data)
                } else{
                    UiUtils.showToastShort(response.msg)
                }
            }

            onError {
                UiUtils.showToastShort("登录失败")
                true
            }

            onFinal {
                baseView.hideLoading()
                true
            }
        }
    }
}