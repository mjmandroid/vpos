package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.ShiftLogDao
import com.tranpos.vpos.entity.ShiftLog


/**
 *CREATED BY AM
 *ON 2020/8/3
 *DESCRIPTION:
 **/
object ShiftLogDbManger : BaseDBManager<ShiftLog>() {
    override fun getDataBaseDao(): BaseDao<ShiftLog> {
        return AppDatabase.getDatabase().shiftLogDao
    }

    override fun loadAll(): MutableList<ShiftLog> {
        val dao : ShiftLogDao = baseDao as ShiftLogDao
        return dao.loadAll()
    }

    override fun deleteAll(): Int {
        val dao : ShiftLogDao = baseDao as ShiftLogDao
        return dao.deleteAll()
    }

    fun getNotShift(workerId : String) : ShiftLog?{
        val dao : ShiftLogDao = baseDao as ShiftLogDao
        return dao.getNoShift(workerId)
    }

    fun getShiftList(workerId : String) : List<ShiftLog>{
        val dao : ShiftLogDao = baseDao as ShiftLogDao
        return dao.getShiftListById(workerId)
    }

    fun getShiftList(workerId : String,status : Int) : List<ShiftLog>{
        val dao : ShiftLogDao = baseDao as ShiftLogDao
        return dao.getShiftListById(workerId,status)
    }

    fun getHasShiftList(startTime : String,endTime : String) : List<ShiftLog>{
        val dao : ShiftLogDao = baseDao as ShiftLogDao
        return dao.getHasShiftList(startTime,endTime)
    }
}