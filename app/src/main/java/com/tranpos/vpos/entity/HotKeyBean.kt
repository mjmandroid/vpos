package com.tranpos.vpos.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 *CREATED BY AM
 *ON 2020/9/1
 *DESCRIPTION:
 **/
class HotKeyBean(val id : Int,var description : String,var keyName : String) : MultiItemEntity{
    var dataType = KEY
    var title = ""
    var supportModify = true
    var moduleType = ""

    override fun getItemType(): Int {
        return dataType
    }

    companion object Type{
        const val TITLE = 0
        const val KEY = 1
    }
}

class HotKeyMap(var id : Int,var keyCode : Int,var keyName : String,var description : String,var moduleType : String = "")
