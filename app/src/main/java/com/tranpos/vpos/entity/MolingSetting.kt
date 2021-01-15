package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/17
 *DESCRIPTION:抹零设置
 **/
class MolingSetting(var isOpen : Boolean,val type : Int,val typeName : String,val example : String)

interface MolingType{

    companion object {
        //四舍五入到角
        const val  ROUND_JIAO_TYPE = 0
        //向下抹零到角
        const val  FLOOR_JIAO_TYPE = 1
        //向上抹零到角
        const val  CEIL_JIAO_TYPE = 2
        //四舍五入到元
        const val ROUND_YUAN_TYPE = 3
        //向下抹零到元
        const val FLOOR_YUAN_TYPE = 4
        //向上抹零到元
        const val CEIL_YUAN_TYPE = 5
        //向下抹零到五角
        const val FLOOR_FIVE_JIAO_TYPE = 6
        //向上抹零到五角
        const val CEIL_FIVE_JIAO_TYPE = 7
    }
}