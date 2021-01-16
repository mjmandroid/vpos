package com.tranpos.vpos.base

import com.tranpos.vpos.entity.*
import okhttp3.RequestBody
import retrofit2.http.*

/**
 *CREATED BY AM
 *ON 2021/1/16
 *DESCRIPTION:
 **/
interface ApiService {

//    @FormUrlEncoded
    @POST(".")
//    suspend fun doRegister(@FieldMap parameter : Map<String,@JvmSuppressWildcards Any>) : BaseResult<RegistrationCode>
    suspend fun doRegister(@Body body : RequestBody) : BaseResult<RegistrationCode>

    @POST(".")
    suspend fun doLogin(@Body body : RequestBody) : BaseResult<Worker>
}