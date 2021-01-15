package com.tranpos.vpos.db.manger

import com.tranpos.vpos.base.BaseDBManager
import com.tranpos.vpos.base.BaseDao
import com.tranpos.vpos.db.AppDatabase
import com.tranpos.vpos.db.dao.PromotionGiftProductDao
import com.tranpos.vpos.entity.PromotionGiftProduct


/**
 *CREATED BY AM
 *ON 2020/8/25
 *DESCRIPTION:
 **/
object PromotionGiftProductDbManger : BaseDBManager<PromotionGiftProduct>() {
    override fun getDataBaseDao(): BaseDao<PromotionGiftProduct> {
        return AppDatabase.getDatabase().promotionGiftProductDao
    }

    override fun deleteAll(): Int {
        val dao : PromotionGiftProductDao = baseDao as PromotionGiftProductDao
        return dao.deleteAll()
    }

    override fun loadAll(): MutableList<PromotionGiftProduct> {
        val dao : PromotionGiftProductDao = baseDao as PromotionGiftProductDao
        return dao.loadAll()
    }
}