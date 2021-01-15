package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.OrderItemDao
import com.tranpos.vpos.entity.OrderItem


object OrderItemDbManger : BaseDBManager<OrderItem>(){


    override fun getDataBaseDao(): BaseDao<OrderItem> {
        return AppDatabase.getDatabase().orderItemDao
    }

    override fun delete(bean: OrderItem?): Int {
        val dao : OrderItemDao = baseDao as OrderItemDao
        return dao.delete(bean)
    }

    override fun loadAll(): MutableList<OrderItem> {
        val dao : OrderItemDao = baseDao as OrderItemDao
        return dao.loadAll()
    }

    fun findOrderItemsByTradeno(tradeNo : String) : MutableList<OrderItem>{
        val dao : OrderItemDao = baseDao as OrderItemDao
        return dao.findByTradeNo(tradeNo)
    }

    fun deleteItems(tradeNo : String) : Int{
        val dao : OrderItemDao = baseDao as OrderItemDao
        return dao.deleteItems(tradeNo)
    }

}