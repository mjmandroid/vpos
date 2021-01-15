package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.PromotionProductBlackDao
import com.tranpos.vpos.entity.PromotionProductBlack


/**
 *CREATED BY AM
 *ON 2020/8/25
 *DESCRIPTION:
 **/
object PromotionProductBlackDbManger : BaseDBManager<PromotionProductBlack>() {
    override fun getDataBaseDao(): BaseDao<PromotionProductBlack> {
        return AppDatabase.getDatabase().promotionProductBlackDao
    }

    override fun deleteAll(): Int {
        val dao : PromotionProductBlackDao = baseDao as PromotionProductBlackDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<PromotionProductBlack> {
        val dao : PromotionProductBlackDao = baseDao as PromotionProductBlackDao
        return dao.loadAll()
    }

    fun checkBlackProduct(specsId : String) : PromotionProductBlack?{
        val dao : PromotionProductBlackDao = baseDao as PromotionProductBlackDao
        return dao.checkBlackProduct(specsId)
    }
}