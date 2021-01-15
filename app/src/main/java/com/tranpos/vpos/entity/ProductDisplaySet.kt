package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/11/16
 *DESCRIPTION:
 **/
class ProductDisplaySet {
    var fontSizeMode = FONT_NORMAL
    var showCount = 4
    var notShowPrice = false
    var showUnit = true
    var showWeight = false
    var showCode =  false

    var isChanged = false

    companion object{
        const val FONT_NORMAL = 1
        const val FONT_BIG = 2
        const val FONT_SUPER_BIG = 3
        const val FONT_NORMAL_BOLD = 4
        const val FONT_BIG_BOLD = 4
        const val FONT_SUPER_BOLD = 4

        fun getFontName(id : Int) : String{
            return when(id){
                FONT_NORMAL -> "正常"
                FONT_BIG -> "大字"
                FONT_SUPER_BIG -> "超大"
                FONT_NORMAL_BOLD -> "正常加粗"
                FONT_BIG_BOLD -> "大字加粗"
                FONT_SUPER_BOLD -> "超大加粗"
                else -> ""
            }
        }
    }

}