package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.QuickCheckOrderDao
import com.tranpos.vpos.entity.QuickCheckOrder


/**
 *CREATED BY AM
 *ON 2020/12/4
 *DESCRIPTION:
 **/
object QuickCheckOrderDbManager : BaseDBManager<QuickCheckOrder>(){
    override fun getDataBaseDao(): BaseDao<QuickCheckOrder> {
        return AppDatabase.getDatabase().quickCheckOrderDao
    }

    override fun loadAll(): MutableList<QuickCheckOrder> {
        val dao : QuickCheckOrderDao = baseDao as QuickCheckOrderDao
        return dao.loadAll()
    }

    override fun deleteAll(): Int {
        val dao : QuickCheckOrderDao = baseDao as QuickCheckOrderDao
        return dao.deleteAll()
    }

    fun loadOrderByTime(startTime : String,endTime : String) : List<QuickCheckOrder>{
        val dao : QuickCheckOrderDao = baseDao as QuickCheckOrderDao
        return dao.loadOrderByTime(startTime,endTime)
    }

    fun loadOrderById(id : String) : QuickCheckOrder?{
        val dao : QuickCheckOrderDao = baseDao as QuickCheckOrderDao
        return dao.loadOrderById(id)
    }
}