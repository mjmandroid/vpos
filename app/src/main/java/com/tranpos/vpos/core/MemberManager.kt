package com.tranpos.vpos.core

import com.tranpos.vpos.base.BaseApp
import com.tranpos.vpos.entity.Member
import com.tranpos.vpos.utils.KeyConstrant
import com.transpos.tools.tputils.TPUtils

/**
 *CREATED BY AM
 *ON 2021/1/11
 *DESCRIPTION:
 **/
object MemberManager {
    fun isMember() : Boolean{
        return TPUtils.getObject(BaseApp.getApplication(), KeyConstrant.KEY_MEMBER, Member::class.java) != null
    }
}