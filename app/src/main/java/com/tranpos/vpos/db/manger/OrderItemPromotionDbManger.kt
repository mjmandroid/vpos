package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.OrderItemPromotionDao
import com.tranpos.vpos.entity.PromotionItem


object OrderItemPromotionDbManger : BaseDBManager<PromotionItem>() {
    override fun getDataBaseDao(): BaseDao<PromotionItem> {
        return AppDatabase.getDatabase().orderItemPromotionDao
    }


    override fun deleteAll(): Int {
        val dao : OrderItemPromotionDao = baseDao as OrderItemPromotionDao
        return dao.deleteAll()
    }


    override fun loadAll(): MutableList<PromotionItem> {
        val dao : OrderItemPromotionDao = baseDao as OrderItemPromotionDao
        return dao.loadAll()
    }
}