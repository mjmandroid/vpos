package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.PreProductOrderDao
import com.tranpos.vpos.entity.PreProductOrder


/**
 *CREATED BY AM
 *ON 2020/11/30
 *DESCRIPTION:
 **/
object PreProductOrderDbManager : BaseDBManager<PreProductOrder>() {
    override fun getDataBaseDao(): BaseDao<PreProductOrder> {
        return AppDatabase.getDatabase().preProductOrderDao
    }

    override fun loadAll(): MutableList<PreProductOrder> {
        val dao = baseDao as PreProductOrderDao
        return dao.loadAll()
    }

    override fun deleteAll(): Int {
        val dao = baseDao as PreProductOrderDao
        return dao.deleteAll()
    }

    fun checkOrderByTime(startTime : String,endTime : String) : List<PreProductOrder>{
        val dao = baseDao as PreProductOrderDao
        return dao.checkOrderByTime(startTime,endTime)
    }

    fun checkOrderByNo(tradeNo : String) : PreProductOrder?{
        val dao = baseDao as PreProductOrderDao
        return dao.checkOrderByNo(tradeNo)
    }
}