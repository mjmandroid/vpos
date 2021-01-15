package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.PromotionProductDao
import com.tranpos.vpos.entity.PromotionProduct


/**
 *CREATED BY AM
 *ON 2020/8/25
 *DESCRIPTION:
 **/
object PromotionProductDbManger : BaseDBManager<PromotionProduct>() {
    override fun getDataBaseDao(): BaseDao<PromotionProduct> {
        return AppDatabase.getDatabase().promotionProductDao
    }

    override fun deleteAll(): Int {
        val dao : PromotionProductDao = baseDao as PromotionProductDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<PromotionProduct> {
        val dao : PromotionProductDao = baseDao as PromotionProductDao
        return dao.loadAll()
    }

    fun checkPromotionProduct(promotionId : String) : MutableList<PromotionProduct>{
        val dao : PromotionProductDao = baseDao as PromotionProductDao
        return dao.checkPromotionProduct(promotionId)
    }
}