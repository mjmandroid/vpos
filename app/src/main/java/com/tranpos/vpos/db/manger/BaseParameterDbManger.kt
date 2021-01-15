package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.BaseParameterDao
import com.tranpos.vpos.entity.Baseparameter


/**
 *CREATED BY AM
 *ON 2020/9/27
 *DESCRIPTION:
 **/
object BaseParameterDbManger : BaseDBManager<Baseparameter>() {

    override fun getDataBaseDao(): BaseDao<Baseparameter> {
        return AppDatabase.getDatabase().baseParameterDao
    }

    override fun deleteAll(): Int {
        val dao : BaseParameterDao = baseDao as BaseParameterDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<Baseparameter> {
        val dao : BaseParameterDao = baseDao as BaseParameterDao
        return dao.loadAll()
    }
}