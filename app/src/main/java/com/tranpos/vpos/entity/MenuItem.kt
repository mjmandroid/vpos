package com.tranpos.vpos.entity

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 *CREATED BY AM
 *ON 2020/11/2
 *DESCRIPTION:
 **/
sealed class MenuItem(val id : Int,val name : String) {
    class OrderMenuItem(id: Int,name: String) : MenuItem(id,name)
    class SystemMenuItem(id : Int,name : String,val type : Int) : MenuItem(id,name),MultiItemEntity{
        override fun getItemType(): Int {
            return type
        }

    }
}