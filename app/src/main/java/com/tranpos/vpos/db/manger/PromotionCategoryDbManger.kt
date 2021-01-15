package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.PromotionCategoryDao
import com.tranpos.vpos.entity.PromotionCategory


/**
 *CREATED BY AM
 *ON 2020/8/25
 *DESCRIPTION:
 **/
object PromotionCategoryDbManger : BaseDBManager<PromotionCategory>() {
    override fun getDataBaseDao(): BaseDao<PromotionCategory> {
        return AppDatabase.getDatabase().promotionCategoryDao
    }

    override fun deleteAll(): Int {
        val dao : PromotionCategoryDao = baseDao as PromotionCategoryDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<PromotionCategory> {
        val dao : PromotionCategoryDao = baseDao as PromotionCategoryDao
        return dao.loadAll()
    }

    fun checkPromotionCategory(promotionId : String) : MutableList<PromotionCategory>{
        val dao : PromotionCategoryDao = baseDao as PromotionCategoryDao
        return dao.checkPromotionCategory(promotionId)
    }
}