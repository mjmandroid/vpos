package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.OrderItemPayDao
import com.tranpos.vpos.entity.OrderItemPay


object OrderItemPayDbManger : BaseDBManager<OrderItemPay>() {
    override fun getDataBaseDao(): BaseDao<OrderItemPay> {
        return AppDatabase.getDatabase().orderItemPayDao
    }

    override fun deleteAll(): Int {
        val dao : OrderItemPayDao = baseDao as OrderItemPayDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<OrderItemPay> {
        val dao : OrderItemPayDao = baseDao as OrderItemPayDao
        return dao.loadAll()
    }

    fun findItemPays(tradeNo : String,itemId : String) : MutableList<OrderItemPay> {
        val dao : OrderItemPayDao = baseDao as OrderItemPayDao
        return dao.findItemPays(tradeNo,itemId)
    }

    fun deletePracticeItems(tradeNo : String) :Int{
        val dao : OrderItemPayDao = baseDao as OrderItemPayDao
        return dao.deletePracticeItems(tradeNo)
    }
}