package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.PromotionBrandDao
import com.tranpos.vpos.entity.PromotionBrand


/**
 *CREATED BY AM
 *ON 2020/8/25
 *DESCRIPTION:
 **/
object PromotionBrandDbManger : BaseDBManager<PromotionBrand>() {
    override fun getDataBaseDao(): BaseDao<PromotionBrand> {
        return AppDatabase.getDatabase().promotionBrandDao
    }

    override fun deleteAll(): Int {
        val dao : PromotionBrandDao = baseDao as PromotionBrandDao
        return dao.deleteAll()
    }


    override fun loadAll(): MutableList<PromotionBrand> {
        val dao : PromotionBrandDao = baseDao as PromotionBrandDao
        return dao.loadAll()
    }

    fun checkPromotionBrand(promotionId : String) : MutableList<PromotionBrand>{
        val dao : PromotionBrandDao = baseDao as PromotionBrandDao
        return dao.checkPromotionBrand(promotionId)
    }
}