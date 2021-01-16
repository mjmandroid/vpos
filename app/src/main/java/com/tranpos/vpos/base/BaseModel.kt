package com.tranpos.vpos.base

import com.tranpos.network.model.RequestViewModel
import com.transpos.tools.GsonHelper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

open class BaseViewModel : RequestViewModel() {

    private fun getContentType() : MediaType {
        return "application/json; charset=UTF-8".toMediaType()
    }

    fun getRequestBody(parameters : Map<String, Any>) : RequestBody {
        return GsonHelper.toJson(parameters).toRequestBody(getContentType())
    }
}