package com.tranpos.vpos.entity.state

/**
 *CREATED BY AM
 *ON 2020/11/30
 *DESCRIPTION:
 **/
enum class PredeterPostWay(val way : Int,val wayName : String) {

    DELIVERY_HOME(PostWayFlag.EMAIL_STATE,"配送到家"),
    ZITI(PostWayFlag.ZITI_STATE,"上门自提")
}