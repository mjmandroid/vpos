package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/20
 *DESCRIPTION:
 **/
sealed class PopStreamEntity(val typeName : String) {
    class TimeStreamEntity(val id : String,val type: Int,name : String) : PopStreamEntity(name)
    class CasherStreamEntity(val no : String,name : String) : PopStreamEntity(name)
    class SalesStreamEntity(val code : String,name: String) : PopStreamEntity(name)
    class SaleWaysStreamEntity(val no : Int ,ways : String) : PopStreamEntity(ways)
    class CashWaysStreamEntity(val no : String,name : String) : PopStreamEntity(name)
    class DeliverStreamEntity(val id : Int,way : String) : PopStreamEntity(way)
    class OrderStatusStreamEntity(val id : Int,statusName : String) : PopStreamEntity(statusName)
    class OrderSourceStreamEntity(val id : Int,source : String) : PopStreamEntity(source)
    class GoodsBrandEntity(val id : String,brand : String) : PopStreamEntity(brand)
    class GoodsCategroyEntity(val id : String,categoryName : String) : PopStreamEntity(categoryName)
    class ExpressEntity(val id : String,name : String) : PopStreamEntity(name)
    class SerialBitEntity(val id : String,name : String) : PopStreamEntity(name)

    class MemberTypeEntity(val memberType: MemberType) : PopStreamEntity(memberType.name)
    class MemberLevelEntity(val level : MemberLevel) : PopStreamEntity(level.name)
    class CryTypeEntity(val id : String,name : String) : PopStreamEntity(name)
    class AgeTypeEntity(val id : String,name: String) : PopStreamEntity(name)

    class FontEnumEntity(val id : Int,name: String) : PopStreamEntity(name)

    class PreStateEntity(val id : Int,name : String) : PopStreamEntity(name)

    class ProductClassEntity(val id : String,name : String) : PopStreamEntity(name)
    class CalcPriceWayEntity(val id : Int,name : String) : PopStreamEntity(name)
    class CheckStateEntity(val id : Int,name: String) : PopStreamEntity(name)
    class SendStateEntity(val id : Int,name: String) : PopStreamEntity(name)
    class SteelyardEntity(val id : Int,name: String) : PopStreamEntity(name)
}

