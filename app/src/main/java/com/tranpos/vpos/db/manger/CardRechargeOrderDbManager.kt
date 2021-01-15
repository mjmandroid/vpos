package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.CardRechargeOrderDao
import com.tranpos.vpos.entity.CardRechargeOrder


/**
 *CREATED BY AM
 *ON 2020/11/2
 *DESCRIPTION:
 **/
object CardRechargeOrderDbManager : BaseDBManager<CardRechargeOrder>() {

    override fun getDataBaseDao(): BaseDao<CardRechargeOrder> {
        return AppDatabase.getDatabase().cardRechargeOrderDao
    }

    override fun deleteAll(): Int {
        val dao = baseDao as CardRechargeOrderDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<CardRechargeOrder> {
        val dao = baseDao as CardRechargeOrderDao
        return dao.loadAll()
    }

    fun findOrdersByTime(startTime : String,endTime : String) : List<CardRechargeOrder>{
        val dao = baseDao as CardRechargeOrderDao
        return dao.findOrdersByTime(startTime,endTime)
    }
}