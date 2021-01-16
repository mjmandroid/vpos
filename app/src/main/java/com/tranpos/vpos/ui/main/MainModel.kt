package com.tranpos.vpos.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tranpos.network.manager.RequestManager
import com.tranpos.vpos.base.ApiService
import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.base.BaseViewModel
import com.tranpos.vpos.entity.BaseResult
import com.tranpos.vpos.entity.DownloadCacheName
import com.tranpos.vpos.entity.RegistrationCode
import com.tranpos.vpos.entity.Tuple2
import com.tranpos.vpos.utils.Global
import com.tranpos.vpos.utils.KeyConstrant
import com.tranpos.vpos.utils.OpenApiUtils
import com.transpos.sale.thread.ThreadDispatcher
import com.transpos.tools.GsonHelper
import com.transpos.tools.logger.Logger
import com.transpos.tools.tputils.TPUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

/**
 *CREATED BY AM
 *ON 2021/1/16
 *DESCRIPTION:
 **/
class MainModel : BaseViewModel() {

    private val mService : ApiService by lazy {
        RequestManager.apiService(ApiService::class.java)
    }

    fun startLoad(){
        viewModelScope.launch (context = Dispatchers.Main){
            val dataVersion = async(context = Dispatchers.IO){
                val parameters: MutableMap<String, Any> =
                    OpenApiUtils.getInstance().newApiParameters()
                parameters["name"] = "server.data.version"
                val auth: RegistrationCode = TPUtils.getObject(
                    BaseApp.getApplication(), KeyConstrant.KEY_AUTH_REGISTER,
                    RegistrationCode::class.java
                )

                val reqData: MutableMap<String, Any> =
                    HashMap()
                reqData["storeId"] = auth.storeId
                parameters["data"] = GsonHelper.toJson(reqData)
                parameters["sign"] = OpenApiUtils.getInstance().sign(auth, parameters)
                mService.obtainServerDataVersion(getRequestBody(parameters))
            }
            val dataVersionResult = dataVersion.await()
            if(dataVersionResult != null){
                if(dataVersionResult.code == BaseResult.SUCCESS){
                    if (dataVersionResult.data == null || dataVersionResult.data.size === 0) {
                        Logger.d("未发现服务端数据版本信息")
                        completeDownload()
                    } else {

                    }
                } else {

                }
            }
        }
    }

    private fun completeDownload() {

    }

    private fun compareVersion(datas: List<Map<String?, String?>>) {
        val localList = TPUtils.getObject(
                BaseApp.getApplication(), KeyConstrant.KEY_DATA_VERSION,
                List::class.java
            ) as List<Map<String,String>>
        val needDownload = Global.downloadDataType.toList()
        val vLists: MutableList<Tuple2<String, String>> =
            ArrayList<Tuple2<String, String>>()
        if (localList != null && localList.isNotEmpty()) {
            val localJson = GsonHelper.toJson(localList)
            val serverJson = GsonHelper.toJson(datas)

        } else {

        }
        for (map in datas) {
            val stype = map["dataType"]
            val sversion = map["dataVersion"]
            var isNeedUpdate = false
            if (needDownload.contains(stype)) {
                if (localList != null) {
                    for (localMap in localList) {
                        if (stype == localMap["dataType"]) {
                            if (sversion != localMap["dataVersion"]) {
                                isNeedUpdate = true
                            }
                            break
                        }
                    }
                } else {
                    isNeedUpdate = true
                }
            }
            if (isNeedUpdate) vLists.add(
                Tuple2<String, String>(
                    stype,
                    sversion
                )
            )
        }
        if (vLists.size == 0) {
            completeDownload()
            Logger.d("本地为最新数据")
            return
        }

        for (list in vLists) {
            val dataType: String = list.first
            val downloadCacheName: DownloadCacheName = DownloadCacheName.valueOf(dataType)

        }
        TPUtils.putObject(BaseApp.getApplication(), KeyConstrant.KEY_DATA_VERSION, datas)
    }
}