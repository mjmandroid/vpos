package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.PromotionOrderDao
import com.tranpos.vpos.entity.PromotionOrder


object PromotionOrderDbManger : BaseDBManager<PromotionOrder>(){

    override fun getDataBaseDao(): BaseDao<PromotionOrder> {
        return AppDatabase.getDatabase().promotionOrderDao
    }

    override fun deleteAll(): Int {
        val dao : PromotionOrderDao = baseDao as PromotionOrderDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<PromotionOrder> {
        val dao : PromotionOrderDao = baseDao as PromotionOrderDao
        return dao.loadAll()
    }

    fun findPromotionOrders(tradeNo: String): MutableList<PromotionOrder> {
        val dao : PromotionOrderDao = baseDao as PromotionOrderDao
        return dao.findPromotionOrders(tradeNo)
    }

    fun deleteCurrentRecord(tradeNo: String):Int{
        val  dao : PromotionOrderDao = baseDao as PromotionOrderDao
        return  dao.deleteCurrentRecord(tradeNo)
    }
}