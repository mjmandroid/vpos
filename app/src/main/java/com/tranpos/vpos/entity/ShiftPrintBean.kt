package com.tranpos.vpos.entity

import com.tranpos.vpos.entity.ShiftByCategroyBean
import com.tranpos.vpos.entity.ShiftByProductBean
import com.tranpos.vpos.entity.ShiftCashDetail
import com.tranpos.vpos.entity.ShiftPayWaysBean
import kotlin.properties.Delegates

/**
 *CREATED BY AM
 *ON 2020/8/4
 *DESCRIPTION:
 * 封裝交班打印类
 **/
class ShiftPrintBean {
//    @Transient
//    var mObserver : IMemoObserver? = null
    var shop : String? = null
    var posNo : String? = null
    var date : String? = null
    var headDate : String? = null
    var tailDate : String? = null
    var count : Int = 0
    var pays : List<ShiftPayWaysBean>? = null
    var detail : ShiftCashDetail? = null
    var memo : String by Delegates.observable(""){ property , oldValue , newValue ->
//        mObserver?.onContentChanged(newValue)
    }
    var memoInfo : String = ""
    var totalBus : Double = 0.0
    var casher : String = ""
    var type : Int = 0

    var productInfos : List<ShiftByProductBean>? = null
    var categroyInfos : List<ShiftByCategroyBean>? = null
    var totalAmount : Double = 0.0
    var totalMoney : Double = 0.0
}

