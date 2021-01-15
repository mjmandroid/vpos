package com.tranpos.vpos.entity

/**
 *CREATED BY AM
 *ON 2020/8/18
 *DESCRIPTION:
 **/

sealed class ChartEntity(val color : Int,val name : String,var value : Double) {

}

class PayWayChartBean(val payNo : String,color: Int, payName : String, money : Double) : ChartEntity(color,payName,money){
}