package com.tranpos.vpos.entity.state

/**
 *CREATED BY AM
 *ON 2020/11/19
 *DESCRIPTION:快捷键模块类型
 **/
enum class KeySetModule(val typeName : String) {
    CASH_MODULE("收银快捷键"),
    PAY_MODULE("结账快捷键"),
    MEMBER_MODULE("会员快捷键"),
    BUSINESS_MODULE("业务快捷键"),
    OTHER_MODULE("其他快捷键")
}