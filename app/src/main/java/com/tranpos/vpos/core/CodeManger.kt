package com.tranpos.vpos.core


import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.entity.CashParamterInfo
import com.tranpos.vpos.utils.KeyConstrant
import com.transpos.tools.tputils.TPUtils

/**
 *CREATED BY AM
 *ON 2020/9/15
 *DESCRIPTION:
 **/
class CodeManger {
    companion object{
        fun parseCode(code : String) : Boolean{
            var isNotCodeFlag = false
            try {
                val money = code.toDouble()
                val cashParamterInfo = TPUtils.getObject(
                    BaseApp.getApplication(),
                    KeyConstrant.KEY_CASH_PARAMETER,
                    CashParamterInfo::class.java
                )
                if(cashParamterInfo != null && cashParamterInfo.isNoCodeEnable){
                    if(money <= cashParamterInfo.maxMoney){
                        isNotCodeFlag = true
                    }
                }
            } catch (e: Exception) {

            }
            return isNotCodeFlag
        }
    }
}