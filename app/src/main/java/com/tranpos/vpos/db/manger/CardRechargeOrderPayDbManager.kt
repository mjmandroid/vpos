package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.CardRechargeOrderPayDao
import com.tranpos.vpos.entity.CardRechargeOrderPay


/**
 *CREATED BY AM
 *ON 2020/11/3
 *DESCRIPTION:
 **/
object CardRechargeOrderPayDbManager : BaseDBManager<CardRechargeOrderPay>() {
    override fun getDataBaseDao(): BaseDao<CardRechargeOrderPay> {
        return AppDatabase.getDatabase().cardRechargeOrderPayDao
    }

    override fun deleteAll(): Int {
        val dao = baseDao as CardRechargeOrderPayDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<CardRechargeOrderPay> {
        val dao = baseDao as CardRechargeOrderPayDao
        return dao.loadAll()
    }

    fun findPaysByNo(tradeNo : String) : List<CardRechargeOrderPay>{
        val dao = baseDao as CardRechargeOrderPayDao
        return dao.findPaysByNo(tradeNo)
    }
}