package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.OrderPayDao
import com.tranpos.vpos.entity.OrderPay


object OrderPayDbManger : BaseDBManager<OrderPay>() {
    override fun getDataBaseDao(): BaseDao<OrderPay> {
        return AppDatabase.getDatabase().orderPayDao
    }


    override fun deleteAll(): Int {
        val dao : OrderPayDao = baseDao as OrderPayDao
        return dao.deleteAll()
    }

    fun deletePractice(tradeNo: String) : Int{
        val dao : OrderPayDao = baseDao as OrderPayDao
        return dao.deletePractice(tradeNo)
    }


    override fun loadAll(): MutableList<OrderPay> {
        val dao : OrderPayDao = baseDao as OrderPayDao
        return dao.loadAll()
    }

    fun findOderPayInfos(tradeNo: String) : MutableList<OrderPay>{
        val dao : OrderPayDao = baseDao as OrderPayDao
        return dao.findOderPayInfos(tradeNo)
    }

    fun findOrderPayRejected(tradeNo: String) : MutableList<OrderPay>{
        val dao : OrderPayDao = baseDao as OrderPayDao
        return dao.findOrderPayRejected(tradeNo)
    }
}